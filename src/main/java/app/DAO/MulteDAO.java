package app.DAO;

import app.DTO.MultaDTO;
import app.exceptions.prenotazione.DataNonCompresaNellaPrenotazioneException;
import app.model.Multa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class MulteDAO{
    private final SessionFactory sessionFactory;

    public MulteDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }


    @Transactional
    public List<Multa> selezionaMulteByCF(String codiceFiscale){
        Session session = getSession();
        List<Multa> multe = null;
        try{
            multe = (List<Multa>) session.createQuery(
                    "SELECT m FROM Multa m JOIN Prenotazione p ON id_prenotazione = numero JOIN Utente ON id_utente = codice_fiscale WHERE codice_fiscale = '" + codiceFiscale + "' ORDER BY p.dataInizio DESC, m.dataProblema DESC, p.dataFine DESC").list();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che rimanga a null
        }
        return multe;
    }

    @Transactional
    public void salvaMulta(MultaDTO multaDTO) throws DataNonCompresaNellaPrenotazioneException{
        Multa multa = toEntity(multaDTO);
        getSession().saveOrUpdate(multa);
    }

    public static MultaDTO toDto(Multa multa){
        MultaDTO multaDTO = new MultaDTO();
        multaDTO.setDescrizione(multa.getDescrizione());
        multaDTO.setPrenotazione(multa.getPrenotazione());
        if(multa.getDataProblema()!=null)
            multaDTO.setDataProblema(multa.getDataProblema());
        if(multa.getCodice()>0)
            multaDTO.setCodice(multa.getCodice());
        return multaDTO;
    }

    public static Multa toEntity(MultaDTO multaDTO) throws DataNonCompresaNellaPrenotazioneException {
        Multa multa = new Multa();
        multa.setDescrizione(multaDTO.getDescrizione());
        //prima prenotazione poi dataProblema perché se no dataProblema non sa quale siano data inizio e fine
        multa.setPrenotazione(multaDTO.getPrenotazione());
        if(multaDTO.getDataProblema()!=null){
            multa.setDataProblema(multaDTO.getDataProblema());
        }
        if(multaDTO.getCodice()>0){
            multa.setCodice(multaDTO.getCodice());
        }
        return multa;
    }
}
