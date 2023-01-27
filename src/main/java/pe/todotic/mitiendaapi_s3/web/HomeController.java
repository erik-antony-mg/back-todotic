package pe.todotic.mitiendaapi_s3.web;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pe.todotic.mitiendaapi_s3.model.ItemVenta;
import pe.todotic.mitiendaapi_s3.model.Libro;
import pe.todotic.mitiendaapi_s3.model.Venta;
import pe.todotic.mitiendaapi_s3.repository.ItemVentaRepository;
import pe.todotic.mitiendaapi_s3.repository.LibroRepository;
import pe.todotic.mitiendaapi_s3.repository.VentaRepository;
import pe.todotic.mitiendaapi_s3.service.PaypalService;
import pe.todotic.mitiendaapi_s3.service.StorageService;
import pe.todotic.mitiendaapi_s3.service.VentaService;
import pe.todotic.mitiendaapi_s3.web.dto.OrdenPaypalResponse;
import pe.todotic.mitiendaapi_s3.web.dto.PagoPaypalResponse;
import pe.todotic.mitiendaapi_s3.web.dto.paypal.OrderCaptureResponse;
import pe.todotic.mitiendaapi_s3.web.dto.paypal.OrderResponse;
import javax.ws.rs.BadRequestException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequestMapping("/api")
@RestController
@AllArgsConstructor
public class HomeController {


    private LibroRepository libroRepository;
    private VentaRepository ventaRepository;
    private PaypalService paypalService;
    private VentaService ventaService;
    private ItemVentaRepository itemVentaRepository;
    private StorageService storageService;

    @GetMapping("/lastLibros")
    List<Libro> lastLibros(){
//    String accesToken=paypalService.getAccessToken();
//        System.out.println("access Token:"+accesToken);

//      String approveUrl=  paypalService.createOrder(ventaRepository.findById(2).get(),"http://localhost:4200")
//              .getLinks()
//              .stream()
//              .filter(link -> link.getRel().equals("approve"))
//              .findFirst()
//              .orElseThrow(RuntimeException::new)
//              .getHref();
//        System.out.println("approveUrl :"+ approveUrl);

    return libroRepository.findTop6ByOrderByFechaCreacionDesc();
    }

    @GetMapping("/losDiezTitulo")
    Page<Libro> losDiezTitulo(@PageableDefault(sort = "titulo") Pageable page){
        return libroRepository.findAll(page);
    }

    @GetMapping("/libros/{slug}")
    Libro buscarPorSlug(@PathVariable String slug){
        return libroRepository.findBySlug(slug);
    }

    @GetMapping("/ventas/{id}")
    Venta buscarVentaPorId(@PathVariable Integer id){
        return ventaRepository.findOneById(id);
    }

    @PostMapping("/pago/paypal/crear")
    OrdenPaypalResponse CrearPagoPaypal(@RequestBody List<Integer> idLibros, @RequestParam String urlRetorno){
        Venta venta=ventaService.crear(idLibros);
        OrderResponse orderResponse=paypalService.createOrder(venta,urlRetorno);

        String approveUrl=  paypalService.createOrder(venta,urlRetorno)
              .getLinks()
              .stream()
              .filter(link -> link.getRel().equals("approve"))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .getHref();

        OrdenPaypalResponse ordenPaypalResponse=new OrdenPaypalResponse();
        ordenPaypalResponse.setApproveUrl(approveUrl);
        return ordenPaypalResponse ;
    }

    @PostMapping("/pago/paypal/capturar")
    PagoPaypalResponse capturarPagoPaypal(@RequestParam String token){
        OrderCaptureResponse orderCaptureResponse=paypalService.captureOrden(token);

        boolean completado=orderCaptureResponse.getStatus().equals("COMPLETED");
        Integer idVenta=null;

        if (completado){
            idVenta=Integer.parseInt(orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId());

            Venta venta=ventaRepository.findById(idVenta).orElseThrow(RuntimeException::new);
            venta.setEstado(Venta.Estado.COMPLETADO);

            ventaRepository.save(venta);
        }

        PagoPaypalResponse pagoPaypalResponse=new PagoPaypalResponse();
        pagoPaypalResponse.setCompletado(completado);
        pagoPaypalResponse.setIdVenta(idVenta);

        return pagoPaypalResponse;
    }

    @GetMapping("/ventas/{idVenta}/items/{idItem}/archivo/descargar")
    Resource descargarArchivoItemVenta(@PathVariable Integer idVenta,@PathVariable Integer idItem){
        ItemVenta itemVenta=itemVentaRepository.findOneByVentaIdAndId(idVenta,idItem).orElseThrow(EntityNotFoundException::new);

        if (itemVenta.getNumeroDescargasDisponibles()>0){
                itemVenta.setNumeroDescargasDisponibles(itemVenta.getNumeroDescargasDisponibles()-1);
                itemVentaRepository.save(itemVenta);

        }else {
            throw new BadRequestException("Ya no existen mas descargas disponibles para este item");
        }

        return storageService.loadAsResource(itemVenta.getLibro().getRutaArchivo());
    }
}
