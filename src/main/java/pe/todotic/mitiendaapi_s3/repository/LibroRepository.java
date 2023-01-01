package pe.todotic.mitiendaapi_s3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.todotic.mitiendaapi_s3.model.Libro;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    List<Libro> findTop6ByOrderByFechaCreacionDesc();

    Libro findBySlug(String slug);
}
