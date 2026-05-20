package br.com.jusradar.monitoramento.application;

import br.com.jusradar.identity.repository.UsuarioRepository;
import br.com.jusradar.monitoramento.api.dto.CriarMonitoramentoRequest;
import br.com.jusradar.monitoramento.domain.Monitoramento;
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
    private final UsuarioRepository usuarioRepository;

    public Monitoramento criar(CriarMonitoramentoRequest request, UUID advogadoId) {
        var advogado = usuarioRepository.findById(advogadoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Advogado autenticado não encontrado"));

        Monitoramento monitoramento = Monitoramento.builder()
                .numeroProcesso(request.getNumeroProcesso())
                .tribunal(request.getTribunal())
                .documentoCliente(request.getDocumentoCliente())
                .advogado(advogado)
                .criadoEm(LocalDateTime.now())
                .build();

        return monitoramentoRepository.save(monitoramento);
    }
}
