package br.com.tick.tickdesck.models.password.application;

import br.com.tick.tickdesck.models.password.domain.ResetPasswordEntity;
import br.com.tick.tickdesck.models.password.repository.ResetPasswordRepository;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private EmailFromResetPasswordService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public void initiatePasswordReset(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Remove tokens existentes para este usuário
        resetPasswordRepository.deleteByUser(user);

        // Gera um novo token único
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (resetPasswordRepository.findByToken(token).isPresent());

        // Cria e salva o novo token

        ResetPasswordEntity resetToken = new ResetPasswordEntity();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        resetPasswordRepository.save(resetToken);
        emailService.sendResetPasswordEmail(email, token);
    }

    public void resetPassword(String token, String newPassword) {
        ResetPasswordEntity resetToken = resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Remove o token usado
        resetPasswordRepository.delete(resetToken);
    }


}
