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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Client> cleients = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new ArrayList<>();



}