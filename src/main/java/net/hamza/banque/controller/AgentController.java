package net.hamza.banque.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import net.hamza.banque.model.Agent;
import net.hamza.banque.model.Client;
import net.hamza.banque.repository.AgentRepo;
import net.hamza.banque.repository.ClientRepo;
import java.util.List;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*")
public class AgentController {
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper= new ObjectMapper();

    // Create agent account
    @PostMapping
    public Agent createAgent(@RequestBody Agent agent) {
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        return agentRepo.save(agent);
    }

    // Delete agent account
    @DeleteMapping("/{id}")
    public void deleteAgent(@PathVariable Long id) {
        agentRepo.deleteById(id);
    }

    // Edit agent account
    @PutMapping("/{id}")
    public Agent editAgent(@PathVariable Long id, @RequestBody Agent updatedAgent) {
        return agentRepo.findById(id).map(agent -> {
            agent.setNom(updatedAgent.getNom());
            agent.setPrenom(updatedAgent.getPrenom());
            agent.setAgence(updatedAgent.getAgence());
            agent.setPassword(passwordEncoder.encode(updatedAgent.getPassword()));
            agent.setEmail(updatedAgent.getEmail());
            agent.setTelephone(updatedAgent.getTelephone());
            return agentRepo.save(agent);
        }).orElseThrow();
    }

    // Activate/deactivate agent account
    @PatchMapping("/{id}/activate")
    public Agent activateAgent(@PathVariable Long id) {
        return agentRepo.findById(id).map(agent -> {
            agent.setEstActif(true);
            return agentRepo.save(agent);
        }).orElseThrow();
    }
    @PatchMapping("/{id}/deactivate")
    public Agent deactivateAgent(@PathVariable Long id) {
        return agentRepo.findById(id).map(agent -> {
            agent.setEstActif(false);
            return agentRepo.save(agent);
        }).orElseThrow();
    }

    // Get all agents
    @GetMapping
    public ResponseEntity<?> getAllAgents() {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(agentRepo.findAll()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing JSON: " + e.getMessage());
        }
    }

    // Get agent by id
    @GetMapping("/{id}")
    public Agent getAgentById(@PathVariable Long id) {
        return agentRepo.findById(id).orElseThrow();
    }

    // Get all clients managed by an agent
    @GetMapping("/{agentId}/clients")
    public ResponseEntity getClientsByAgent(@PathVariable Long agentId) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(agent.getCleients()));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing JSON: " + e.getMessage());
        }
    }

    // Get a specific client managed by an agent
    @GetMapping("/{agentId}/clients/{clientId}")
    public Client getClientByAgent(@PathVariable Long agentId, @PathVariable Long clientId) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        return agent.getCleients().stream()
                .filter(client -> client.getId().equals(clientId))
                .findFirst()
                .orElseThrow();
    }

    // Agent manages client accounts
    @PostMapping("/{agentId}/clients")
    public Client createClientByAgent(@PathVariable Long agentId, @RequestBody Client client) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        Client savedClient = clientRepo.save(client);
        agent.getCleients().add(savedClient);
        agentRepo.save(agent);
        return savedClient;
    }

    @DeleteMapping("/{agentId}/clients/{clientId}")
    public void deleteClientByAgent(@PathVariable Long agentId, @PathVariable Long clientId) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        Client client = clientRepo.findById(clientId).orElseThrow();
        agent.getCleients().remove(client);
        agentRepo.save(agent);
        clientRepo.delete(client);
    }

    @PutMapping("/{agentId}/clients/{clientId}")
    public Client editClientByAgent(@PathVariable Long agentId, @PathVariable Long clientId, @RequestBody Client updatedClient) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        return clientRepo.findById(clientId).map(client -> {
            client.setNom(updatedClient.getNom());
            client.setPrenom(updatedClient.getPrenom());
            client.setAdresse(updatedClient.getAdresse());
            client.setPassword(passwordEncoder.encode(updatedClient.getPassword()));
            client.setVille(updatedClient.getVille());
            client.setCodePostal(updatedClient.getCodePostal());
            client.setPays(updatedClient.getPays());
            client.setDateNaissance(updatedClient.getDateNaissance());
            client.setCin(updatedClient.getCin());
            client.setEmail(updatedClient.getEmail());
            client.setTelephone(updatedClient.getTelephone());
            return clientRepo.save(client);
        }).orElseThrow();
    }

    @PatchMapping("/{agentId}/clients/{clientId}/activate")
    public Client activateClientByAgent(@PathVariable Long agentId, @PathVariable Long clientId) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        return clientRepo.findById(clientId).map(client -> {
            client.setEstActif(true);
            return clientRepo.save(client);
        }).orElseThrow();
    }
    @PatchMapping("/{agentId}/clients/{clientId}/deactivate")
    public Client deactivateClientByAgent(@PathVariable Long agentId, @PathVariable Long clientId) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        return clientRepo.findById(clientId).map(client -> {
            client.setEstActif(false);
            return clientRepo.save(client);
        }).orElseThrow();
    }
    @GetMapping("/{agentId}/transactions")
    public ResponseEntity<?> getTransactionsByAgent(@PathVariable Long agentId) {
        Agent agent = agentRepo.findById(agentId).orElseThrow();
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(agent.getCleients().stream()
                    .flatMap(client -> client.getComptes().stream())
                    .flatMap(compte -> compte.getTransactions().stream())
                    .toList()));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing JSON: " + e.getMessage());
        }
    }
}
