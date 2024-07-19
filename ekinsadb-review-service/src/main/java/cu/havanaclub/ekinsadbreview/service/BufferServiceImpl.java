package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.BufferLlenado;
import cu.havanaclub.ekinsadbreview.entity.BufferVaciado;
import cu.havanaclub.ekinsadbreview.repository.BufferLlenadoRepository;
import cu.havanaclub.ekinsadbreview.repository.BufferVaciadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BufferServiceImpl implements BufferService{

    @Autowired
    BufferLlenadoRepository bufferLlenadoRepository;

    @Autowired
    BufferVaciadoRepository bufferVaciadoRepository;

    @Override
    public List<BufferLlenado> getBufferLlenado() {
        return bufferLlenadoRepository.findAll();
    }

    @Override
    public List<BufferVaciado> getBufferVaciado() {
        return bufferVaciadoRepository.findAll();
    }
}
