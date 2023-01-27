package pe.todotic.mitiendaapi_s3.web.dto;

import lombok.Data;

@Data
public class PagoPaypalResponse {
    private Boolean completado;
    private Integer idVenta;
}
