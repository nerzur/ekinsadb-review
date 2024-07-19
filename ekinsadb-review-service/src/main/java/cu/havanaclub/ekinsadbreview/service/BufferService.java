package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.BufferLlenado;
import cu.havanaclub.ekinsadbreview.entity.BufferVaciado;

import java.util.List;

public interface BufferService {

    List<BufferLlenado> getBufferLlenado();
    List<BufferVaciado> getBufferVaciado();
}
