package pe.todotic.mitiendaapi_s3.web.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class Amount {
    @JsonProperty("currency_code")
    private CurrencyCode currencyCode;
    private String value;
    private Breakdown breakdown;


    public enum CurrencyCode{
        USD
    }

    @Data
    @AllArgsConstructor
    public static class Breakdown{
        @JsonProperty("item_total")
        private Amount itemTotal;
    }
}
