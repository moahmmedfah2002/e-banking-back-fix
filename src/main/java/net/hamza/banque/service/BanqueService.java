package net.hamza.banque.service;

import lombok.RequiredArgsConstructor;
import net.hamza.banque.model.Banque;
import net.hamza.banque.repository.BanqueRepo;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class BanqueService {
    private final BanqueRepo banqueRepo;
    public void saveBanque(Banque banque) {
        banqueRepo.save(banque);
    }
    public void deleteBanque(Banque banque) {
        banqueRepo.delete(banque);
    }
    public Banque updateBanque(Banque banque,Long id) {
        Banque banque1= banqueRepo.findById(id);
        if (banque1 != null) {
            banque1.setNom(banque.getNom());
            banque1.setAdresse(banque.getAdresse());
            banque1.setTelephone(banque.getTelephone());
            banque1.setEmail(banque.getEmail());

        }
        return banqueRepo.save(banque1);
    }

}
