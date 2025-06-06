package net.hamza.banque.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    private Long code;

    private String nom;
    private Double taunChange;

}