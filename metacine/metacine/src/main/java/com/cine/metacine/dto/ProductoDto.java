package com.cine.metacine.dto;

import org.springframework.web.multipart.MultipartFile;

import com.cine.metacine.models.TipoProducto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ProductoDto {
    @NotEmpty(message = "El nombre es requerido.")
    private String nombre;

    @Size(min = 10, message = "El minimo de caracteres para la descripcion es 10")
    @Size(max = 1000, message = "El maximo de caracteres para la descripcion es 1000")
    private String descripcion;

    private MultipartFile imagen;
    
    private TipoProducto tipoProducto;
    
    @Min(value = 1, message = "El stock no puede ser 0.")
    private int stock;
    
    @Min(value = 1, message = "El precio no puede ser 0.")
    private double precio;


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

    public MultipartFile getImagen() {
        return this.imagen;
    }

    public void setImagen(MultipartFile imagen) {
        this.imagen = imagen;
    }

    public TipoProducto getTipoProducto() {
        return this.tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return this.precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
