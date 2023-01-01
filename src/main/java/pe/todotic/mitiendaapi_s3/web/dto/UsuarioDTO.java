package pe.todotic.mitiendaapi_s3.web.dto;

import lombok.Data;
import pe.todotic.mitiendaapi_s3.model.Usuario;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class UsuarioDTO {

    @NotNull(message = "el nombre es obligatorio")
    private String nombres;
    @NotNull(message = "el nombre es obligatorio")
    private String apellidos;
    @Email(message = "formato de email equivocado")
    @NotNull(message = "el email es obligatorio")
    private String email;
    @NotNull(message = "la contraseña es obligatorio")
    private String password;
    @NotNull(message = "la roll es obligatorio")
    private Usuario.Rol rol;
}
