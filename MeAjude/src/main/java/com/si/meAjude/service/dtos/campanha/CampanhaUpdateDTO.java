package com.si.meAjude.service.dtos.campanha;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaUpdateDTO (
    Long id,
    String titulo,
    BigDecimal meta,
    boolean ativa,
    String descricao,
    LocalDateTime dataInicio,
    LocalDateTime dataFinal,
    Long criadorId
){}