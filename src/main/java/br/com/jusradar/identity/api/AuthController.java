package br.com.jusradar.identity.api;

import br.com.jusradar.identity.application.LoginService;
<<<<<<< HEAD
import br.com.jusradar.identity.application.RegisterService;
import br.com.jusradar.identity.application.dto.AuthResponse;
import br.com.jusradar.identity.application.dto.LoginRequest;
=======
import br.com.jusradar.identity.application.PasswordRecoveryService;
import br.com.jusradar.identity.application.RegisterService;
import br.com.jusradar.identity.application.dto.AuthResponse;
import br.com.jusradar.identity.application.dto.ForgotPasswordRequest;
import br.com.jusradar.identity.application.dto.LoginRequest;
import br.com.jusradar.identity.application.dto.ResetPasswordRequest;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
import br.com.jusradar.identity.application.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterService registerService;
    private final LoginService loginService;
<<<<<<< HEAD
=======
    private final PasswordRecoveryService passwordRecoveryService;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

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
<<<<<<< HEAD
}
=======

    @PostMapping("/forgot-password")
    public void forgotPassword(
        @RequestBody ForgotPasswordRequest request
    ) {
        passwordRecoveryService.solicitarRecuperacao(request.email());
    }

    @PostMapping("/reset-password")
    public void resetPassword(
        @RequestBody ResetPasswordRequest request
    ) {
        passwordRecoveryService.redefinirSenha(request.token(), request.senha());
    }
}
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
