package pe.todotic.mitiendaapi_s3.web.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItem {

    private String name;
    private String sku;
    private String quantity;
    @JsonProperty("unit_amount")
    private Amount unitAmount;
}
