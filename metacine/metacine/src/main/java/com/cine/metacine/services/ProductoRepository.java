package com.cine.metacine.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.metacine.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
