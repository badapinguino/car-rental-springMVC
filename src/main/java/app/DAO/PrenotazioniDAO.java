package app.DAO;

import app.DTO.PrenotazioneDTO;
import app.model.Prenotazione;
import app.model.Veicolo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrenotazioniDAO {
    private final SessionFactory sessionFactory;

    public PrenotazioniDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }


    @Transactional
    public void salvaPrenotazione(PrenotazioneDTO prenotazioneDTO){
        Prenotazione prenotazione = toEntity(prenotazioneDTO);
        getSession().saveOrUpdate(prenotazione);
    }

    @Transactional
    public List<Prenotazione> selezionaPrenotazioniByCF(String codiceFiscale){
        Session session = getSession();
        List<Prenotazione> prenotazioni = null;
        try{
            prenotazioni = (List<Prenotazione>) session.createQuery(
                    "SELECT p FROM Prenotazione p WHERE id_utente='" + codiceFiscale + "' ORDER BY dataInizio DESC, dataFine DESC").list();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che rimanga a null
        }
        return prenotazioni;
    }

    @Transactional
    public void approvaLaPrenotazione(int numeroPrenotazione){
        //fai una update
        Session session = getSession();
        try{
            Prenotazione prenotazione =
                    (Prenotazione)session.get(Prenotazione.class, numeroPrenotazione);
            prenotazione.setApprovata(true);
        }catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void eliminaPrenotazioneByNumero(int numeroPrenotazione){
        Prenotazione prenotazione = getSession().load(Prenotazione.class, numeroPrenotazione);
        getSession().delete(prenotazione);
    }

    @Transactional
    public Prenotazione selezionaPrenotazioneByNumero(int numeroPrenotazione){
        Session session = getSession();
        Prenotazione prenotazione = null;
        try{
            prenotazione = (Prenotazione) session.createQuery(
                    "SELECT p FROM Prenotazione p WHERE numero='" + numeroPrenotazione + "'").getSingleResult();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che prenotazione rimanga a null
        }
        return prenotazione;
    }

    @Transactional
    public boolean esistePrenotazioneDataVeicolo(LocalDate dataSelezionata, Veicolo veicolo, int numeroPrenotazione){
        Session session = getSession();
        Prenotazione prenotazione = null;
        try{
            prenotazione = (Prenotazione) session.createQuery(
                    "SELECT p FROM Prenotazione p WHERE id_veicolo='" + veicolo.getCodiceMezzo() + "' AND '"+ dataSelezionata + "'>=dataInizio AND '"+ dataSelezionata + "'<=dataFine").getSingleResult();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che prenotazione rimanga a null
        }
        if(prenotazione==null || prenotazione.getNumero() == numeroPrenotazione){
            return false;
        }else{
            return true;
        }
    }

    @Transactional
    public List<Prenotazione> selezionaPrenotazioniByCodiceMezzo(String codiceMezzo){
        Session session = getSession();
        List<Prenotazione> prenotazioni = null;
        try{
            prenotazioni = (List<Prenotazione>) session.createQuery(
                    "SELECT p FROM Prenotazione p WHERE id_veicolo='" + codiceMezzo + "' ORDER BY dataInizio DESC, dataFine DESC").list();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che rimanga a null
        }
        return prenotazioni;
    }

    public static PrenotazioneDTO toDto(Prenotazione prenotazione){
        PrenotazioneDTO prenotazioneDTO = new PrenotazioneDTO();
        prenotazioneDTO.setDataInizio(prenotazione.getDataInizio());
        prenotazioneDTO.setDataFine(prenotazione.getDataFine());
        prenotazioneDTO.setUtente(prenotazione.getUtente());
        prenotazioneDTO.setVeicolo(prenotazione.getVeicolo());
        prenotazioneDTO.setApprovata(prenotazione.isApprovata());
        if(prenotazione.getBuonoSconto()!=null) {
            prenotazioneDTO.setBuonoSconto(prenotazione.getBuonoSconto());
        }
        //non posso fare int != null, quindi metto >0
        if(prenotazione.getNumero()>0){
            prenotazioneDTO.setNumero(prenotazione.getNumero());
        }
        return prenotazioneDTO;
    }

    public static Prenotazione toEntity(PrenotazioneDTO prenotazioneDTO) {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDataInizio(prenotazioneDTO.getDataInizio());
        prenotazione.setDataFine(prenotazioneDTO.getDataFine());
        prenotazione.setUtente(prenotazioneDTO.getUtente());
        prenotazione.setVeicolo(prenotazioneDTO.getVeicolo());
        prenotazione.setApprovata(prenotazioneDTO.isApprovata());
        if(prenotazioneDTO.getBuonoSconto()!=null) {
            prenotazione.setBuonoSconto(prenotazioneDTO.getBuonoSconto());
        }
        //non posso fare int != null, quindi metto >0
        if(prenotazioneDTO.getNumero()>0){
            prenotazione.setNumero(prenotazioneDTO.getNumero());
        }
        return prenotazione;
    }
}
