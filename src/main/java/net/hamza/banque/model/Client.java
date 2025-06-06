package net.hamza.banque.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "clients")

public class Client extends Utilisateur {

    private Long numeroClient;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;

    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    private String cin;



    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Compte> comptes = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER)
    private List<Feedback> feedbacks = new ArrayList<>();





}