package com.hotel.encuesta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name="encuestas")
public class Encuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Este campo debe tener al menos 2 caracteres.")
    private String nombre;

    @Size(min = 2, message = "Este campo deben tener al menos 2 caracteres.")
    private String apellidos;

    @Email(message = "Debes proporcionar un correo electrónico válido.")
    private String email;

    @Min(value = 18, message = "Debes tener al menos 18 años")
    private Integer edad;

    @NotNull(message = "Este campo no puede estar vacío o solo contener espacios.")
    private Integer telefono;

    @NotNull(message = "Este campo no puede estar vacío o solo contener espacios.")
    @PastOrPresent(message = "La fecha debe ser igual o anterior al día de hoy.")
    private LocalDate fechaInicioEstancia;

    @NotBlank(message = "Debe seleccionar un motivo de visita.")
    private String motivoVisita;

    private Boolean serviciosUtilizados;

    @NotBlank(message = "Es necesario marcar alguna opción.")
    private String satisfaccion;

    private String comentarios;


    public Encuesta() {
    }

    public Encuesta(Long id, String email, String nombre, String apellidos, Integer edad, Integer telefono, LocalDate fechaInicioEstancia, String motivoVisita, String satisfaccion, Boolean serviciosUtilizados, String comentarios) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.telefono = telefono;
        this.fechaInicioEstancia = fechaInicioEstancia;
        this.motivoVisita = motivoVisita;
        this.satisfaccion = satisfaccion;
        this.serviciosUtilizados = serviciosUtilizados;
        this.comentarios = comentarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaInicioEstancia() {
        return fechaInicioEstancia;
    }

    public void setFechaInicioEstancia(LocalDate fechaInicioEstancia) {
        this.fechaInicioEstancia = fechaInicioEstancia;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }

    public Boolean getServiciosUtilizados() {
        return serviciosUtilizados;
    }

    public void setServiciosUtilizados(Boolean serviciosUtilizados) {
        this.serviciosUtilizados = serviciosUtilizados;
    }

    public String getSatisfaccion() {
        return satisfaccion;
    }

    public void setSatisfaccion(String satisfaccion) {
        this.satisfaccion = satisfaccion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
