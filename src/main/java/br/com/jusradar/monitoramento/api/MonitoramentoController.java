package br.com.jusradar.monitoramento.api;

import br.com.jusradar.monitoramento.api.dto.CriarMonitoramentoRequest;
<<<<<<< HEAD
import br.com.jusradar.monitoramento.application.CriarMonitoramentoService;
import br.com.jusradar.monitoramento.application.MonitoramentoService;
=======
import br.com.jusradar.monitoramento.api.dto.SimularMovimentacaoRequest;
import br.com.jusradar.monitoramento.application.CriarMonitoramentoService;
import br.com.jusradar.monitoramento.application.MonitoramentoService;
import br.com.jusradar.monitoramento.application.SimularMovimentacaoService;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.notificacao.application.NotificacaoService;
import br.com.jusradar.shared.security.AuthUtils;
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
<<<<<<< HEAD
=======
    private final SimularMovimentacaoService simularService;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
    private final NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<Monitoramento> criar(@RequestBody CriarMonitoramentoRequest request) {
        return ResponseEntity.status(201).body(criarService.criar(request, AuthUtils.getUsuarioIdLogado()));
    }

    @GetMapping
    public List<Monitoramento> listarTodos() {
        return service.buscarPorAdvogado(AuthUtils.getUsuarioIdLogado());
    }

    @GetMapping("/advogado/{advogadoId}")
<<<<<<< HEAD
    public List<Monitoramento> listarPorAdvogado(@PathVariable UUID advogadoId) {
=======
    public List<Monitoramento> listarPorAdvogado(@PathVariable("advogadoId") UUID advogadoId) {
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
        return service.buscarPorAdvogado(advogadoId);
    }

    @PostMapping("/testar-notificacao/{id}")
<<<<<<< HEAD
    public ResponseEntity<String> testarNotificacao(@PathVariable UUID id) {
=======
    public ResponseEntity<String> testarNotificacao(@PathVariable("id") UUID id) {
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
        var monitoramento = service.buscarDoAdvogado(id, AuthUtils.getUsuarioIdLogado());
        notificacaoService.notificarMovimentacao(monitoramento);
        return ResponseEntity.ok("Email enviado com sucesso!");
    }
<<<<<<< HEAD
=======

    @PostMapping("/simular")
    public ResponseEntity<Monitoramento> simularMovimentacao(@RequestBody SimularMovimentacaoRequest request) {
        return ResponseEntity.ok(simularService.simular(request, AuthUtils.getUsuarioIdLogado()));
    }
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
}
