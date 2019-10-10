package app.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import app.utils.HibernateUtil;
import app.DTO.UtenteDTO;
import app.model.Utente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class UtentiDAO{

    private final SessionFactory sessionFactory;
    public UtentiDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    //metodo per selezionare tutti gli utenti


    @Transactional
    public List<UtenteDTO> selezionaTuttiUtenti() {
        Session session = getSession();
        List<Utente> utenti = session.createQuery("SELECT u FROM Utente u ORDER BY u.cognome ASC", Utente.class).list();
        List<UtenteDTO> utentiDTO;
//        for (Utente utente : utenti) {
//            UtenteDTO uDTO = new UtenteDTO();
//            uDTO.setCodiceFiscale(utente.getCodiceFiscale());
//            uDTO.setNome(utente.getNome());
//            uDTO.setCognome(utente.getCognome());
//            uDTO.setDataNascita(utente.getDataNascita());
//            uDTO.setSuperuser(utente.isSuperuser());
//            uDTO.setPassword(utente.getPassword());
//            //utentiDTO.add(toDto(utente));
//            utentiDTO.add(uDTO);
//
//        }
        utentiDTO  = utenti.stream().map(UtenteDTO::toDto).collect(Collectors.toList());
        //utentiDTO = utenti.stream().map(utente -> UtenteDTO.toDto(utente)).collect(Collectors.toList());
        return utentiDTO;
    }
    //metodo per salvare l'entity Utente
    @Transactional
    public void salvaUtente(UtenteDTO utenteDTO) {
        Utente utente = new Utente();
        utente.setCodiceFiscale(utenteDTO.getCodiceFiscale());
        utente.setCognome(utenteDTO.getCognome());
        utente.setNome(utenteDTO.getNome());
        utente.setDataNascita(utenteDTO.getDataNascita());
        utente.setSuperuser(utenteDTO.isSuperuser());
        utente.setPassword(utenteDTO.getPassword());
        utente.setImmagine(utenteDTO.getImmagine());
        getSession().saveOrUpdate(utente);
    }
    //metodo per selezionare l'utente partendo dal codice fiscale
    @Transactional
    public Utente selezionaUtenteByCF(String codiceFiscale){
        Utente utente = null;
        try {
            utente = (Utente) getSession().createQuery(
                    "SELECT s FROM Utente s WHERE codiceFiscale='" + codiceFiscale + "'").getSingleResult();
        }catch(Exception e){
            //non fare nulla, utente serve a null
        }
        return utente;
    }

    //metodo per controllare se esiste l'utente con CF e passwd dati
    @Transactional
    public Utente loginUtente(String codiceFiscale, String password){
        Utente utente = null;
        utente = (Utente) getSession().createQuery(
                "SELECT u FROM Utente u WHERE codiceFiscale='" + codiceFiscale + "' AND password='" + password + "'", Utente.class).getSingleResult();
        return utente;
    }

    @Transactional
    public void eliminaUtenteByCF(String codiceFiscale){
        Utente utente = getSession().load(Utente.class, codiceFiscale);
        getSession().delete(utente);
    }

    public static UtenteDTO toDto(Utente utente){
        UtenteDTO uDTO = new UtenteDTO();
        uDTO.setCodiceFiscale(utente.getCodiceFiscale());
        uDTO.setNome(utente.getNome());
        uDTO.setCognome(utente.getCognome());
        uDTO.setDataNascita(utente.getDataNascita());
        uDTO.setSuperuser(utente.isSuperuser());
        uDTO.setImmagine(utente.getImmagine());
        // uDTO.setPassword(utente.getPassword());
        return uDTO;
    }
}
