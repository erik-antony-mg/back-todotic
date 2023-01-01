package pe.todotic.mitiendaapi_s3.web.dto;



import lombok.Data;
import org.apache.logging.log4j.message.Message;
import javax.validation.constraints.*;

@Data
public class LibroDTO {
    @Size(max = 100,min = 3,message ="El título debe tener {min} caracteres como mínimo y {max} caracteres como máximo" )
    @NotNull(message = "el titulo no puede ser nulo")
    private String titulo;
//    @Pattern(regexp = "[a-z0-9-]+",message = "El slug debe tener este formato [a-z 0-9]")
    @NotNull(message = "el slug es obligatorio")
    private String slug;
    @NotBlank(message ="La descripción es obligatoria")
    private String descripcion;
    @NotBlank(message = "La portada es obligatoria")
    private String rutaPortada;
    @NotBlank(message = "Falta la ruta de archivo")
    private String rutaArchivo;
    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero
    private Float precio;
}
