package net.hamza.banque.controller;

import lombok.RequiredArgsConstructor;
import net.hamza.banque.dto.CompteAdd;
import net.hamza.banque.model.Client;
import net.hamza.banque.model.Compte;
import net.hamza.banque.repository.ClientRepo;
import net.hamza.banque.repository.CompteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comptes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CompteController {

    private final CompteRepo compteRepository;
    private final ClientRepo clientRepository;

    /**
     * Récupérer tous les comptes
     */
    @GetMapping
    public ResponseEntity<List<Compte>> getAllComptes() {
        try {
            List<Compte> comptes = compteRepository.findAll();
            return new ResponseEntity<>(comptes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Récupérer un compte par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Compte> getCompteById(@PathVariable Long id) {
        try {
            Optional<Compte> compte = compteRepository.findById(id);
            if (compte.isPresent()) {
                return new ResponseEntity<>(compte.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Créer un nouveau compte
     */
    @PostMapping("/add")
    public ResponseEntity<?> createCompte(@RequestBody CompteAdd compteAdd) {
        try {
            Client client=clientRepository.findByEmail(compteAdd.getClient().getUsername()).get();
            Compte nouveauCompte = compteRepository.save(compteAdd.getCompte());
            List<Compte> comptes = client.getComptes();
            comptes.add(nouveauCompte);
            client.setComptes(comptes);
            clientRepository.save(client);


            return new ResponseEntity<>(nouveauCompte, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Mettre à jour un compte existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Compte> updateCompte(@PathVariable Long id, @RequestBody Compte compteDetails) {
        try {
            Optional<Compte> compteOptional = compteRepository.findById(id);
            if (compteOptional.isPresent()) {
                Compte compte = compteOptional.get();

                // Mise à jour des champs modifiables
                if (compteDetails.getSolde() != null) {
                    compte.setSolde(compteDetails.getSolde());
                }
                if (compteDetails.getStatut() != null) {
                    compte.setStatut(compteDetails.getStatut());
                }
                if (compteDetails.getTypeCompte() != null) {
                    compte.setTypeCompte(compteDetails.getTypeCompte());
                }

                Compte compteUpdated = compteRepository.save(compte);
                return new ResponseEntity<>(compteUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Supprimer un compte
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCompte(@PathVariable Long id) {
        try {
            if (compteRepository.existsById(id)) {
                compteRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Activer/Désactiver un compte
     */
    @PutMapping("/{id}/statut")
    public ResponseEntity<Compte> toggleCompteStatut(@PathVariable Long id) {
        try {
            Optional<Compte> compteOptional = compteRepository.findById(id);
            if (compteOptional.isPresent()) {
                Compte compte = compteOptional.get();
                compte.setStatut(!compte.getStatut());
                Compte compteUpdated = compteRepository.save(compte);
                return new ResponseEntity<>(compteUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}/solde")
    public ResponseEntity<Compte> updateSolde(@PathVariable Long id, @RequestParam Double nouveauSolde) {
        try {
            Optional<Compte> compteOptional = compteRepository.findById(id);
            if (compteOptional.isPresent()) {
                Compte compte = compteOptional.get();
                compte.setSolde(nouveauSolde);
                Compte compteUpdated = compteRepository.save(compte);
                return new ResponseEntity<>(compteUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Récupérer les comptes actifs
     */
    @GetMapping("/actifs")
    public ResponseEntity<List<Compte>> getComptesActifs() {
        try {
            List<Compte> comptesActifs = compteRepository.findByStatutTrue();
            return new ResponseEntity<>(comptesActifs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Récupérer les comptes par type
     */
    @GetMapping("/type/{typeCompte}")
    public ResponseEntity<List<Compte>> getComptesByType(@PathVariable String typeCompte) {
        try {
            List<Compte> comptes = compteRepository.findByTypeCompte(typeCompte);
            return new ResponseEntity<>(comptes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
