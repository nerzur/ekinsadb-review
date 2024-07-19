package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.BufferLlenado;
import cu.havanaclub.ekinsadbreview.entity.BufferVaciado;
import cu.havanaclub.ekinsadbreview.service.BufferService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${server.url.prefix}/buffer")
@PreAuthorize("hasRole('COMMON_USER_ROLE')")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class BufferControllerImpl implements BufferController{

    @Autowired
    BufferService bufferService;

    @Override
    public ResponseEntity<List<BufferLlenado>> getBufferLlenado() {
        return ResponseEntity.ok(bufferService.getBufferLlenado());
    }

    @Override
    public ResponseEntity<List<BufferVaciado>> getBufferVaciado() {
        return ResponseEntity.ok(bufferService.getBufferVaciado());
    }
}
