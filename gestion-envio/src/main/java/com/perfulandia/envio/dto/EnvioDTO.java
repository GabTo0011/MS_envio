package com.perfulandia.envio.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioDTO extends RepresentationModel<EnvioDTO> {
    private Integer id;
    private Integer idVenta;
    private String direccionEnvio;
    private String estadoEnvio;
    private LocalDate fechaEnvio;
    private LocalDate fechaEntrega;
}

