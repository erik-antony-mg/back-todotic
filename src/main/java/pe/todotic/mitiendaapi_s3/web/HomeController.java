package pe.todotic.mitiendaapi_s3.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.todotic.mitiendaapi_s3.model.Libro;
import pe.todotic.mitiendaapi_s3.model.Venta;
import pe.todotic.mitiendaapi_s3.repository.LibroRepository;
import pe.todotic.mitiendaapi_s3.repository.VentaRepository;

import java.util.List;

@RequestMapping("/api")
@RestController
public class HomeController {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping("/lastLibros")
    List<Libro> lastLibros(){
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

}
