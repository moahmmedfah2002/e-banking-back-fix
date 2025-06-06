package net.hamza.banque.service;

import lombok.RequiredArgsConstructor;
import net.hamza.banque.model.Agent;
import net.hamza.banque.model.Client;
import net.hamza.banque.repository.AgentRepo;
import net.hamza.banque.repository.ClientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepo agentRepo;
    private final ClientRepo clientRepo;
    public void creerClient(Client client,Agent agent) {

        Client nouveauClient = clientRepo.save(client);
        List<Client> clients = agent.getCleients();
        clients.add(nouveauClient);
        agent.setCleients(clients);
        agentRepo.save(agent);




    }
    public void supprimerClient(Agent agent,Client client) {
        List<Client> clients = agent.getCleients();
        clients.remove(client);
        agent.setCleients(clients);
        agentRepo.save(agent);
        clientRepo.delete(client);
    }
    public void desactiverClient(Agent agent,Client client) {
        client.isAccountNonLocked();
        clientRepo.save(client);
    }

}
