package com.cine.metacine.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.*;

@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private int duracion;
    private Date fecEstreno;
    private String peliPortada;

    @ManyToMany
    @JoinTable(name = "pelicula_genero", joinColumns = @JoinColumn(name = "pelicula_id"), inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private List<Genero> generos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "pelicula_idioma", joinColumns = @JoinColumn(name = "pelicula_id"), inverseJoinColumns = @JoinColumn(name = "idioma_id"))
    private List<Idioma> idiomas;

    @ManyToOne
    @JoinColumn(name = "clasificacion_id")
    private Clasificacion clasificacion;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPeliPortada() {
        return this.peliPortada;
    }

    public void setPeliPortada(String peliPortada) {
        this.peliPortada = peliPortada;
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

    public Pelicula() {
    }

}
