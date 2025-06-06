package net.hamza.banque.repository;

import net.hamza.banque.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepo extends JpaRepository<Compte, Long> {
    List<Compte> findByTypeCompte(String typeCompte);

    List<Compte> findByStatutTrue();
}