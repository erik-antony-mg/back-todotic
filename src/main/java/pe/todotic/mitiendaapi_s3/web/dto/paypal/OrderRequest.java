package pe.todotic.mitiendaapi_s3.web.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Intent intent;
    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;
    @JsonProperty("application_context")
    private ApplicationContext applicationContext;
    public enum Intent{
        CAPTURE
    }
}
