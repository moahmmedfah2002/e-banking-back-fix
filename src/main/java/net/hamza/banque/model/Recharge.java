package net.hamza.banque.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "recharges")
@DiscriminatorValue("RECHARGE")
@Data
public class Recharge extends Transaction {
    private String numeroTelephone;
    private Double montant;
    private String operateur;


}