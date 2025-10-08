package br.com.tick.tickdesck.models.reset_password.application.controller;

import br.com.tick.tickdesck.models.reset_password.application.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService service;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody Map<String, String> req) {
        service.initiatePasswordReset(req.get("email"));
        return ResponseEntity.ok("E-mail enviado.");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> req) {
        service.resetPassword(req.get("token"), req.get("newPassword"));
        return ResponseEntity.ok("Senha alterada.");
    }
}
