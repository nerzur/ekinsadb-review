package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;

import java.sql.Date;
import java.util.List;

public interface PesajesLineaService {

    public List<EkPesajesLinea> listAllPesajes();
    public List<EkPesajesLinea> lisAllPesajesAfterDate(Date date);
}
