package net.hamza.banque.repository;

import net.hamza.banque.model.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirementRepo extends JpaRepository<Virement, Long> {
}
