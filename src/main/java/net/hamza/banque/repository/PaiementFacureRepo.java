package net.hamza.banque.repository;

import net.hamza.banque.model.PaiementFacture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementFacureRepo extends JpaRepository<PaiementFacture, Long> {

}
