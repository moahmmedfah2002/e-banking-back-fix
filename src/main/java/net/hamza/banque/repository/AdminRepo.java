package net.hamza.banque.repository;

import net.hamza.banque.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Administrateur, Long> {

    // Méthode pour trouver un administrateur par son email
    Administrateur findByEmail(String email);

    // Méthode pour vérifier l'existence d'un administrateur par son email
    boolean existsByEmail(String email);

    // Méthode pour supprimer un administrateur par son ID
    void deleteById(Long id);
}
