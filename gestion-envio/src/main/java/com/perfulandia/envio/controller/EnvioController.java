package com.perfulandia.envio.controller;

import com.perfulandia.envio.dto.EnvioDTO;
import com.perfulandia.envio.service.EnvioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

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
    public ResponseEntity<CollectionModel<EnvioDTO>> listar() {
        List<EnvioDTO> lista = envioService.listar();
        for (EnvioDTO dto : lista) {
            // Agregar enlaces HATEOAS a cada DTO en la lista
            dto.add(linkTo(methodOn(EnvioController.class).obtenerHateoas(dto.getId())).withSelfRel());
        }
        // Usar CollectionModel para envolver la lista con los enlaces
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(EnvioController.class).listar()).withSelfRel()));
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
    public ResponseEntity<EntityModel<EnvioDTO>> obtenerHateoas(@PathVariable Integer id) {
        EnvioDTO dto = envioService.obtenerPorId(id);
        // Usar EntityModel para agregar enlaces a un solo objeto
        EntityModel<EnvioDTO> entityModel = EntityModel.of(dto,
                linkTo(methodOn(EnvioController.class).obtenerHateoas(id)).withSelfRel(),
                linkTo(methodOn(EnvioController.class).listar()).withRel("todos"),
                linkTo(methodOn(EnvioController.class).eliminar(id)).withRel("eliminar"));
        return ResponseEntity.ok(entityModel);
    }

    // HATEOAS: listar todos con enlaces
    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EnvioDTO>> listarHateoas() {
        List<EnvioDTO> lista = envioService.listar();
        for (EnvioDTO dto : lista) {
            dto.add(linkTo(methodOn(EnvioController.class).obtenerHateoas(dto.getId())).withSelfRel());
        }
        // Usar CollectionModel para envolver la lista con los enlaces
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(EnvioController.class).listar()).withSelfRel()));
    }
}
