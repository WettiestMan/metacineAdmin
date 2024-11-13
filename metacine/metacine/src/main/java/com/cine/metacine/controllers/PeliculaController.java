package com.cine.metacine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.cine.metacine.dto.PeliculaDto;
import com.cine.metacine.models.Clasificacion;
import com.cine.metacine.models.Genero;
import com.cine.metacine.models.Idioma;
import com.cine.metacine.models.Pelicula;
import com.cine.metacine.services.ClasificacionRepository;
import com.cine.metacine.services.GeneroRepository;
import com.cine.metacine.services.IdiomaRepository;
import com.cine.metacine.services.PeliculaRepository;
import java.util.*;
import java.nio.file.*;
import java.io.*;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {
    @Autowired
    private PeliculaRepository repository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private IdiomaRepository idiomaRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        List<Pelicula> peliculas = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        String dirAlmacen = "public/images/peliculas/";
        for (Pelicula pelicula : peliculas) {
            Path imagePath = Paths.get(dirAlmacen + pelicula.getPeliPortada());
            if (!Files.exists(imagePath)) {
                pelicula.setPeliPortada("default.jpg");
            }
        }

        model.addAttribute("peliculas", peliculas);
        return "peliculas/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        PeliculaDto peliculaDto = new PeliculaDto();
        List<Genero> generos = generoRepository.findAll();
        List<Idioma> idiomas = idiomaRepository.findAll();
        List<Clasificacion> clasificaciones = clasificacionRepository.findAll();
        model.addAttribute("generos", generos);
        model.addAttribute("idiomas", idiomas);
        model.addAttribute("clasificaciones", clasificaciones);
        model.addAttribute("peliculaDto", peliculaDto);
        return "peliculas/crearPelicula";
    }

    @PostMapping("/create")
    public String createPelicula(
            @Valid @ModelAttribute PeliculaDto peliculaDto, 
            BindingResult result, 
            Model model,
            @RequestParam (required = false) List<Genero> generos, 
            @RequestParam (required = false) List<Idioma> idiomas,
            @RequestParam (required = false) Long clasificacionId) {

        if (generos == null || generos.isEmpty()){
            result.rejectValue("generos", "error.peliculaDto", "Debe seleccionar al menos un genero.");
        }

        if (peliculaDto.getPortada().isEmpty()) {
            result.addError(new FieldError("peliculaDto", "portada", "La imagen es requerida."));
        }

        if (idiomas == null || idiomas.isEmpty()) {
            result.rejectValue("idiomas", "error.peliculaDto", "Debe seleccionar al menos un idioma.");
        }

        if (clasificacionId == null) {
            result.rejectValue("clasificacionId", "error.peliculaDto", "Debe seleccionar una clasificación.");
        }

        if (result.hasErrors()) {
            model.addAttribute("generos", generoRepository.findAll()); 
            model.addAttribute("idiomas", idiomaRepository.findAll());  
            model.addAttribute("clasificaciones", clasificacionRepository.findAll());
            return "peliculas/crearPelicula";
        }

        MultipartFile portada = peliculaDto.getPortada();
        Date fecCreacion = new Date();
        String nombreImagenGuardada = fecCreacion.getTime() + "_" + portada.getOriginalFilename();

        try {
            String dirAlmacen = "public/images/peliculas/";
            Path dirCarga = Paths.get(dirAlmacen);

            if (!Files.exists(dirCarga)) {
                Files.createDirectories(dirCarga);
            }

            try (InputStream inputStream = portada.getInputStream()) {
                Files.copy(inputStream, Paths.get(dirAlmacen + nombreImagenGuardada),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setNombre(peliculaDto.getNombre());
        pelicula.setDescripcion(peliculaDto.getDescripcion());
        pelicula.setDuracion(peliculaDto.getDuracion());
        pelicula.setFecEstreno(peliculaDto.getFecEstreno());
        pelicula.setPeliPortada(nombreImagenGuardada);

        Clasificacion clasificacion = clasificacionRepository.findById(clasificacionId).orElse(null);
        pelicula.setClasificacion(clasificacion);
        pelicula.setGeneros(generos);
        pelicula.setIdiomas(idiomas);

        repository.save(pelicula);

        return "redirect:/peliculas";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Pelicula pelicula = repository.findById(id).get();

            String dirAlmacen = "public/images/peliculas/";
            Path imagePath = Paths.get(dirAlmacen + pelicula.getPeliPortada());
            if (!Files.exists(imagePath)) {
                pelicula.setPeliPortada("default.jpg");
            }

            model.addAttribute("pelicula", pelicula);

            PeliculaDto peliculaDto = new PeliculaDto();
            peliculaDto.setNombre(pelicula.getNombre());
            peliculaDto.setDescripcion(pelicula.getDescripcion());
            peliculaDto.setDuracion(pelicula.getDuracion());
            peliculaDto.setFecEstreno(pelicula.getFecEstreno());

            List<Clasificacion> clasificaciones = clasificacionRepository.findAll();
            List<Genero> generos = generoRepository.findAll();
            List<Idioma> idiomas = idiomaRepository.findAll();

            model.addAttribute("generos", generos);
            model.addAttribute("clasificaciones", clasificaciones);
            model.addAttribute("idiomas", idiomas);

            model.addAttribute("selectedGeneros", pelicula.getGeneros());
            model.addAttribute("selectedIdiomas", pelicula.getIdiomas());

            model.addAttribute("peliculaDto", peliculaDto);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/peliculas";
        }

        return "peliculas/editarPelicula";
    }

    @PostMapping("/edit")
    public String actualizarPelicula(Model model, @RequestParam Long id, @Valid @ModelAttribute PeliculaDto peliculaDto,
            BindingResult result, @RequestParam List<Genero> generos, @RequestParam List<Idioma> idiomas,
            @RequestParam Long clasificacionId) {

        try {
            Pelicula pelicula = repository.findById(id).get();
                model.addAttribute("pelicula", pelicula);
            
            if (result.hasErrors()) {
                pelicula = repository.findById(id).get();
                model.addAttribute("pelicula", pelicula);
                List<Clasificacion> clasificaciones = clasificacionRepository.findAll();
                generos = generoRepository.findAll();
                idiomas = idiomaRepository.findAll();
    
                model.addAttribute("generos", generos);
                model.addAttribute("clasificaciones", clasificaciones);
                model.addAttribute("idiomas", idiomas);
    
                model.addAttribute("selectedGeneros", pelicula.getGeneros());
                model.addAttribute("selectedIdiomas", pelicula.getIdiomas());
                return "peliculas/editarPelicula";
            }

            if (!peliculaDto.getPortada().isEmpty()) {
                // Borrar al imagen antigua
                String dirAlmacen = "public/images/peliculas/";
                Path antiguaDirCarga = Paths.get(dirAlmacen + pelicula.getPeliPortada());

                try {
                    Files.delete(antiguaDirCarga);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }

                // guardar la nueva imagen
                MultipartFile portada = peliculaDto.getPortada();
                Date fecCreacion = new Date();
                String nombreImagenGuardada = fecCreacion.getTime() + "_" + portada.getOriginalFilename();

                try (InputStream inputStream = portada.getInputStream()) {
                    Files.copy(inputStream, Paths.get(dirAlmacen + nombreImagenGuardada),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                pelicula.setPeliPortada(nombreImagenGuardada);
            }

            pelicula.setNombre(peliculaDto.getNombre());
            pelicula.setDescripcion(peliculaDto.getDescripcion());
            pelicula.setDuracion(peliculaDto.getDuracion());
            pelicula.setFecEstreno(peliculaDto.getFecEstreno());
            pelicula.setGeneros(generos);
            pelicula.setIdiomas(idiomas);
            Clasificacion clasificacion = clasificacionRepository.findById(clasificacionId).orElse(null);
            pelicula.setClasificacion(clasificacion);

            repository.save(pelicula);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/delete")
    public String borraPelicula(@RequestParam Long id) {

        try {
            Pelicula pelicula = repository.findById(id).get();

            // borrar la portada
            Path portadaDir = Paths.get("public/images/peliculas/" + pelicula.getPeliPortada());

            try {
                Files.delete(portadaDir);

            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }

            repository.delete(pelicula);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/peliculas";
    }

}