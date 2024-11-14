package com.cine.metacine.apis;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cine.metacine.models.Producto;
import com.cine.metacine.services.ProductoRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoRestController {
    @Autowired
    private ProductoRepository productoRepository;

    // asegúrense de añadir nuevas propiedades en orden alfabético, estoy usando
    // Arrays.binarySearch() para el chequeo de los parámetros sortBy
    private static final String[] validSorts = {"id", "nombre", "tipoProducto"};

    /**
     * Ruta: <server>/api/v1/productos
     * mostrarTodo() devuelve todas los productos de la base de datos, los datos
     * pueden ser ordenados en base al valor de sortBy
     * 
     * Para obtener las imágenes, haga un request a <serverdns>/api/v1/imagenes/productos.
     * 
     * Para evitar bucles infinitos al momento de entregar los productos al cliente,
     * las listas de productos adjuntas en los atributos tipoProducto de cada uno de
     * los productos están asignadas a null. Esto es un hack y este comportamiento
     * podría cambiar luego...
     * 
     * @param sortBy (opcional) una String que indica el atributo utilizado para el ordenado
     * @return 200 en caso de una lista con productos,
     *         404 en caso de una lista vacía 
     *         o 500 en caso de error del servidor.
     */
    @GetMapping
    public ResponseEntity<List<Producto>> mostrarTodo(@RequestParam(required = false, defaultValue = "nombre") String sortBy) {
        try {
            if (Arrays.binarySearch(validSorts, sortBy) < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            final List<Producto> productos = productoRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
            if (productos.isEmpty())
                return ResponseEntity.notFound().build();

            // HACK: véase PeliculaRestController.java para el razonamiento de esto (o el javadoc que puse arriba (. - .))
            productos.forEach(x -> {if (x.getTipoProducto() != null) x.getTipoProducto().setProductos(null);});

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
