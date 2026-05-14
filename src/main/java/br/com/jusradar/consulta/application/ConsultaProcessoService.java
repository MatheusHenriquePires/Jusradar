package br.com.jusradar.consulta.application;

import br.com.jusradar.consulta.application.dto.ConsultaResponse;
import br.com.jusradar.consulta.infra.datajud.DataJudClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaProcessoService {

    private final DataJudClient datajudClient;

    public ConsultaResponse consultar(String documento, String tribunal) {
        var processos = datajudClient.buscar(documento, tribunal);
        return new ConsultaResponse(documento, tribunal, processos.size(), processos);
    }
}