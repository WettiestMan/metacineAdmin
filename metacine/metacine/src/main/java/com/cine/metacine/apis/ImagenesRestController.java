package com.cine.metacine.apis;

import java.util.Collections;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cine.metacine.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/imagenes")
public class ImagenesRestController {

    private static final String DIRECTORIO_PORTADAS = "public/images/peliculas/";
    private static final Path PORTADA_DEFECTO = Paths.get("public/images/peliculas/default.jpg");
    private static final String DIRECTORIO_PRODUCTOS = "public/images/productos/";
    private static final Path PRODUCTO_DEFECTO = Paths.get("public/images/productos/default.jpg");

    private static final Map<String, MediaType> extensionMapping = Map.of(
        ".jpg", MediaType.IMAGE_JPEG,
        ".jpeg", MediaType.IMAGE_JPEG,
        ".png", MediaType.IMAGE_PNG,
        ".webp", MediaType.valueOf("image/webp")
    );

    /**
     * Ruta: <server>/api/v1/imagenes/peliculas/{filename}
     * obtenerPortada() obtiene una imagen de portada de una película, según el valor
     * de filename. En caso de no encontrar la imagen pedida, retorna una imagen por
     * defecto. filename NO ES UN PATH, es un nombre de archivo que puede obtenerse de
     * Pelicula.peliPortada
     * 
     * @param filename String: el nombre del archivo a recuperar
     * @return 200 si devuelve la imagen pedida
     *         204 si no se encontró la imagen perdida, pero retornó la imagen por defecto
     *         400 si se pide un archivo de extensión incompatible (los tipos compatibles
     *         actualmente son .jpg, .jpeg, .png y .webp) (devuelve un JSON)
     *         404 si no encontró la imágen pedida y no pudo devolver la imagen por defecto
     *         500 si ocurre un error en el servidor (devuelve un JSON)
     */
    @GetMapping("/peliculas/{filename:.+}")
    public ResponseEntity<Resource> obtenerPortada(@PathVariable String filename) {
        final int extIndex = filename.lastIndexOf('.');
        final String extension = filename.substring(extIndex).toLowerCase();

        if (extIndex != -1 && extensionMapping.containsKey(extension)) {
            try {
                System.out.println(Paths.get(DIRECTORIO_PORTADAS).toAbsolutePath());
                final Path resolvedPath = Paths.get(DIRECTORIO_PORTADAS + filename);
                
                if (Files.exists(resolvedPath)) {
                    final var requestedImage = new ByteArrayResource(Files.readAllBytes(resolvedPath));
                    return ResponseEntity.ok()
                                        .contentType(extensionMapping.get(extension))
                                        .body(requestedImage);
                } else {
                    if (Files.exists(PORTADA_DEFECTO)) {
                        final var defaultImage = new ByteArrayResource(Files.readAllBytes(PORTADA_DEFECTO));

                        return ResponseEntity.status(HttpStatus.OK)
                                            .contentType(MediaType.IMAGE_JPEG)
                                            .body(defaultImage);
                    }

                    return ResponseEntity.notFound().build();
                }
            } catch (Exception ex) {
                try {
                    final var objMapper = new ObjectMapper();
                    final String exJson = objMapper.writeValueAsString(Collections.singletonMap("error", ex.getMessage()));

                    return Utils.stringToResourceResponse(exJson
                                            , MediaType.APPLICATION_JSON
                                            , HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (JsonProcessingException exc) {   // en caso el mensaje de la excepción no se pueda parsear.
                    return Utils.stringToResourceResponse(ex.getMessage()
                                            , MediaType.TEXT_PLAIN
                                            , HttpStatus.INTERNAL_SERVER_ERROR);
                }      
            }
        } else {
            return Utils.stringToResourceResponse("{\"error\":\"tipo de archivo no soportado, solo se aceptan los siguientes archivos: .jpg, .jpeg, .png, .webp.\"}"
                                    , MediaType.APPLICATION_JSON
                                    , HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Ruta: <server>/api/v1/imagenes/productos/{filename}
     * obtenerImagenProducto() obtiene una imagen de un producto, según el valor
     * de filename. En caso de no encontrar la imagen pedida, retorna una imagen por
     * defecto. filename NO ES UN PATH, es un nombre de archivo que puede obtenerse de
     * Producto.imagen
     * 
     * @param filename String: el nombre del archivo a recuperar
     * @return 200 si devuelve la imagen pedida
     *         204 si no se encontró la imagen perdida, pero retornó la imagen por defecto
     *         400 si se pide un archivo de extensión incompatible (los tipos compatibles
     *         actualmente son .jpg, .jpeg, .png y .webp) (devuelve un JSON)
     *         404 si no encontró la imagen pedida y no pudo devolver la imagen por defecto
     *         500 si ocurre un error en el servidor (devuelve un JSON)
     */
    @GetMapping("/productos/{filename:.+}")
    public ResponseEntity<Resource> obtenerImagenProducto(@PathVariable String filename) {
        final int extIndex = filename.lastIndexOf('.');
        final String extension = filename.substring(extIndex).toLowerCase();

        if (extIndex != -1 && extensionMapping.containsKey(extension)) {
            try {
                final Path resolvedPath = Paths.get(DIRECTORIO_PRODUCTOS).resolve(filename);
                
                if (Files.exists(resolvedPath)) {
                    final var requestedImage = new ByteArrayResource(Files.readAllBytes(resolvedPath));

                    return ResponseEntity.ok()
                                        .contentType(extensionMapping.get(extension))
                                        .body(requestedImage);
                } else {
                    if (Files.exists(PRODUCTO_DEFECTO)) {
                        final var defaultImage = new ByteArrayResource(Files.readAllBytes(PRODUCTO_DEFECTO));

                        return ResponseEntity.status(HttpStatus.OK)
                                            .contentType(MediaType.IMAGE_JPEG)
                                            .body(defaultImage);
                    }

                    return ResponseEntity.notFound().build();
                }
            } catch (Exception ex) {
                try {
                    final var objMapper = new ObjectMapper();
                    final String exJson = objMapper.writeValueAsString(Collections.singletonMap("error", ex.getMessage()));
                    
                    return Utils.stringToResourceResponse(exJson
                                            , MediaType.APPLICATION_JSON
                                            , HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (JsonProcessingException exc) {   // en caso el mensaje de la excepción no se pueda parsear.
                    return Utils.stringToResourceResponse(ex.getMessage()
                                            , MediaType.TEXT_PLAIN
                                            , HttpStatus.INTERNAL_SERVER_ERROR);
                }      
            }
        } else {
            return Utils.stringToResourceResponse("{\"error\":\"tipo de archivo no soportado, solo se aceptan los siguientes archivos: .jpg, .jpeg, .png, .webp.\"}"
                                    , MediaType.APPLICATION_JSON
                                    , HttpStatus.BAD_REQUEST);
        }
    }
}
