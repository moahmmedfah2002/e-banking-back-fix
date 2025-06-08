package net.hamza.banque.service;

import lombok.RequiredArgsConstructor;

import net.hamza.banque.dto.AuthResponse;
import net.hamza.banque.dto.RequestAuth;
import net.hamza.banque.jwt.JwtService;
import net.hamza.banque.model.Client;
import net.hamza.banque.model.Role;
import net.hamza.banque.model.Utilisateur;
import net.hamza.banque.repository.ClientRepo;
import net.hamza.banque.repository.UserRepo;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")

public class AuthenticationService {
    public final UserRepo userRepo;
    public final ClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private Environment env;


    public AuthResponse login(RequestAuth request) throws IOException {
        try {
            // Vérification des paramètres d'entrée
            if (request.getUsername() == null || request.getPassword() == null) {
                throw new IllegalArgumentException("Username and password are required");
            }

            // Authentication
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Recherche du client
            Client user = clientRepo.findByEmail(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));


            var jwtToken = jwtService.generateToken(user);
            int otp = sendOtp(user.getTelephone());
            user.setOtp(otp);

            userRepo.save(user);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .user(user)
                    .valid(true)
                    .otp(otp)
                    .build();

        } catch (Exception e) {
            // Log l'erreur pour le debugging
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw pour que le controller puisse gérer
        }
    }
        public int optGenerate(){
            Random rand =new Random();
            return rand.nextInt(1000000000);
        }

        public int sendOtp(String phone ) throws IOException {
            int otp =this.optGenerate();
            String body="{ \"session\": \"default\"," +
                    " \"chatId\": \""+phone+"@c.us\"," +
                    " \"text\": \""+"your code to login is :  "+otp+"\"" +
                    "}";
            Request request = new Request.Builder()
                    .url("http://waha:3000/api/sendText")
                    .post(RequestBody.create(MediaType.parse("application/json"),body))
                    .build();
            Call call =new OkHttpClient().newCall(request);
            call.execute();
            OkHttpClient client = new OkHttpClient();

//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody bodysms = RequestBody.create(mediaType, "{\"type\":\"transactional\",\"unicodeEnabled\":false,\"sender\":\"pfs\",\"recipient\":\""+phone+"\",\"content\":\"your code to login : "+otp+"\"}");
//            Request requestsms = new Request.Builder()
//                    .url("https://api.brevo.com/v3/transactionalSMS/send")
//                    .post(bodysms)
//                    .addHeader("accept", "application/json")
//                    .addHeader("content-type", "application/json")
//                    .addHeader(env.getRequiredProperty("api.name"), env.getRequiredProperty("api.value"))
//                    .build();
//
//            Response responsesms = client.newCall(requestsms).execute();
            return otp;

        }
    public AuthResponse register(RequestAuth request) {




            Client client = new Client();



            client.setEmail(request.getUsername());
            client.setPassword(passwordEncoder.encode(request.getPassword()));
            client.setRole(request.getRole());
            client.setTelephone(request.getPhone());
            client.setPrenom(request.getFirstName());
            client.setNom(request.getLastName());
            client.setEstActif(true);


            clientRepo.save(client);
            var jwtToken=jwtService.generateToken(client);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
    }
}
