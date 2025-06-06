package net.hamza.banque.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "agents")
@Data

public class Agent extends Utilisateur {
    private Long AgentId;
    private String agence;
    @OneToMany
    private List<Client> cleients = new ArrayList<>();
    @OneToMany
    private List<Feedback> feedbacks = new ArrayList<>();



}