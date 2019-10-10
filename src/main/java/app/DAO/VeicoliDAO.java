package app.DAO;

import app.DTO.VeicoloDTO;
import app.exceptions.veicolo.AnnoFuturoException;
import app.model.Veicolo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class VeicoliDAO {

    private final SessionFactory sessionFactory;

    public VeicoliDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public Veicolo selezionaVeicoloByCodice(String codiceVeicolo){
        Session session = getSession();
        Veicolo veicolo = null;
        try{
            veicolo = (Veicolo) session.createQuery(
                    "SELECT v FROM Veicolo v WHERE codice_mezzo = '" + codiceVeicolo + "'").getSingleResult();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che veicolo rimanga a null
        }
        return veicolo;
    }

    @Transactional
    public List<Veicolo> selezionaTuttiVeicoli() {
        Session session = getSession();
        List<Veicolo> veicoli = null;
        try{
            veicoli = (List<Veicolo>) session.createQuery(
                    "SELECT v FROM Veicolo v ORDER BY casaCostruttrice ASC, modello ASC, anno DESC").list();
        }catch(Exception e){
            //non faccio niente perché voglio che rimanga a null
        }
        return veicoli;
    }

    @Transactional
    public void eliminaVeicoloByCodice(String codiceMezzo) throws PersistenceException {
        Veicolo veicolo = getSession().load(Veicolo.class, codiceMezzo);
        getSession().delete(veicolo);
    }

    @Transactional
    public void salvaVeicolo(VeicoloDTO veicoloDTO) throws AnnoFuturoException {
        Veicolo veicolo;
        veicolo = toEntity(veicoloDTO);
        getSession().saveOrUpdate(veicolo);
    }

    public static VeicoloDTO toDto(Veicolo veicolo){
        VeicoloDTO vDTO = new VeicoloDTO();
        vDTO.setCodiceMezzo(veicolo.getCodiceMezzo());
        vDTO.setTarga(veicolo.getTarga());
        vDTO.setCasaCostruttrice(veicolo.getCasaCostruttrice());
        vDTO.setModello(veicolo.getModello());
        vDTO.setTipologia(veicolo.getTipologia());
        vDTO.setPrezzoGiornata(veicolo.getPrezzoGiornata());
        vDTO.setAnno(veicolo.getAnno());
        if(veicolo.getPrenotazioni()!=null){
            vDTO.setPrenotazioni(veicolo.getPrenotazioni());
        }
        vDTO.setDettagli(vDTO.getCasaCostruttrice() + " " + vDTO.getModello() + ". Tipologia: " + vDTO.getTipologia() + ". Al prezzo di " + vDTO.getPrezzoGiornata() + "€ al giorno.");
        return vDTO;
    }

    public static Veicolo toEntity(VeicoloDTO veicoloDTO) throws AnnoFuturoException {
        Veicolo veicolo = new Veicolo();
        veicolo.setCodiceMezzo(veicoloDTO.getCodiceMezzo());
        veicolo.setTarga(veicoloDTO.getTarga());
        veicolo.setCasaCostruttrice(veicoloDTO.getCasaCostruttrice());
        veicolo.setModello(veicoloDTO.getModello());
        veicolo.setTipologia(veicoloDTO.getTipologia());
        veicolo.setPrezzoGiornata(veicoloDTO.getPrezzoGiornata());
        veicolo.setAnno(veicoloDTO.getAnno());
        if(veicoloDTO.getPrenotazioni()!=null)
            veicolo.setPrenotazioni(veicoloDTO.getPrenotazioni());
        return veicolo;
    }
}
