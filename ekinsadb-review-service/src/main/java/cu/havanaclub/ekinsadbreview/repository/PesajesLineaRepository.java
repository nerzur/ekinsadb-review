package cu.havanaclub.ekinsadbreview.repository;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.util.EntriesByDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * DOM de la tabla EkPesajes Línea.
 * @author Nerzur
 */
@Repository
public interface PesajesLineaRepository extends JpaRepository<EkPesajesLinea, String> {

    /**
     * Lista todos los pesajes a partir de una fecha.
     * @param date Fecha de inicio
     * @return Devuelve todos los pesajes almacenados en el sistema a partir de una fecha.
     */
    List<EkPesajesLinea> findEkPesajesLineaByFechaAfterOrderByFecha(Date date);

    @Query(value = "select * from EK_PesajesLinea where Fecha BETWEEN ?1 AND ?2 ORDER BY Fecha", nativeQuery = true)
    List<EkPesajesLinea> findEkPesajesLineaByFechaBetweenOrderByFecha(Timestamp startDate, Timestamp endDate);

    /**
     * Devuelve todos los registros a partir de un tag ordenados por fecha.
     * @param tag Tag a buscar.
     * @return Devuelve una lista con los registros asociados a un tag.
     */
    List<EkPesajesLinea> findEkPesajesLineaByTagOrderByFecha(String tag);

    /**
     * Devuelve una lista de los registros que no tienen una entrada o salida homóloga en las líneas de llenado o vaciado
     * en un rango de fechas.
     * @param startDate Fecha de inicio de la búsqueda.
     * @param endDate Fecha de fin de la búsqueda
     * @return Devuelve una lista de los registros que no tienen una entrada o salida.
     */
    @Query(value = "SELECT * FROM EK_PesajesLinea WHERE (IdZona = 1 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t2 WHERE t2.Tag = EK_PesajesLinea.Tag AND t2.IdZona = 2) OR IdZona = 2 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t3 WHERE t3.Tag = EK_PesajesLinea.Tag AND t3.IdZona = 1) OR IdZona = 3 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t4 WHERE t4.Tag = EK_PesajesLinea.Tag AND t4.IdZona = 4) OR IdZona = 4 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t5 WHERE t5.Tag = EK_PesajesLinea.Tag AND t5.IdZona = 3)) AND Fecha > ?1 AND Fecha < ?2", nativeQuery = true)
    List<EkPesajesLinea> findEkPesajesLineaWithErrorsInZoneByDate(Date startDate, Date endDate);

    /**
     * Devuelve la cantidad de pesajes que se han realizado en el día de hoy.
     * @return Cantidad de pesajes que se han realizado en el día de hoy.
     */
    @Query(value = "SELECT COUNT(*) FROM EK_PesajesLinea WHERE CAST(Fecha AS DATE) = CAST(GETDATE() AS DATE)", nativeQuery = true)
    Integer countEkPesajesLineaToday();

    /**
     * Devuelve la cantidad de lotes que han sido procesados en un rango de fechas.
     * @param startDate Fecha de inicio.
     * @param endDate Fecha de fin.
     * @return Devuelve cantidad de lotes que han sido procesados en un rango de fechas.
     */
    @Query(value = "SELECT COUNT(DISTINCT Numero_Lote) AS Cantidad FROM EK_PesajesLinea WHERE Fecha >= ?1 and Fecha <= ?2", nativeQuery = true)
    Integer countLotesByDates(Date startDate, Date endDate);

    /**
     * Devuelve la cantidad de Tags que tienen errores de registros (carecen de un registro homólogo de entrada o salida en las líneas).
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Cantidad de registros con errores.
     */
    @Query(value = "SELECT COUNT(DISTINCT Tag) FROM EK_PesajesLinea WHERE (IdZona = 1 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t2 WHERE t2.Tag = EK_PesajesLinea.Tag AND t2.IdZona = 2) OR IdZona = 2 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t3 WHERE t3.Tag = EK_PesajesLinea.Tag AND t3.IdZona = 1) OR IdZona = 3 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t4 WHERE t4.Tag = EK_PesajesLinea.Tag AND t4.IdZona = 4) OR IdZona = 4 AND NOT EXISTS (SELECT * FROM EK_PesajesLinea AS t5 WHERE t5.Tag = EK_PesajesLinea.Tag AND t5.IdZona = 3)) AND Fecha > ?1 AND Fecha < ?2", nativeQuery = true)
    Integer countErrorsTagByDates(Date startDate, Date endDate);

    /**
     * Devuelve los lotes que han sido procesados en un rango de fechas.
     * @param startDate Fecha de inicio.
     * @param endDate Fecha de fin.
     * @return Devuelve los lotes que han sido procesados en un rango de fechas.
     */
    @Query(value = "SELECT DISTINCT Numero_Lote FROM EK_PesajesLinea WHERE Fecha >= ?1 and Fecha <=?2", nativeQuery = true)
    List<String> findDistinctNumeroLoteByDate(Date startDate, Date endDate);

    /**
     * Devuelve la cantidad total de pallets procesados por mes y año.
     * @return Cantidades de pallets procesados por mes y año.
     */
    @Query("SELECT new cu.havanaclub.ekinsadbreview.util.EntriesByDate(YEAR(p.fecha), MONTH(p.fecha), COUNT(*)) AS registries FROM EkPesajesLinea p GROUP BY YEAR(p.fecha), MONTH(p.fecha) ORDER BY  YEAR(p.fecha), MONTH(p.fecha)")
    List<EntriesByDate> countEntriesByDates();

    /**
     * Devuelve la cantidad total de pallets procesados por mes y año teniendo en cuenta la zona. Si se desea buscar en la línea de vaciado utilizar las zonas 1 y 2, en el caso de llenado 3 y 4.
     * @param zone1 Zona 1 a buscar.
     * @param zone2 Zona 2 a buscar
     * @return Cantidades de pallets procesados por mes y año.
     */
    @Query("SELECT new cu.havanaclub.ekinsadbreview.util.EntriesByDate(YEAR(p.fecha), MONTH(p.fecha), COUNT(*)) AS registries FROM EkPesajesLinea p where p.idZona = ?1 or p.idZona = ?2  GROUP BY YEAR(p.fecha), MONTH(p.fecha) ORDER BY YEAR(p.fecha) DESC , MONTH(p.fecha) DESC limit 12")
    List<EntriesByDate> countEntriesVaciadoOrLLenadoByDates(int zone1, int zone2);

    List<EkPesajesLinea> findByTagAndNumeroLote(String tag, String numeroLote);

//    @Modifying
//    @Query(value = "update EkPesajesLinea set Numero_Lote = ?3 where Tag like ?1 and Numero_lote like ?2 and (IdZona = 1 or IdZona = 2)", nativeQuery = true)
//    void updateLote(String tag, String prevLote, String newLote);

    @Query(value = "select distinct Tag from EK_PesajesLinea where Numero_Lote like ?1 and (IdZona = ?2 or IdZona = ?3)", nativeQuery = true)
    List<String> findDistinctTagByNumeroLote(String numeroLote, int zone1, int zone2);
}
