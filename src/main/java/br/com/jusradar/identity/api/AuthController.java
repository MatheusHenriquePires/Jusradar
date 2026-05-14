package br.com.jusradar.identity.api;

import br.com.jusradar.identity.application.LoginService;
import br.com.jusradar.identity.application.RegisterService;
import br.com.jusradar.identity.application.dto.AuthResponse;
import br.com.jusradar.identity.application.dto.LoginRequest;
import br.com.jusradar.identity.application.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterService registerService;
    private final LoginService loginService;

    @PostMapping("/register")
    public void register(
        @RequestBody RegisterRequest request
    ) {
        registerService.registrar(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
        @RequestBody LoginRequest request
    ) {
        return loginService.login(request);
    }
}