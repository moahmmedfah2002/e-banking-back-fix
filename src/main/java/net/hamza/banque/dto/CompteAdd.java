package net.hamza.banque.dto;

import lombok.Data;
import net.hamza.banque.model.Client;
import net.hamza.banque.model.Compte;
@Data
public class CompteAdd {
    private Compte compte;
    private Client client;
}
