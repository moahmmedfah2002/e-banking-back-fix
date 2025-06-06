package net.hamza.banque.service;

import lombok.RequiredArgsConstructor;

import net.hamza.banque.model.Utilisateur;
import net.hamza.banque.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepository;
    public Optional<Utilisateur> findUserByEmail(String email) {
        return userRepository.findByEmail(email);

    }

}
