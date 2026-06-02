package br.com.jusradar.consulta.infra;

import br.com.jusradar.consulta.application.ConsultaProcessoService;
import br.com.jusradar.consulta.application.dto.ConsultaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processos")
@RequiredArgsConstructor
public class ProcessoController {

    private final ConsultaProcessoService service;

    @GetMapping
    public ConsultaResponse consultar(
<<<<<<< HEAD
        @RequestParam String documento,
        @RequestParam(defaultValue = "TJPI") String tribunal
    ) {
        return service.consultar(documento, tribunal);
    }
}
=======
        @RequestParam(name = "documento") String documento,
        @RequestParam(name = "tribunal", defaultValue = "TJPI") String tribunal
    ) {
        return service.consultar(documento, tribunal);
    }
}
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
