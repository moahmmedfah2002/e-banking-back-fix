package net.hamza.banque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.hamza.banque.repository.ClientRepo;
import org.hibernate.mapping.Array;


@EqualsAndHashCode(callSuper = true)
@Entity
@RequiredArgsConstructor
@Table(name = "virements")
@Data
@DiscriminatorValue("VIREMENT")
public class Virement extends Transaction {


    @ManyToOne()
    private Compte compteSource;

    @ManyToOne
    private Compte compteDestination;
    private String motif;
    @JsonProperty("namedest")
    public String getCompteDestinationName() {
        return compteDestination.getClient();
    }

    @JsonIgnore
    public Compte getCompteDestination() {
        return this.compteDestination;
    }







    @JsonIgnore
    public Compte getCompteSource() {
        return compteSource;
    }
    @JsonProperty("namesource")
    public String getCompteSourceName() {
        return this.compteSource.getClient();
    }

}