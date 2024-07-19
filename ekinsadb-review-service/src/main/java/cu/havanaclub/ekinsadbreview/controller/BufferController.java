package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.BufferLlenado;
import cu.havanaclub.ekinsadbreview.entity.BufferVaciado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface BufferController {

    @GetMapping(value = "/bufferLlenado")
    ResponseEntity<List<BufferLlenado>> getBufferLlenado();

    @GetMapping(value = "/bufferVaciado")
    ResponseEntity<List<BufferVaciado>> getBufferVaciado();
}
