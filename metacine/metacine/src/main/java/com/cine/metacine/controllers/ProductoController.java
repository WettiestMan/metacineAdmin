package com.cine.metacine.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.cine.metacine.models.Producto;
import com.cine.metacine.services.ProductoRepository;
import com.cine.metacine.services.TipoProductoRepository;

import java.nio.file.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoRepository repository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Producto> productos = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        String dirAlmacen = "public/images/productos/";
        for (Producto producto :productos){
            Path imagePath = Paths.get(dirAlmacen + producto.getImagen());
            if (!Files.exists(imagePath)){
                producto.setImagen("default.jpg");
            }
        }
        model.addAttribute("productos", productos);
        return "productos/index";
    }
    
}
