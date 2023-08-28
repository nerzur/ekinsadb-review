package cu.havanaclub.ekinsadbreview.repository;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PesajesLineaRepository extends JpaRepository <EkPesajesLinea, String>{

    public List<EkPesajesLinea> findEkPesajesLineaByFechaAfterOrderByFecha(Date date);

    public List<EkPesajesLinea> findEkPesajesLineaByTag(String tag);
}
