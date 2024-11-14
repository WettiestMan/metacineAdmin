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

import com.cine.metacine.models.Pelicula;
import com.cine.metacine.services.PeliculaRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/peliculas")
public class PeliculaRestController {
    @Autowired
    private PeliculaRepository peliculaRepository;

    // asegúrense de añadir nuevas propiedades en orden alfabético, estoy usando
    // Arrays.binarySearch() para el chequeo de los parámetros sortBy
    private static final String[] validSorts = {"clasificacion", "duracion", "id", "nombre"};

    /**
     * Ruta: <server>/api/v1/peliculas
     * mostrarTodo() devuelve todas las películas de la base de datos, los datos
     * pueden ser ordenados en base al valor de sortBy
     * 
     * Para obtener las portadas, haga un request a <server>/api/v1/imagenes/peliculas.
     * 
     * Para evitar bucles infinitos al momento de entregar las películas al cliente,
     * las listas de películas adjuntas en los atributos clasificacion, genero e idioma,
     * de cada una de las películas están asignadas a null. Esto es un hack y este comportamiento
     * podría cambiar luego...
     * 
     * @param sortBy (opcional) una String que indica el atributo utilizado para el ordenado
     * @return 200 en caso de una lista con películas,
     *         404 en caso de una lista vacía 
     *         o 500 en caso de error del servidor.
     */
    @GetMapping
    public ResponseEntity<List<Pelicula>> mostrarTodo(@RequestParam(required = false, defaultValue = "nombre") String sortBy) {
        try {
            if (Arrays.binarySearch(validSorts, sortBy) < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            final List<Pelicula> peliculas = peliculaRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
            /*if (peliculas.isEmpty())
                return ResponseEntity.notFound().build();*/

            // HACK: para evitar bucles infinitos al momento de entregar las películas al cliente, voy a asignar
            // las listas de películas adjuntas en clasificacion, genero e idioma a null (no creo que sea necesario
            // acceder a las películas desde ellos una vez recibidos ya que se tienen todas las películas)
            //
            // Podría usar los dtos de estos elementos y que estos dtos solo incluyan el nombre, pero
            // PeliculaDto usa los modelos directamente (y no quise cambiarlo para no romper nada asi
            // que lo dejé así XD).
            peliculas.forEach(
                x -> {
                    if (x.getClasificacion() != null)
                        x.getClasificacion().setPeliculas(null);
                    
                    if (x.getIdiomas() != null)
                        x.getIdiomas().forEach(i -> i.setPeliculas(null));
                    
                    if (x.getGeneros() != null)
                        x.getGeneros().forEach(g -> g.setPeliculas(null));
                }
            );

            return ResponseEntity.ok(peliculas);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}