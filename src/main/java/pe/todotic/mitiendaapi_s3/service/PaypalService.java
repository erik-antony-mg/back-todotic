package pe.todotic.mitiendaapi_s3.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pe.todotic.mitiendaapi_s3.model.Libro;
import pe.todotic.mitiendaapi_s3.model.Venta;
import pe.todotic.mitiendaapi_s3.repository.VentaRepository;
import pe.todotic.mitiendaapi_s3.web.dto.paypal.*;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class PaypalService {
    private final static  String PAYPAL_API_BASE="https://api-m.sandbox.paypal.com";
    private final static  String PAYPAL_CLIENT_ID="AYElGjYfRezxu-jSEe_VKzGgkUZeI6jrDZUcr3qvPUvjj6yyXNS1H15-7VDxbtjSiXeosQPyRHT9r0Nq";
    private final static  String PAYPAL_CLIENT_SECRET="ED53uRtdvcQKnO98LSqlILmP4i_DA4vUG7hPfEZAxggSZhUhHwd9LrcaRGuaSl9hnjEMRF9y0iHelezs";

    public String getAccessToken(){
        String url=String.format("%s/v1/oauth2/token",PAYPAL_API_BASE);
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(PAYPAL_CLIENT_ID,PAYPAL_CLIENT_SECRET);

        MultiValueMap<String,String> form=new LinkedMultiValueMap<>();
        form.add("grant_type","client_credentials");

        HttpEntity<MultiValueMap<String,String>> entity=new HttpEntity<>(form,headers);
        ResponseEntity<Token>response = restTemplate.postForEntity(url,entity, Token.class);

        return response.getBody().getAccessToken();
    }

    public OrderResponse createOrder(Venta venta,String returnUrl){
        String url=String.format("%s/v2/checkout/orders",PAYPAL_API_BASE);

        OrderRequest orderRequest=new OrderRequest();
        orderRequest.setIntent(OrderRequest.Intent.CAPTURE);

        //detalles de la orden
        ApplicationContext applicationContext=new ApplicationContext();
        applicationContext.setBrandName("Libreria Muñico");
        applicationContext.setReturnUrl(returnUrl);
        applicationContext.setCancelUrl(returnUrl);

        orderRequest.setApplicationContext(applicationContext);

        //crear un purchaseUnit de la venta
        PurchaseUnit purchaseUnit=new PurchaseUnit();
        purchaseUnit.setReferenceId(venta.getId().toString());

        //crear un Amount de la venta
        Amount purchaseAmount=new Amount();
        purchaseAmount.setCurrencyCode(Amount.CurrencyCode.USD);
        purchaseAmount.setValue(venta.getTotal().toString());


        Amount itemsAmount=new Amount();
        itemsAmount.setCurrencyCode(Amount.CurrencyCode.USD);
        itemsAmount.setValue(venta.getTotal().toString());
        purchaseAmount.setBreakdown(new Amount.Breakdown(itemsAmount));

        purchaseUnit.setAmount(purchaseAmount);
        purchaseUnit.setItems(new ArrayList<>());

        //agregando items al purcharseUnit
        venta.getItems().forEach(itemVenta -> {
            Libro libro=itemVenta.getLibro();

            OrderItem orderItem=new OrderItem();
            orderItem.setName(libro.getTitulo());
            orderItem.setSku(libro.getSlug());
            orderItem.setQuantity("1");

            Amount unitAmount=new Amount();
            unitAmount.setCurrencyCode(Amount.CurrencyCode.USD);
            unitAmount.setValue(itemVenta.getPrecio().toString());


            orderItem.setUnitAmount(unitAmount);
            purchaseUnit.getItems().add(orderItem);

        });

        orderRequest.setPurchaseUnits(Collections.singletonList(purchaseUnit));

        //crear solicutd http
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<OrderRequest> entity=new HttpEntity<>(orderRequest,headers);
        ResponseEntity<OrderResponse>response = restTemplate.postForEntity(url,entity, OrderResponse.class);
        return response.getBody();
    }


    public  OrderCaptureResponse captureOrden(String orderId){
        String url=String.format("%s/v2/checkout/orders/%s/capture",PAYPAL_API_BASE,orderId);

        RestTemplate restTemplate=new RestTemplate();

        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String,String>> entity=new HttpEntity<>(null,headers);
        ResponseEntity<OrderCaptureResponse>response = restTemplate.postForEntity(url,entity, OrderCaptureResponse.class);

        return response.getBody();
    }
}
