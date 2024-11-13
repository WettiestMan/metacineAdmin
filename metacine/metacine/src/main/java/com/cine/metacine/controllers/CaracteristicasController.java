package com.cine.metacine.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cine.metacine.dto.ClasificacionDto;
import com.cine.metacine.dto.GeneroDto;
import com.cine.metacine.dto.IdiomaDto;
import com.cine.metacine.dto.TipoProductoDto;
import com.cine.metacine.models.Clasificacion;
import com.cine.metacine.models.Genero;
import com.cine.metacine.models.Idioma;
import com.cine.metacine.models.TipoProducto;
import com.cine.metacine.services.ClasificacionRepository;
import com.cine.metacine.services.GeneroRepository;
import com.cine.metacine.services.IdiomaRepository;
import com.cine.metacine.services.TipoProductoRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/caracteristicas")
public class CaracteristicasController {
    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private IdiomaRepository idiomaRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @GetMapping({"", "/"})
    public String showCharacteristicsLists(Model model) {
        final List<Clasificacion> clasificaciones = clasificacionRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        final List<Genero> generos = generoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        final List<Idioma> idiomas = idiomaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        final List<TipoProducto> tipoProductos = tipoProductoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("clasificaciones", clasificaciones);
        model.addAttribute("generos", generos);
        model.addAttribute("idiomas", idiomas);
        model.addAttribute("tipoProductos", tipoProductos);

        return "caracteristicas/index";
    }
    
    @GetMapping("/create/clasificacion")
    public String showCreateClasificacionPage(Model model) {
        var nuevaClasificacion = new ClasificacionDto();
        
        model.addAttribute("clasificacionDto", nuevaClasificacion);

        return "caracteristicas/crearClasificacion";
    }

    @PostMapping("/create/clasificacion")
    public String createClasificacion(
        @Valid @ModelAttribute ClasificacionDto clasificacionDto,
        BindingResult result,
        Model model
    ) {
        if (clasificacionDto.getNombre() == null || clasificacionDto.getNombre().isBlank()) {
            result.rejectValue("nombre", "error.clasificacionDto.noName", "No se puede crear una nueva clasificación sin un nombre");
        }

        if (result.hasErrors())
            return "caracteristicas/crearClasificacion";

        var nuevaClasificacion = new Clasificacion(clasificacionDto.getNombre());

        clasificacionRepository.save(nuevaClasificacion);

        return "redirect:/caracteristicas";
    }

    @GetMapping("/create/genero")
    public String showCreateGeneroPage(Model model) {
        var nuevoGenero = new GeneroDto();
        
        model.addAttribute("generoDto", nuevoGenero);

        return "caracteristicas/crearGenero";
    }

    @PostMapping("/create/genero")
    public String createGenero(
        @Valid @ModelAttribute GeneroDto generoDto,
        BindingResult result,
        Model model
    ) {
        if (generoDto.getNombre() == null || generoDto.getNombre().isBlank()) {
            result.rejectValue("nombre", "error.generoDto.noName", "No se puede crear un nuevo género sin un nombre");
        }

        if (result.hasErrors())
            return "caracteristicas/crearGenero";

        var nuevoGenero = new Genero(generoDto.getNombre());

        generoRepository.save(nuevoGenero);

        return "redirect:/caracteristicas";
    }

    @GetMapping("/create/idioma")
    public String showCreateIdiomaPage(Model model) {
        var nuevoIdioma = new IdiomaDto();
        
        model.addAttribute("idiomaDto", nuevoIdioma);

        return "caracteristicas/crearIdioma";
    }

    @PostMapping("/create/idioma")
    public String createIdioma(
        @Valid @ModelAttribute IdiomaDto idiomaDto,
        BindingResult result,
        Model model
    ) {
        if (idiomaDto.getNombre() == null || idiomaDto.getNombre().isBlank()) {
            result.rejectValue("nombre", "error.idiomaDto.noName", "No se puede crear un nuevo idioma sin un nombre");
        }

        if (result.hasErrors())
            return "caracteristicas/crearIdioma";

        var nuevoIdioma = new Idioma();
        nuevoIdioma.setNombre(idiomaDto.getNombre());

        idiomaRepository.save(nuevoIdioma);

        return "redirect:/caracteristicas";
    }

    @GetMapping("/create/tipoProducto")
    public String showCreateTipoProductoPage(Model model) {
        var nuevoTipoProd = new TipoProductoDto();
        
        model.addAttribute("tipoProdDto", nuevoTipoProd);

        return "caracteristicas/crearTipoProd";
    }

    @PostMapping("/create/tipoProducto")
    public String createTipoProducto(
        @Valid @ModelAttribute TipoProductoDto tipoProdDto,
        BindingResult result,
        Model model
    ) {
        if (tipoProdDto.getNombre() == null || tipoProdDto.getNombre().isBlank()) {
            result.rejectValue("nombre", "error.generoDto.noName", "No se puede crear un nuevo género sin un nombre");
        }

        if (result.hasErrors())
            return "caracteristicas/crearTipoProd";

        var nuevoTipoProd = new TipoProducto(tipoProdDto.getNombre());

        tipoProductoRepository.save(nuevoTipoProd);

        return "redirect:/caracteristicas";
    }

    @GetMapping("/delete/clasificacion")
    public String deleteClasificacion(@RequestParam Long id) {
        try {
            Optional<Clasificacion> aEliminar = clasificacionRepository.findById(id);
            if (aEliminar.isEmpty()) {
                System.err.println("[ERROR] Eliminar clasificación: Elemento no encontrado");
            } else {
                clasificacionRepository.delete(aEliminar.get());
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción: " + e.getMessage());
        }

        return "redirect:/caracteristicas";
    }

    @GetMapping("/delete/genero")
    public String deleteGenero(@RequestParam Long id) {
        try {
            Optional<Genero> aEliminar = generoRepository.findById(id);
            if (aEliminar.isEmpty()) {
                System.err.println("[ERROR] Eliminar clasificación: Elemento no encontrado");
            } else {
                generoRepository.delete(aEliminar.get());
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción: " + e.getMessage());
        }

        return "redirect:/caracteristicas";
    }

    @GetMapping("/delete/idioma")
    public String deleteIdioma(@RequestParam Long id) {
        try {
            Optional<Idioma> aEliminar = idiomaRepository.findById(id);
            if (aEliminar.isEmpty()) {
                System.err.println("[ERROR] Eliminar clasificación: Elemento no encontrado");
            } else {
                idiomaRepository.delete(aEliminar.get());
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción: " + e.getMessage());
        }

        return "redirect:/caracteristicas";
    }

    @GetMapping("/delete/tipoProducto")
    public String deleteTipoProducto(@RequestParam Long id) {
        try {
            Optional<TipoProducto> aEliminar = tipoProductoRepository.findById(id);
            if (aEliminar.isEmpty()) {
                System.err.println("[ERROR] Eliminar clasificación: Elemento no encontrado");
            } else {
                tipoProductoRepository.delete(aEliminar.get());
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción: " + e.getMessage());
        }

        return "redirect:/caracteristicas";
    }
}
