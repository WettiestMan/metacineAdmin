package com.cine.metacine.dto;

import jakarta.validation.constraints.NotEmpty;

public class TipoProductoDto {
    @NotEmpty(message = "El nombre del género es requerido")
    private String nombre;
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
