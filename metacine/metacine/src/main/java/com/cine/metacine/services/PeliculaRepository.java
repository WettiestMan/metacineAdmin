package com.cine.metacine.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.metacine.models.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long>{

}
 