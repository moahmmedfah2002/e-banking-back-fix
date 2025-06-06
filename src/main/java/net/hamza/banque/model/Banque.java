package net.hamza.banque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data

public class Banque {
    @Id
    private long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    @OneToMany
    private List<Agent> agents;
    private Status status;

}
