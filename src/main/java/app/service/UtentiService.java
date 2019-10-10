package app.service;

import app.DAO.UtentiDAO;
import app.DTO.UtenteDTO;
import app.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UtentiService {
    //@Autowired
    UtentiDAO utentiDAO;

    @Autowired(required = false)
    public UtentiService(@Qualifier("utentiDAO") UtentiDAO utentiDAO) {
        this.utentiDAO = utentiDAO;
    }

    public List<UtenteDTO> selezionaTuttiUtentiNoPassword(){
        List<UtenteDTO> utentiDTOConPassword = utentiDAO.selezionaTuttiUtenti();
        List<UtenteDTO> utentiDTONoPassword = new ArrayList<UtenteDTO>();
        for (UtenteDTO utenteConPassword : utentiDTOConPassword) {
            UtenteDTO uDTO = new UtenteDTO();
            uDTO.setCodiceFiscale(utenteConPassword.getCodiceFiscale());
            uDTO.setNome(utenteConPassword.getNome());
            uDTO.setCognome(utenteConPassword.getCognome());
            uDTO.setDataNascita(utenteConPassword.getDataNascita());
            uDTO.setSuperuser(utenteConPassword.isSuperuser());
            uDTO.setImmagine(utenteConPassword.getImmagine());
            utentiDTONoPassword.add(uDTO);
        }
        return utentiDTONoPassword;
    }

    public UtenteDTO selezionaUtenteByCFNoPassword(String codiceFiscale){
        Utente utente = utentiDAO.selezionaUtenteByCF(codiceFiscale);
        UtenteDTO uDTO = null;
        try {
            uDTO = new UtenteDTO();
            uDTO.setNome(utente.getNome());
            uDTO.setCognome(utente.getCognome());
            uDTO.setDataNascita(utente.getDataNascita());
            uDTO.setSuperuser(utente.isSuperuser());
            uDTO.setCodiceFiscale(utente.getCodiceFiscale());
            uDTO.setImmagine(utente.getImmagine());
        }catch(NullPointerException npe){
            //non fare nulla perché voglio mi ritorni null
        }
        return uDTO;
    }
    public UtenteDTO loginUtente(String codiceFiscale, String password){
        Utente utente = utentiDAO.loginUtente(codiceFiscale, password);
        UtenteDTO uDTO = null;
        try {
            //se c'è un risultato lo assegno ad uDTO
            uDTO = new UtenteDTO();
            uDTO.setNome(utente.getNome());
            uDTO.setCognome(utente.getCognome());
            uDTO.setSuperuser(utente.isSuperuser());
        }catch (NullPointerException npe){
            //non faccio niente perché voglio che utente rimanga a null
        }
        return uDTO;
    }

    public UtenteDTO eliminaUtenteByCF(String codiceFiscale){
        Utente utente = utentiDAO.selezionaUtenteByCF(codiceFiscale);
        UtenteDTO uDTO = null;
        if(utente!=null) {
            uDTO = new UtenteDTO();
            uDTO.setCognome(utente.getCognome());
            uDTO.setNome(utente.getNome());
            uDTO.setCodiceFiscale(utente.getCodiceFiscale());
            utentiDAO.eliminaUtenteByCF(codiceFiscale);
        }
        return uDTO;
    }

    public boolean controllaSeSuperuserByCF(String codiceFiscale){
        Utente utente = utentiDAO.selezionaUtenteByCF(codiceFiscale);
        boolean superuser = utente.isSuperuser();
        return superuser;
    }
}
