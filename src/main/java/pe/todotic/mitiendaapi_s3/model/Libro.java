package pe.todotic.mitiendaapi_s3.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Entity
public class Libro {
    @Id
    @Column(name = "idlibro")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String titulo;
    private String slug;
    private String descripcion;
    private String rutaPortada;
    private String rutaArchivo;
    private Float precio;

    private LocalDateTime fechaCreacion;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    private void asignarFechaCreacion() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    private void asignarFechaActualizacion() {
        fechaActualizacion = LocalDateTime.now();
    }
}
