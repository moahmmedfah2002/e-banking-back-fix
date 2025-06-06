package net.hamza.banque.repository;

import net.hamza.banque.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepo extends JpaRepository<Recharge, Long> {
}
