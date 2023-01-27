package pe.todotic.mitiendaapi_s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.todotic.mitiendaapi_s3.model.ItemVenta;

import java.util.Optional;

@Repository
public interface ItemVentaRepository extends JpaRepository<ItemVenta, Integer> {

    Optional<ItemVenta> findOneByVentaIdAndId(Integer idVenta,Integer id);
}
