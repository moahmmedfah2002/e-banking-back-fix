package net.hamza.banque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import net.hamza.banque.model.Client;
import net.hamza.banque.repository.ClientRepo;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClientController {
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper= new ObjectMapper();

    // Create client account
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepo.save(client);
    }

    // Delete client account
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientRepo.deleteById(id);
    }

    // Edit client account
    @PutMapping("/{id}")
    public Client editClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        return clientRepo.findById(id).map(client -> {
            client.setNom(updatedClient.getNom());
            client.setPrenom(updatedClient.getPrenom());
            client.setAdresse(updatedClient.getAdresse());
            client.setVille(updatedClient.getVille());
            client.setCodePostal(updatedClient.getCodePostal());
            client.setPays(updatedClient.getPays());
            client.setPassword(passwordEncoder.encode(updatedClient.getPassword()));
            client.setDateNaissance(updatedClient.getDateNaissance());
            client.setCin(updatedClient.getCin());
            client.setEmail(updatedClient.getEmail());
            client.setTelephone(updatedClient.getTelephone());
            return clientRepo.save(client);
        }).orElseThrow();
    }

    // Set account to inactive
    @PatchMapping("/{id}/inactif")
    public Client setInactif(@PathVariable Long id) {
        return clientRepo.findById(id).map(client -> {
            client.setEstActif(false);
            return clientRepo.save(client);
        }).orElseThrow();
    }

    // Set account to active
    @PatchMapping("/{id}/actif")
    public Client setActif(@PathVariable Long id) {
        return clientRepo.findById(id).map(client -> {
            client.setEstActif(true);
            return clientRepo.save(client);
        }).orElseThrow();
    }

    // Get all clients
    @GetMapping

    public ResponseEntity<?> getAllClients() {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(clientRepo.findAll()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing JSON: " + e.getMessage());
        }
    }

    // Get client by id
    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientRepo.findById(id).orElseThrow();
    }
}
