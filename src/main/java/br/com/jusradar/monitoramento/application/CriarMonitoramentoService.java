package br.com.jusradar.monitoramento.application;

import br.com.jusradar.identity.repository.UsuarioRepository;
import br.com.jusradar.monitoramento.api.dto.CriarMonitoramentoRequest;
<<<<<<< HEAD
import br.com.jusradar.monitoramento.domain.Monitoramento;
=======
import br.com.jusradar.monitoramento.domain.Cliente;
import br.com.jusradar.monitoramento.domain.Monitoramento;
import br.com.jusradar.monitoramento.infra.ClienteRepository;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
import br.com.jusradar.monitoramento.infra.MonitoramentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CriarMonitoramentoService {

    private final MonitoramentoRepository monitoramentoRepository;
<<<<<<< HEAD
=======
    private final ClienteRepository clienteRepository;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
    private final UsuarioRepository usuarioRepository;

    public Monitoramento criar(CriarMonitoramentoRequest request, UUID advogadoId) {
        var advogado = usuarioRepository.findById(advogadoId)
<<<<<<< HEAD
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Advogado autenticado não encontrado"));
=======
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Advogado autenticado nao encontrado"));

        Cliente cliente = localizarOuCriarCliente(request);
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

        Monitoramento monitoramento = Monitoramento.builder()
                .numeroProcesso(request.getNumeroProcesso())
                .tribunal(request.getTribunal())
                .documentoCliente(request.getDocumentoCliente())
<<<<<<< HEAD
=======
                .cliente(cliente)
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
                .advogado(advogado)
                .criadoEm(LocalDateTime.now())
                .build();

        return monitoramentoRepository.save(monitoramento);
    }
<<<<<<< HEAD
=======

    private Cliente localizarOuCriarCliente(CriarMonitoramentoRequest request) {
        if (request.getEmailCliente() != null && !request.getEmailCliente().isBlank()) {
            return clienteRepository.findByEmail(request.getEmailCliente())
                .map(cliente -> atualizarCliente(cliente, request))
                .orElseGet(() -> criarCliente(request));
        }

        if (request.getDocumentoCliente() != null && !request.getDocumentoCliente().isBlank()) {
            return clienteRepository.findByDocumento(request.getDocumentoCliente())
                .map(cliente -> atualizarCliente(cliente, request))
                .orElseGet(() -> criarCliente(request));
        }

        return criarCliente(request);
    }

    private Cliente atualizarCliente(Cliente cliente, CriarMonitoramentoRequest request) {
        if (request.getNomeCliente() != null && !request.getNomeCliente().isBlank()) {
            cliente.setNome(request.getNomeCliente());
        }

        if (request.getEmailCliente() != null && !request.getEmailCliente().isBlank()) {
            cliente.setEmail(request.getEmailCliente());
        }

        if (request.getDocumentoCliente() != null && !request.getDocumentoCliente().isBlank()) {
            cliente.setDocumento(request.getDocumentoCliente());
        }

        return clienteRepository.save(cliente);
    }

    private Cliente criarCliente(CriarMonitoramentoRequest request) {
        return clienteRepository.save(Cliente.builder()
            .nome(request.getNomeCliente() == null || request.getNomeCliente().isBlank()
                ? "Cliente nao informado"
                : request.getNomeCliente())
            .documento(request.getDocumentoCliente() == null || request.getDocumentoCliente().isBlank()
                ? "00000000000"
                : request.getDocumentoCliente())
            .email(request.getEmailCliente())
            .build());
    }
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
}
