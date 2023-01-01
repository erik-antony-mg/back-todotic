package pe.todotic.mitiendaapi_s3.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.todotic.mitiendaapi_s3.model.Usuario;
import pe.todotic.mitiendaapi_s3.repository.UsuarioRepository;
import pe.todotic.mitiendaapi_s3.web.dto.UsuarioDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Devuelve la lista de usuarios de forma paginada.
     * El cliente puede enviar los parámetros page, size, sort,... en la URL
     * para configurar la página solicitada.
     * Si el cliente no envía ningún parámetro para la paginación,
     * se toma la configuración por defecto.
     * Retorna el status OK: 200
     * Ej.: GET http://localhost:9090/api/usuarios?page=0&size=2&sort=nombreCompleto,desc
     *
     * @param pageable la configuración de paginación que captura los parámetros como: page, size y sort
     */
    @GetMapping
    Page<Usuario> index(@PageableDefault(sort = "nombreCompleto", size = 10) Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    /**
     * Devuelve la lista completa de usuarios
     * Retorna el status OK: 200
     * Ej.: GET http://localhost:9090/api/usuarios
     */
    @GetMapping("/listar")
    List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    /**
     * Devuelve un usuario por su ID.
     * Retorna el status OK: 200
     * Ej.: GET http://localhost:9090/api/usuarios/1
     */
    @GetMapping("/{id}")
    Usuario obtener(@PathVariable Integer id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Crea un usuario a partir del cuerpo
     * de la solicitud HTTP y retorna
     * el usuario creado.
     * Retorna el status CREATED: 201
     * Ej.: POST http://localhost:9090/api/admin/usuarios
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Usuario crear(@Validated @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuarioRepository.save(usuario);
        return usuario;
    }

    /**
     * Actualiza un usuario por su ID, a partir
     * del cuerpo de la solicitud HTTP.
     * Retorna el status OK: 200.
     * Ej.: PUT http://localhost:9090/api/usuarios/1
     */
    @PutMapping("/{id}")
    Usuario actualizar(@PathVariable Integer id,@Validated @RequestBody UsuarioDTO usuarioDTO
    ) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);

         modelMapper.map(usuarioDTO, usuario);
        usuarioRepository.save(usuario);

        return usuario;
    }

    /**
     * Elimina un usuario por su ID.
     * Retorna el status NO_CONTENT: 204
     * Ej.: DELETE http://localhost:9090/api/admin/usuarios/1
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void eliminar(@PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        usuarioRepository.delete(usuario);
    }

}
