package br.com.jusradar.consulta.application.dto;

import br.com.jusradar.consulta.domain.Processo;
import java.util.List;

public record ConsultaResponse(
    String documento,
    String tribunal,
    int totalEncontrados,
    List<Processo> processos
) {}