package br.com.jusradar.monitoramento.api;

import br.com.jusradar.monitoramento.application.MonitoramentoService;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/monitoramentos")
@RequiredArgsConstructor
public class MonitoramentoController {

    private final MonitoramentoService service;

    @PostMapping
    public Monitoramento criar(@RequestBody Monitoramento m) {
        return service.salvar(m);
    }

    @GetMapping("/{advogadoId}")
    public List<Monitoramento> listar(@PathVariable UUID advogadoId) {
        return service.buscarPorAdvogado(advogadoId);
    }
}
