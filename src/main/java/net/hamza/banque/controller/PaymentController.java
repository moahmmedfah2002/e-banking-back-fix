package net.hamza.banque.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stripe.model.*;
import jakarta.transaction.Transactional;
import net.hamza.banque.dto.ResponseVirement;
import net.hamza.banque.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;

import net.hamza.banque.repository.CompteRepo;
import net.hamza.banque.repository.VirementRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/virement")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private CompteRepo compteRepository;

    @Autowired
    private VirementRepo transactionRepository;


    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Transactional
    @GetMapping("/sendStip")
    public ResponseEntity<Object> payementStrip(

            @RequestParam Long accountNumberSource,
            @RequestParam double  amount) throws StripeException {
        Stripe.apiKey=this.stripeApiKey;
        Compte compteSource = compteRepository.findByNumericCompte(accountNumberSource).orElse(null);

        assert compteSource != null;
        System.out.println(compteSource.getSolde());

        if (compteSource.getSolde()-amount<0){
            return ResponseEntity.badRequest().body("amount not enough");
        }
// Use it in a PaymentIntent
        PaymentIntentCreateParams params1 = PaymentIntentCreateParams.builder()
                .setAmount((long) amount*100)
                .setCurrency("usd")
                .setPaymentMethod("pm_card_visa")

                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        PaymentIntent.create(params1);



        compteSource.setSolde(compteSource.getSolde() - amount);

        // Create transaction record
        Virement virement = new Virement();
        virement.setCompteSource(compteSource);
        virement.setMontant(amount);
        virement.setCompteDestination(compteSource);
        virement.setDateTransaction(new java.util.Date());
        virement.setDescription("Stripe deposit - ");
        virement.setTransactiontype(Transactiontype.TRANSACTIONEXTERNE);
        virement.setDebitType(DebitType.DEBIT);
        List<Virement> virement1=compteSource.getVirements();
        virement1.add(virement);
        compteSource.setVirements(virement1);
        compteRepository.save(compteSource);
        Transaction savedTransaction = transactionRepository.save(virement);

        return ResponseEntity.ok(savedTransaction);

    }
    @Transactional
    @GetMapping("/sendAccount")
    @JsonSerialize
    public ResponseVirement payementAccount(@RequestParam long accountReciver,
                                            @RequestParam long accountDebit,
                                            @RequestParam double amount,
                                            @RequestParam String motif
                                                    ){
        Compte compteSource = compteRepository.findByNumericCompte(accountReciver).orElse(null);
        Compte compteDestination = compteRepository.findByNumericCompte(accountDebit).orElse(null);
        if( compteDestination==null){
            return new ResponseVirement("destination compte not found");
        }
        if (compteSource==null ){
            return new ResponseVirement("something went wrong");
        }
        if (compteSource.getSolde()-amount<0){
            return new ResponseVirement("amount not enough");
        }
        compteDestination.setSolde(compteDestination.getSolde() + amount);
        compteSource.setSolde(compteSource.getSolde()-amount);
        Virement virement1 = new Virement();
        virement1.setCompteSource(compteSource);
        virement1.setMontant(amount);
        virement1.setCompteDestination(compteDestination);
        virement1.setDateTransaction(new java.util.Date());
        virement1.setTransactiontype(Transactiontype.TRANSACTIONEXTERNE);
        virement1.setDebitType(DebitType.DEBIT);
        virement1.setMotif(motif);
        List<Virement> listVirementSource=compteSource.getVirements();
        listVirementSource.add(virement1);
        compteSource.setVirements(listVirementSource);
        compteRepository.save(compteSource);
        transactionRepository.save(virement1);


        Virement virement2 = new Virement();
        virement2.setCompteSource(compteSource);
        virement2.setMontant(amount);
        virement2.setCompteDestination(compteDestination);
        virement2.setDateTransaction(new java.util.Date());
        virement2.setTransactiontype(Transactiontype.TRANSACTIONEXTERNE);
        virement2.setDebitType(DebitType.RECEIVE);
        virement2.setMotif(motif);
        List<Virement> listVirementDestination=compteDestination.getVirements();
        listVirementDestination.add(virement2);
        compteDestination.setVirements(listVirementDestination);
        compteRepository.save(compteDestination);
        transactionRepository.save(virement2);
        return new ResponseVirement("success");





    }





}