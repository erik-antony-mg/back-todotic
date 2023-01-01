package pe.todotic.mitiendaapi_s3.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.todotic.mitiendaapi_s3.model.Libro;
import pe.todotic.mitiendaapi_s3.repository.LibroRepository;
import pe.todotic.mitiendaapi_s3.web.dto.LibroDTO;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/libros")
public class AdminLibroController {

    @Autowired
    private LibroRepository libroRepository;

    /**
     * Devuelve la lista completa de libros
     * Retorna el status OK: 200
     * Ej.: GET http://localhost:9090/api/libros/listar
     */
    @GetMapping("/listar")
    List<Libro> listar() {
        return libroRepository.findAll();
    }

    /**
     * Devuelve un libro por su ID.
     * Retorna el status OK: 200
     * Ej.: GET http://localhost:9090/api/admin/libros/1
     */
    @GetMapping("/{id}")
    Libro obtener(@PathVariable Integer id) {
        return libroRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Crea un libro a partir del cuerpo
     * de la solicitud HTTP y retorna
     * el libro creado.
     * Retorna el status CREATED: 201
     * Ej.: POST http://localhost:9090/api/admin/libros
     */

    /*  validar el modelo de forma automática */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Libro crear( @RequestBody @Validated LibroDTO libroDTO) {
        Libro libro = new ModelMapper().map(libroDTO, Libro.class);
        return libroRepository.save(libro);
    }
    /**
     * Actualiza un libro por su ID, a partir
     * del cuerpo de la solicitud HTTP.
     * Retorna el status OK: 200.
     * Ej.: PUT http://localhost:9090/api/admin/libros/1
     */
    @PutMapping("/{id}")
    Libro actualizar(@PathVariable Integer id,
                     @RequestBody @Validated  LibroDTO libroDTO
    ) {
        Libro libro = libroRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        new ModelMapper().map(libroDTO, libro);
        libroRepository.save(libro);

        return libro;
    }

    /**
     * Elimina un libro por su ID.
     * Retorna el status NO_CONTENT: 204
     * Ej.: DELETE http://localhost:9090/api/libros/1
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void eliminar(@PathVariable Integer id) {
        Libro libro = libroRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        libroRepository.delete(libro);
    }

    /**
     * Devuelve la lista de libros de forma paginada.
     * El cliente puede enviar los parámetros page, size, sort,... en la URL
     * para configurar la página solicitada.
     * Si el cliente no envía ningún parámetro para la paginación,
     * se toma la configuración por defecto.
     * Retorna el status OK: 200
     * Ej.: GET http://localhost:9090/api/libros?page=0&size=2&sort=fechaCreacion,desc
     *
     * @param pageable la configuración de paginación que captura los parámetros como: page, size y sort
     */
    @GetMapping
    Page<Libro> index(@PageableDefault(size = 5, sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return libroRepository.findAll(pageable);
    }

}
