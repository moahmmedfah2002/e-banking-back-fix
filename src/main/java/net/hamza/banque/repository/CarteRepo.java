package net.hamza.banque.repository;

import net.hamza.banque.model.Carte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteRepo extends JpaRepository<Carte, Long> {

}
