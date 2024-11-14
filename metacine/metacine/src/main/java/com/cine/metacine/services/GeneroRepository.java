package com.cine.metacine.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.metacine.models.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long>{
    List<Genero> findAll();  // ¿no que este de aquí es autogenerado?
}
