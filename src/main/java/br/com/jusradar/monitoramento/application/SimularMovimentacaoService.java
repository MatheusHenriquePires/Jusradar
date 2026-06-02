package br.com.jusradar.monitoramento.application;

import br.com.jusradar.identity.repository.UsuarioRepository;
import br.com.jusradar.monitoramento.api.dto.SimularMovimentacaoRequest;
import br.com.jusradar.monitoramento.domain.Cliente;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.monitoramento.infra.ClienteRepository;
import br.com.jusradar.monitoramento.infra.MonitoramentoRepository;
import br.com.jusradar.notificacao.application.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimularMovimentacaoService {

    private final MonitoramentoRepository monitoramentoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public Monitoramento simular(SimularMovimentacaoRequest request, UUID advogadoId) {
        var advogado = usuarioRepository.findById(advogadoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Advogado autenticado não encontrado"));

        Cliente cliente = localizarOuCriarCliente(request);

        Monitoramento monitoramento = monitoramentoRepository
            .findFirstByNumeroProcessoAndAdvogadoId(request.numeroProcesso(), advogadoId)
            .orElseGet(Monitoramento::new);

        monitoramento.setNumeroProcesso(request.numeroProcesso());
        monitoramento.setTribunal(request.tribunal());
        monitoramento.setDocumentoCliente(request.documentoCliente());
        monitoramento.setCliente(cliente);
        monitoramento.setAdvogado(advogado);
        monitoramento.setUltimaMovimentacao(
            request.movimentacao() == null || request.movimentacao().isBlank()
                ? "Movimentacao simulada para teste"
                : request.movimentacao()
        );
        monitoramento.setUltimaConsulta(LocalDateTime.now());
        if (monitoramento.getCriadoEm() == null) {
            monitoramento.setCriadoEm(LocalDateTime.now());
        }

        Monitoramento salvo = monitoramentoRepository.save(monitoramento);
        notificacaoService.notificarMovimentacao(salvo);
        return salvo;
    }

    private Cliente localizarOuCriarCliente(SimularMovimentacaoRequest request) {
        if (request.clienteEmail() != null && !request.clienteEmail().isBlank()) {
            return clienteRepository.findByEmail(request.clienteEmail())
                .map(cliente -> atualizarCliente(cliente, request))
                .orElseGet(() -> salvarClienteNovo(request));
        }

        if (request.documentoCliente() != null && !request.documentoCliente().isBlank()) {
            return clienteRepository.findByDocumento(request.documentoCliente())
                .map(cliente -> atualizarCliente(cliente, request))
                .orElseGet(() -> salvarClienteNovo(request));
        }

        return salvarClienteNovo(request);
    }

    private Cliente atualizarCliente(Cliente cliente, SimularMovimentacaoRequest request) {
        if (request.clienteNome() != null && !request.clienteNome().isBlank()) {
            cliente.setNome(request.clienteNome());
        }

        if (request.clienteEmail() != null && !request.clienteEmail().isBlank()) {
            cliente.setEmail(request.clienteEmail());
        }

        if (request.documentoCliente() != null && !request.documentoCliente().isBlank()) {
            cliente.setDocumento(request.documentoCliente());
        }

        return clienteRepository.save(cliente);
    }

    private Cliente salvarClienteNovo(SimularMovimentacaoRequest request) {
        Cliente cliente = Cliente.builder()
            .nome(request.clienteNome() == null || request.clienteNome().isBlank() ? "Cliente Teste" : request.clienteNome())
            .documento(request.documentoCliente() == null || request.documentoCliente().isBlank() ? "00000000000" : request.documentoCliente())
            .email(request.clienteEmail())
            .build();

        return clienteRepository.save(cliente);
    }
}
