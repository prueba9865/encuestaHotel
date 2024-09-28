## ¿Qué sucede si intentas borrar una encuesta que no existe? ¿Cómo lo has controlado?

Me redirige a un archivo ".html" que he hecho aposta para que me indique que la encuesta no existe, lo he planteado asi:


```
  @GetMapping("/admin/encuestas/del/{id}")
    public String delete(@PathVariable Long id) {
        if(encuestaRepository.existsById(id)){
            encuestaRepository.deleteById(id);
            return "redirect:/admin/encuestas";
        }
        return "idNotFound";
    }
```
Donde compruebo si el ID por el que intento eliminarlo existe, si es asi lo elimina y redirige al listado de encuestas, y si no lo encuentra me lleva a "idNotFound", es mi documento ".html" que muestra el error personalizado.

## Si introduces en un texto del tipo "<style>body background-color:red</style>" en uno de los campos. ¿Qué sucede al ver la encuesta?, ¿el navegador ejecuta ese código o no? ¿porqué?, ¿cómo podrías hacer que se ejecute ese código o que se deje de ejecutar?
Ese codigo no se ejecutaria porque esta mal escrito, se escribiria "<style>body { background-color: red; }</style>", antes le faltaban las "{}" y el ";" al final para cerrarlo, poniendolo como yo lo he puesto si se ejecutaria, pero habria que tener en cuenta si ya tenemos un color de fondo ya puesto, si es asi lo sobreescribe.

## ¿Qué has tenido que modificar en el repositorio para filtrar por motivo de búsqueda?, ¿has tenido que escribir el código específico o como lo has realizado?
En mi caso no he tenido que modificar el repositorio, simplemente he añadido un nuevo metodo como el siguiente:

```
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

        return "encuesta-list"; // nombre de la vista que muestra las encuestas
    }
```

En resumen lo que hacemos es recibir en este metodo un parametro, y si en ese parametro no hay nada, meteme en una lista todas las encuestas que haya y envialas a la vista (esto sucederia si no seleccionamos ninguna opcion del desplegable), pero si hay algo en el parametro, filtramos por el nivel de satisfaccion que hay en la encuesta correspondiente y lo comparamos con el que recibimos por GET en el parametro, y si es igual lo almacenamos en nuestra lista, por lo que ya tendriamos la/las encuestas que necesitemos filtradas, solo quedaria enviarlas a la vista, ademas tambien envio a la vista la informacion que quiero que aparezca en el desplegable, que basicamente son los valores de una lista donde estan reflejados los diferentes niveles:

```
  private List<String> getNivelesSatisfaccion() {
        return Arrays.asList("Muy Satisfecho", "Satisfecho", "Neutral", "Insatisfecho", "Muy Insatisfecho");
    }
```
El archimo ".html" que recibe la informacion ya se encarga de mostrar en el desplegable de manera dinamica gracias al atributo "nivelesSatisfaccion" de la siguiente manera:

```
  <option th:each="nivel : ${nivelesSatisfaccion}" th:value="${nivel}" th:text="${nivel}"></option>
```
