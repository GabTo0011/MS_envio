package com.perfulandia.envio.controller;

import com.perfulandia.envio.dto.EnvioDTO;
import com.perfulandia.envio.service.EnvioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/envio")
@Tag(name = "Envíos", description = "Operaciones para gestión de envíos")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    public ResponseEntity<EnvioDTO> crear(@RequestBody EnvioDTO dto) {
        return ResponseEntity.ok(envioService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<EnvioDTO>> listar() {
        return ResponseEntity.ok(envioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnvioDTO> actualizar(@PathVariable Integer id, @RequestBody EnvioDTO dto) {
        return ResponseEntity.ok(envioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // HATEOAS: obtener por id con enlaces
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EnvioDTO> obtenerHateoas(@PathVariable Integer id) {
        EnvioDTO dto = envioService.obtenerPorId(id);
        dto.add(linkTo(methodOn(EnvioController.class).obtenerHateoas(id)).withSelfRel());
        dto.add(linkTo(methodOn(EnvioController.class).listar()).withRel("todos"));
        dto.add(linkTo(methodOn(EnvioController.class).eliminar(id)).withRel("eliminar"));
        return ResponseEntity.ok(dto);
    }

    // HATEOAS: listar todos con enlaces
    @GetMapping("/hateoas")
    public ResponseEntity<List<EnvioDTO>> listarHateoas() {
        List<EnvioDTO> lista = envioService.listar();
        for (EnvioDTO dto : lista) {
            dto.add(linkTo(methodOn(EnvioController.class).obtenerHateoas(dto.getId())).withSelfRel());
        }
        return ResponseEntity.ok(lista);
    }
}

