package pe.todotic.mitiendaapi_s3.web.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApplicationContext {
    @JsonProperty("return_url")
    private String returnUrl;
    @JsonProperty("cancel_url")
    private String cancelUrl;
    @JsonProperty("brand_name")
    private String brandName;

}
