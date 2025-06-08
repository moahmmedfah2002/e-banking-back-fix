package net.hamza.banque.controller;

import com.stripe.model.*;
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

@RestController
@RequestMapping("/virement")
public class PaymentController {

    @Autowired
    private CompteRepo compteRepository;

    @Autowired
    private VirementRepo transactionRepository;


    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @GetMapping("/send")
    public ResponseEntity<Object> payementTest(

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
                .setAmount((long) amount*10)
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


        compteRepository.save(compteSource);
        Transaction savedTransaction = transactionRepository.save(virement);

        return ResponseEntity.ok(savedTransaction);

    }





}