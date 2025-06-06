package net.hamza.banque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "administrateurs")
@Data
public class Administrateur extends Utilisateur {


}
