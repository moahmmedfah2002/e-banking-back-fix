package net.hamza.banque.controller;

import lombok.RequiredArgsConstructor;
import net.hamza.banque.dto.ResponseTransaction;
import net.hamza.banque.model.Compte;
import net.hamza.banque.model.PaiementFacture;
import net.hamza.banque.model.Recharge;
import net.hamza.banque.model.Virement;
import net.hamza.banque.repository.CompteRepo;
import net.hamza.banque.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;
    private final CompteRepo compteRepo;

    /**
     * Endpoint pour effectuer une recharge
     * @param compteId ID du compte à recharger
     * @param recharge Objet Recharge contenant les détails de la recharge
     * @return ResponseEntity avec le statut de l'opération
     */
    @PostMapping("/recharge/{compteId}")
    public ResponseEntity<?> effectuerRecharge(
            @PathVariable Long compteId,
            @RequestBody Recharge recharge) {

        try {
            Optional<Compte> compteOpt = compteRepo.findById(compteId);
            if (compteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseTransaction.builder()
                                .message("Compte non trouvé")
                                .status(false)
                                .build());
            }

            Compte compte = compteOpt.get();

            transactionService.ResponceTransaction(compte, recharge);

            return ResponseEntity.ok(ResponseTransaction.builder()
                    .message("Recharge effectuée avec succès")
                    .status(true)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseTransaction.builder()
                            .message("Erreur lors de la recharge: " + e.getMessage())
                            .status(false)
                            .build());
        }
    }

    /**
     * Endpoint pour effectuer un virement
     * @param virement Objet Virement contenant les détails du virement
     * @return ResponseEntity avec le statut de l'opération
     */
    @PostMapping("/virement")
    public ResponseEntity<ResponseTransaction> effectuerVirement(@RequestBody Virement virement) {

        try {
            // Vérifier que les comptes source et destination existent
            if (virement.getCompteSource() == null || virement.getCompteDestination() == null) {
                return ResponseEntity.badRequest()
                        .body(ResponseTransaction.builder()
                                .message("Compte source et destination requis")
                                .status(false)
                                .build());
            }

            ResponseTransaction response = transactionService.addVirement(virement);

            if (response.isStatus()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseTransaction.builder()
                            .message("Erreur lors du virement: " + e.getMessage())
                            .status(false)
                            .build());
        }
    }

    /**
     * Endpoint pour effectuer un paiement de facture
     * @param paiementFacture Objet PaiementFacture contenant les détails du paiement
     * @return ResponseEntity avec le statut de l'opération
     */
    @PostMapping("/paiement-facture")
    public ResponseEntity<ResponseTransaction> effectuerPaiementFacture(@RequestBody PaiementFacture paiementFacture) {

        try {
            if (paiementFacture.getCompte() == null) {
                return ResponseEntity.badRequest()
                        .body(ResponseTransaction.builder()
                                .message("Compte requis pour le paiement")
                                .status(false)
                                .build());
            }

            ResponseTransaction response = transactionService.addPaiementFacture(paiementFacture);

            if (response.isStatus()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseTransaction.builder()
                            .message("Erreur lors du paiement: " + e.getMessage())
                            .status(false)
                            .build());
        }
    }

    /**
     * Endpoint pour récupérer l'historique des transactions d'un compte
     * @param compteId ID du compte
     * @return ResponseEntity avec l'historique des transactions
     */
    @GetMapping("/historique/{compteId}")
    public ResponseEntity<?> getHistoriqueTransactions(@PathVariable Long compteId) {

        try {
            Optional<Compte> compteOpt = compteRepo.findById(compteId);
            if (compteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseTransaction.builder()
                                .message("Compte non trouvé")
                                .status(false)
                                .build());
            }

            Compte compte = compteOpt.get();

            // Créer un objet contenant toutes les transactions
            var historique = new Object() {
                public final List<Recharge> recharges = compte.getRecharges();
                public final List<Virement> virements = compte.getVirements();
                public final List<PaiementFacture> paiementFactures = compte.getPaiementFactures();
            };

            return ResponseEntity.ok(historique);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseTransaction.builder()
                            .message("Erreur lors de la récupération de l'historique: " + e.getMessage())
                            .status(false)
                            .build());
        }
    }
}