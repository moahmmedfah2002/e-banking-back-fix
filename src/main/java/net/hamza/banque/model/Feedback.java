package net.hamza.banque.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "feedbacks")
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String motif;
    private String detail;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Agent agent;
}
