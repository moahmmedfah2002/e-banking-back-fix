package net.hamza.banque.service;

import jakarta.faces.view.facelets.Facelet;
import lombok.RequiredArgsConstructor;
import net.hamza.banque.model.Agent;
import net.hamza.banque.model.Client;
import net.hamza.banque.model.Feedback;
import net.hamza.banque.repository.AgentRepo;
import net.hamza.banque.repository.ClientRepo;
import net.hamza.banque.repository.FeedbackRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepo feedbackRepo;
    private final ClientRepo clientRepo;
    private final AgentRepo agentRepo;
    public void save(Feedback feedback,Client client, Agent agent) {
        feedback.setClient(client);
        feedback.setAgent(agent);
        feedbackRepo.save(feedback);
    }




    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        Feedback feedback = feedbackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'ID: " + id));

        feedback.setMotif(updatedFeedback.getMotif());
        feedback.setDetail(updatedFeedback.getDetail());

        return feedbackRepo.save(feedback);
    }
    public void deleteFeedback(Feedback feedback) {

        feedbackRepo.delete(feedback);
    }
}
