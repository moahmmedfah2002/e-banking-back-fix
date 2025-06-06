package net.hamza.banque.repository;

import net.hamza.banque.model.Compte;
import net.hamza.banque.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

}
