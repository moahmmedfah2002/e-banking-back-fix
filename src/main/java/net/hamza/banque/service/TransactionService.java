package net.hamza.banque.service;

import lombok.RequiredArgsConstructor;
import net.hamza.banque.dto.ResponseTransaction;
import net.hamza.banque.dto.Transactions;
import net.hamza.banque.model.*;
import net.hamza.banque.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {


    private final CompteRepo compteRepo;

    private final RechargeRepo rechargeRepo;
    private final PaiementFacureRepo paiementFacureRepo;
    private final VirementRepo virementRepo;


    public void ResponceTransaction(Compte compte, Recharge recharge) {

        this.rechargeRepo.save(recharge);
        List<Recharge> recharges=compte.getRecharges();
        recharges.add(recharge);
        compte.setRecharges(recharges);
        compte.setSolde(compte.getSolde()-recharge.getMontant());
        compteRepo.save(compte);
        ResponseTransaction responseTransaction = ResponseTransaction.builder()
                .message("Recharge effectuée avec succès")
                .status(true)
                .build();
    }




    public ResponseTransaction addVirement(Virement virement) {

        this.virementRepo.save(virement);

        Compte compteDebit =virement.getCompteSource();
        List<Virement> virements=compteDebit.getVirements();
        virements.add(virement);
        compteDebit.setVirements(virements);
        if (compteDebit.getSolde() < virement.getMontant()) {
            return ResponseTransaction.builder()
                    .message("Solde insuffisant pour effectuer le virement")
                    .status(false)
                    .build();
        }
        compteDebit.setSolde(compteDebit.getSolde()-virement.getMontant());
        Compte compteDest=virement.getCompteDestination();
        List<Virement> virement1 = compteDebit.getVirements();
        virement1.add(virement);
        compteDest.setVirements(virement1);
        compteDest.setSolde(compteDest.getSolde()+virement.getMontant());
        compteRepo.save(compteDest);
        compteRepo.save(compteDebit);
        return ResponseTransaction.builder()
                .message("Virement effectué avec succès")
                .status(true)
                .build();



    }
    public ResponseTransaction addPaiementFacture(PaiementFacture paiementFacture) {

        this.paiementFacureRepo.save(paiementFacture);

        Compte compte = paiementFacture.getCompte();
        List<PaiementFacture> paiementFactures = compte.getPaiementFactures();
        paiementFactures.add(paiementFacture);
        compte.setPaiementFactures(paiementFactures);
        if (compte.getSolde() < paiementFacture.getMontant()) {
            return ResponseTransaction.builder()
                    .message("Solde insuffisant pour effectuer le paiement de la facture")
                    .status(false)
                    .build();
        }
        compte.setSolde(compte.getSolde() - paiementFacture.getMontant());
        compteRepo.save(compte);
        return ResponseTransaction.builder()
                .message("Paiement de la facture effectué avec succès")
                .status(true)
                .build();
    }




}