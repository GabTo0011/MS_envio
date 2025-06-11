package com.perfulandia.envio.service;

import com.perfulandia.envio.dto.EnvioDTO;
import com.perfulandia.envio.model.Envio;
import com.perfulandia.envio.model.Venta;
import com.perfulandia.envio.repository.EnvioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private EnvioDTO toDto(Envio envio) {
        return new EnvioDTO(
                envio.getId(),
                envio.getVenta().getId(),
                envio.getDireccionEnvio(),
                envio.getEstadoEnvio(),
                envio.getFechaEnvio(),
                envio.getFechaEntrega()
        );
    }

    private Envio toEntity(EnvioDTO dto) {
        Venta ventaRef = entityManager.getReference(Venta.class, dto.getIdVenta());

        Envio envio = new Envio();
        envio.setId(dto.getId());
        envio.setVenta(ventaRef);
        envio.setDireccionEnvio(dto.getDireccionEnvio());
        envio.setEstadoEnvio(dto.getEstadoEnvio());
        envio.setFechaEnvio(dto.getFechaEnvio());
        envio.setFechaEntrega(dto.getFechaEntrega());

        return envio;
    }

    public EnvioDTO crear(EnvioDTO dto) {
        Envio envio = toEntity(dto);
        return toDto(envioRepository.save(envio));
    }

    public List<EnvioDTO> listar() {
        return envioRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EnvioDTO obtenerPorId(Integer id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        return toDto(envio);
    }

    public EnvioDTO actualizar(Integer id, EnvioDTO dto) {
        Envio existente = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        Venta ventaRef = entityManager.getReference(Venta.class, dto.getIdVenta());

        existente.setVenta(ventaRef);
        existente.setDireccionEnvio(dto.getDireccionEnvio());
        existente.setEstadoEnvio(dto.getEstadoEnvio());
        existente.setFechaEnvio(dto.getFechaEnvio());
        existente.setFechaEntrega(dto.getFechaEntrega());

        return toDto(envioRepository.save(existente));
    }

    public void eliminar(Integer id) {
        if (!envioRepository.existsById(id)) {
            throw new RuntimeException("Envío no encontrado");
        }
        envioRepository.deleteById(id);
    }
}
