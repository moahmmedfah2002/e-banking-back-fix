package net.hamza.banque.repository;

import net.hamza.banque.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepo extends JpaRepository<Compte, Long> {
    List<Compte> findByTypeCompte(String typeCompte);

    List<Compte> findByStatueTrue();

     Optional<Compte> findByNumericCompte(long accountNumberSource);

//    Optional<Compte> findByNumericCompte(Long accountNumber);
}