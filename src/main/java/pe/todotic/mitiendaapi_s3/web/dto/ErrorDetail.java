package pe.todotic.mitiendaapi_s3.web.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ErrorDetail {
    private Long timeStamp;
    private int status;
    private String title;
    private String detail;

    /**
     *  "titulo":[
     *  {"code": "NotNull","message":"El titulo es obligatorio."},
     *  {"code": "Size","message":"EL titulo debe estar entre .."}
     *  ]
     */
    private Map<String, List<ValidationError>> errors=new HashMap<>();

}
