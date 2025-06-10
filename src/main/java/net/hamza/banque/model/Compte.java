package net.hamza.banque.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comptes")
@NoArgsConstructor
@RequestMapping("api/comptes")
@Getter
@Setter
@ToString
public class    Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numericCompte;

    private Double solde = 0.0;

    private Boolean statue = true; // Renommé de 'statue' à 'statut'

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation = new Date();

    private String typeCompte;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    public String getClient() {
        return client == null ? "" : client.getNom();
    }

    @OneToMany( fetch = FetchType.EAGER)
    private List<Virement> virements = new ArrayList<Virement>();
    @OneToMany( fetch = FetchType.EAGER)
    private List<Recharge> recharges = new ArrayList<Recharge>();
    @OneToMany( fetch = FetchType.EAGER )
    private List<PaiementFacture> paiementFactures = new ArrayList<PaiementFacture>();

    public Collection<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(virements);
        transactions.addAll(recharges);
        transactions.addAll(paiementFactures);
        return transactions;
    }
}