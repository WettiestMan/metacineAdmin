package com.cine.metacine.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.cine.metacine.models.Clasificacion;
import com.cine.metacine.models.Genero;
import com.cine.metacine.models.Idioma;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.*;

public class PeliculaDto {
    @NotEmpty(message = "El nombre es requerido.")
    private String nombre;

    @Size(min = 10, message = "El minimo de caracteres para la descripcion es 10")
    @Size(max = 1000, message = "El maximo de caracteres para la descripcion es 1000")
    private String descripcion;

    @Min(value = 1, message = "La duración debe ser mayor a 0.")
    private int duracion;

    @NotNull(message = "La fecha de estreno es requerida.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecEstreno;

    @NotEmpty(message = "Al menos un género debe ser seleccionado.")
    private List<Genero> generos = new ArrayList<>();

    @NotEmpty(message = "Al menos un idioma debe ser seleccionado.")
    private List<Idioma> idiomas;

    private Clasificacion clasificacion;

    private MultipartFile portada;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return this.duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Date getFecEstreno() {
        return this.fecEstreno;
    }

    public void setFecEstreno(Date fecEstreno) {
        this.fecEstreno = fecEstreno;
    }

    public MultipartFile getPortada() {
        return this.portada;
    }

    public void setPortada(MultipartFile portada) {
        this.portada = portada;
    }

    public List<Genero> getGeneros() {
        return this.generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public List<Idioma> getIdiomas() {
        return this.idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }
}
