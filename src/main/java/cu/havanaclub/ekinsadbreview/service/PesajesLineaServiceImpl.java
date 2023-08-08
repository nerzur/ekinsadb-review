package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.repository.PesajesLineaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PesajesLineaServiceImpl implements PesajesLineaService {

    private final PesajesLineaRepository pesajesLineaRepository;

    @Override
    public List<EkPesajesLinea> listAllPesajes() {
        return pesajesLineaRepository.findAll();
    }

    @Override
    public List<EkPesajesLinea> lisAllPesajesAfterDate(Date date) {
        return pesajesLineaRepository.findEkPesajesLineaByFechaAfterOrderByFecha(date);
    }
}
