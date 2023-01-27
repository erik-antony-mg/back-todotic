package pe.todotic.mitiendaapi_s3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.todotic.mitiendaapi_s3.model.ItemVenta;
import pe.todotic.mitiendaapi_s3.model.Libro;
import pe.todotic.mitiendaapi_s3.model.Venta;
import pe.todotic.mitiendaapi_s3.repository.LibroRepository;
import pe.todotic.mitiendaapi_s3.repository.VentaRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VentaService {
    private LibroRepository libroRepository;
    private VentaRepository ventaRepository;

    public Venta crear(List<Integer> idLibros){
        Venta venta =new Venta();
        List<ItemVenta>items=new ArrayList<>();
        float total=0;

        for (int idLibro :idLibros){
            Libro libro= libroRepository.findById(idLibro).orElseThrow(EntityNotFoundException::new);

            ItemVenta itemVenta=new ItemVenta();
            itemVenta.setLibro(libro);
            itemVenta.setPrecio(libro.getPrecio());
            itemVenta.setNumeroDescargasDisponibles(3);
            itemVenta.setVenta(venta);

                items.add(itemVenta);
                total+=itemVenta.getPrecio();
        }
        venta.setEstado(Venta.Estado.CREADO);
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(total);
        venta.setItems(items);

        return ventaRepository.save(venta);
    }
}
