package cu.havanaclub.ekinsadbreview.repository;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PesajesLineaRepository extends JpaRepository <EkPesajesLinea, String>{

    public List<EkPesajesLinea> findEkPesajesLineaByFechaAfterOrderByFecha(Date date);

    public List<EkPesajesLinea> findEkPesajesLineaByTagOrderByFecha(String tag);

    @Query(value = "SELECT * FROM EK_PesajesLinea WHERE (IdZona = 1 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t2 WHERE t2.Tag = EK_PesajesLinea.Tag AND t2.IdZona = 2) OR IdZona = 2 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t3 WHERE t3.Tag = EK_PesajesLinea.Tag AND t3.IdZona = 1) OR IdZona = 3 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t4 WHERE t4.Tag = EK_PesajesLinea.Tag AND t4.IdZona = 4) OR IdZona = 4 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t5 WHERE t5.Tag = EK_PesajesLinea.Tag AND t5.IdZona = 3)) AND Fecha > ?1 AND Fecha < ?2", nativeQuery = true)
    List<EkPesajesLinea> findEkPesajesLineaWithErrorsInZoneByDate(Date startDate, Date endDate);

}
