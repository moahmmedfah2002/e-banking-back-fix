package net.hamza.banque.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.hamza.banque.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import net.hamza.banque.model.Administrateur;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private AdminRepo administrateurRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper= new ObjectMapper();

    @PostMapping
    public Administrateur createAdmin(@RequestBody Administrateur admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return administrateurRepo.save(admin);
    }

    @GetMapping
    @JsonSerialize
    public ResponseEntity<?> getAllAdmins() {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(administrateurRepo.findAll()));

        }catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing JSON: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Administrateur getAdminById(@PathVariable Long id) {
        return administrateurRepo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Administrateur editAdmin(@PathVariable Long id, @RequestBody Administrateur updatedAdmin) {
        return administrateurRepo.findById(id).map(admin -> {
            admin.setNom(updatedAdmin.getNom());
            admin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
            admin.setPrenom(updatedAdmin.getPrenom());
            admin.setEmail(updatedAdmin.getEmail());
            admin.setTelephone(updatedAdmin.getTelephone());
            return administrateurRepo.save(admin);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        administrateurRepo.deleteById(id);
    }

    @PatchMapping("/{id}/activate")
    public Administrateur activateAdmin(@PathVariable Long id) {
        return administrateurRepo.findById(id).map(admin -> {
            admin.setEstActif(true);
            return administrateurRepo.save(admin);
        }).orElseThrow();
    }

    @PatchMapping("/{id}/deactivate")
    public Administrateur deactivateAdmin(@PathVariable Long id) {
        return administrateurRepo.findById(id).map(admin -> {
            admin.setEstActif(false);
            return administrateurRepo.save(admin);
        }).orElseThrow();
    }
}
