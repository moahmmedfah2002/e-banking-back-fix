package net.hamza.banque.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@DiscriminatorColumn(name = "transaction_type")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;
    @Enumerated(EnumType.STRING)
    protected Transactiontype transactiontype;
    @Enumerated(EnumType.STRING)
    protected DebitType debitType;

    protected Double montant;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateTransaction;


    protected String reference;
    protected String description;




}