package com.hotel.encuesta.controller;

import com.hotel.encuesta.entity.Encuesta;
import com.hotel.encuesta.repository.EncuestaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EncuestaController {
    private final EncuestaRepository encuestaRepository;

    public EncuestaController(EncuestaRepository repository) {
        this.encuestaRepository = repository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/encuestas/send")
    public String mensajeEncuestaEnviada(Model model) {
        return "encuesta-send";
    }

    @GetMapping("/admin")
    public String redirectAdmin(Model model) {
        return "redirect:/admin/encuestas";
    }

    @GetMapping("/admin/encuestas")
    public String mostrarEncuestas(Model model) {
        List<Encuesta> encuestas = encuestaRepository.findAll();
        model.addAttribute("encuestas", encuestas);
        model.addAttribute("nivelesSatisfaccion", getNivelesSatisfaccion());
        model.addAttribute("numeroEncuestas", encuestas.size());
        model.addAttribute("promedioEdad", calcularPromedioEdad(encuestas));
        model.addAttribute("desgloseSatisfaccion", calcularDesgloseSatisfaccion(encuestas));

        return "encuesta-list";
    }

    @GetMapping("/encuestas/new")
    public String mostrarEncuesta(Model model) {
        model.addAttribute("encuesta", new Encuesta());
        model.addAttribute("nivelesSatisfaccion", getNivelesSatisfaccion());
        return "encuesta-new";
    }

    @PostMapping("/encuestas/new")
    public String añadirEncuesta(Model model, @Valid @ModelAttribute Encuesta encuesta, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("nivelesSatisfaccion", getNivelesSatisfaccion());
            return "encuesta-new";
        }
        encuestaRepository.save(encuesta);
        return "redirect:/encuestas/send";
    }

    @GetMapping("/admin/encuestas/view/{id}")
    public String mostrarEncuesta(Model model, @PathVariable Long id) {
        Optional<Encuesta> e = encuestaRepository.findById(id);
        if (e.isPresent()) {
            model.addAttribute("encuesta", e.get());
            return "encuesta-view";
        } else {
            return "redirect:/encuestas"; // Manejo de error si no se encuentra la encuesta
        }
    }

    @GetMapping("/admin/encuestas/del/{id}")
    public String delete(@PathVariable Long id) {
        if(encuestaRepository.existsById(id)){
            encuestaRepository.deleteById(id);
            return "redirect:/admin/encuestas";
        }
        return "idNotFound";
    }

    @GetMapping("/admin/encuestas/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        Optional<Encuesta> encuesta = encuestaRepository.findById(id);
        if (encuesta.isEmpty()) {
            return "error"; // Manejar el caso en el que la encuesta no exista
        }
        model.addAttribute("encuesta", encuesta.get());
        model.addAttribute("nivelesSatisfaccion", getNivelesSatisfaccion());
        return "encuesta-edit"; // Vista para editar
    }

    @PostMapping("/admin/encuestas/edit/{id}")
    public String updateEncuesta(@PathVariable Long id, @Valid @ModelAttribute Encuesta encuesta, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "encuesta-edit";
        }
        encuesta.setId(id);
        encuestaRepository.save(encuesta);
        return "redirect:/admin/encuestas"; // Redirigir a la lista de encuestas
    }

    @GetMapping("/admin/encuestas/filtrar")
    public String filtrarEncuestas(@RequestParam(required = false) String satisfaccion, Model model) {
        List<Encuesta> encuestasFiltradas = new ArrayList<>();

        // Si la satisfacción es null o vacío, simplemente obten todas las encuestas
        if (satisfaccion == null || satisfaccion.isEmpty()) {
            encuestasFiltradas = encuestaRepository.findAll();
        } else {
            // Filtramos las encuestas basándonos en la satisfacción seleccionada
            encuestasFiltradas = encuestaRepository.findAll().stream()
                    .filter(encuesta -> encuesta.getSatisfaccion().equals(satisfaccion))
                    .collect(Collectors.toList());
        }

        model.addAttribute("encuestas", encuestasFiltradas);
        model.addAttribute("nivelesSatisfaccion", getNivelesSatisfaccion());
        model.addAttribute("numeroEncuestas", encuestasFiltradas.size());
        model.addAttribute("promedioEdad", calcularPromedioEdad(encuestasFiltradas));
        model.addAttribute("desgloseSatisfaccion", calcularDesgloseSatisfaccion(encuestasFiltradas));

        return "encuesta-list"; // nombre de la vista que muestra las encuestas
    }


    private List<String> getNivelesSatisfaccion() {
        return Arrays.asList("Muy Satisfecho", "Satisfecho", "Neutral", "Insatisfecho", "Muy Insatisfecho"); // Puedes modificar esto según tus necesidades
    }

    private double calcularPromedioEdad(List<Encuesta> encuestas) {
        int totalEdad = 0;
        int contador = 0;

        for (Encuesta encuesta : encuestas) {
            if (encuesta.getEdad() != null) { // Asegúrate de que la edad no sea nula
                totalEdad += encuesta.getEdad();
                contador++;
            }
        }

        return (contador > 0) ? (double) totalEdad / contador : 0.0; // Retorna 0 si no hay encuestas
    }

    private Map<String, Double> calcularDesgloseSatisfaccion(List<Encuesta> encuestas) {
        Map<String, Double> desglose = new HashMap<>();
        int total = encuestas.size();

        if (total == 0) {
            return desglose; // Retornar vacío si no hay encuestas
        }

        for (String nivel : getNivelesSatisfaccion()) {
            long count = encuestas.stream().filter(encuesta -> encuesta.getSatisfaccion().equals(nivel)).count();
            double percentage = (double) count / total * 100;
            desglose.put(nivel, percentage);
        }

        return desglose;
    }
}

