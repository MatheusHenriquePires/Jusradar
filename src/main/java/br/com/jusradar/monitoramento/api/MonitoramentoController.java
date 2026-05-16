package br.com.jusradar.monitoramento.api;

import br.com.jusradar.monitoramento.api.dto.CriarMonitoramentoRequest;
import br.com.jusradar.monitoramento.application.CriarMonitoramentoService;
import br.com.jusradar.monitoramento.application.MonitoramentoService;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.notificacao.application.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/monitoramentos")
@RequiredArgsConstructor
public class MonitoramentoController {

    private final MonitoramentoService service;
    private final CriarMonitoramentoService criarService;
    private final NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<Monitoramento> criar(@RequestBody CriarMonitoramentoRequest request) {
        return ResponseEntity.status(201).body(criarService.criar(request));
    }

    @GetMapping
    public List<Monitoramento> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/advogado/{advogadoId}")
    public List<Monitoramento> listarPorAdvogado(@PathVariable UUID advogadoId) {
        return service.buscarPorAdvogado(advogadoId);
    }

    @PostMapping("/testar-notificacao/{id}")
    public ResponseEntity<String> testarNotificacao(@PathVariable UUID id) {
        var m = service.listarTodos().stream()
            .filter(mon -> mon.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Monitoramento não encontrado"));

        notificacaoService.notificarMovimentacao(m);
        return ResponseEntity.ok("Email enviado com sucesso!");
    }
}