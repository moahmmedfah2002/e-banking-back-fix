package net.hamza.banque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.hamza.banque.model.Utilisateur;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse  {
    private String token;
    private Utilisateur user;
    private int otp;
    private Boolean valid;
}

