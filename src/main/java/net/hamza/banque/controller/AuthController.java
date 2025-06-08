package net.hamza.banque.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import net.hamza.banque.dto.AuthResponse;
import net.hamza.banque.dto.RequestAuth;
import net.hamza.banque.jwt.JwtService;
import net.hamza.banque.model.Client;
import net.hamza.banque.model.Role;
import net.hamza.banque.model.Utilisateur;
import net.hamza.banque.repository.ClientRepo;
import net.hamza.banque.repository.UserRepo;
import net.hamza.banque.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserRepo userRepository;
    private final ClientRepo clientRepo;
    private  final JwtService jwtService;
    

    private final AuthenticationService service;

    @PostMapping("login")

    public ResponseEntity<?> login(@RequestBody RequestAuth request) {
        try {
            // Validation des données d'entrée
            if (request == null) {
                return ResponseEntity.badRequest().body("Request body is required");
            }
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            System.out.println("Login attempt for: " + request.getUsername());

            AuthResponse response = service.login(request);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            System.err.println("User not found: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
            @PostMapping("logup")
        public ResponseEntity<AuthResponse>  logup(@RequestBody  RequestAuth request){

            return  ResponseEntity.ok(service.register(request));
        }

    @GetMapping("user")
    @JsonSerialize
    public Utilisateur getUser(@RequestParam String u){
        return userRepository.findByEmail(u).get();

    }
    @GetMapping("/validateOtp")
    public boolean validateOtp(@RequestParam Integer otp,@RequestParam String token){
            Utilisateur user=this.getUserByToken(token);
            Integer otpuser=user.getOtp();
            user.setOtpNull();
            userRepository.save(user);
            return otpuser.equals(otp);


    }


    @PostMapping("isAuth")
    public Utilisateur getUserByToken(@RequestParam String token){
            String username=jwtService.extractUsername(token);


        return this.getUser(username);




    }
}
