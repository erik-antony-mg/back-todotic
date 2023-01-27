package pe.todotic.mitiendaapi_s3.web.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseUnit {

    @JsonProperty("reference_id")
    private String referenceId;
    private Amount amount;
    private List<OrderItem> items;
}
