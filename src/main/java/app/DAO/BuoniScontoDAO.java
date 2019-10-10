package app.DAO;

import app.DTO.BuonoScontoDTO;
import app.exceptions.buonoSconto.ValorePercentualeOltreCentoException;
import app.model.BuonoSconto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BuoniScontoDAO {
    private final SessionFactory sessionFactory;

    public BuoniScontoDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public List<BuonoSconto> selezionaTuttiBuoniSconto() {
        Session session = getSession();
        List<BuonoSconto> buoniSconto = null;
        try{
            buoniSconto = (List<BuonoSconto>) session.createQuery(
                    "SELECT b FROM BuonoSconto b ORDER BY codiceSconto ASC").list();
        }catch(Exception e){
            //non faccio niente perché voglio che utente rimanga a null
        }
        return buoniSconto;
    }

    @Transactional
    public BuonoSconto selezionaBuonoScontoByCodice(String codiceSconto){
        Session session = getSession();
        BuonoSconto buonoSconto = null;
        try{
            buonoSconto = (BuonoSconto) session.createQuery(
                    "SELECT b FROM BuonoSconto b WHERE codiceSconto = '" + codiceSconto + "'").getSingleResult();
        }catch (NoResultException nre){
            //non faccio niente perché voglio che rimanga a null
        }
        return buonoSconto;
    }

    @Transactional
    public void eliminaBuonoScontoByCodice(String codiceSconto) throws PersistenceException {
        BuonoSconto buonoSconto = getSession().load(BuonoSconto.class, codiceSconto);
        getSession().delete(buonoSconto);
    }

    @Transactional
    public void salvaBuonoSconto(BuonoScontoDTO buonoScontoDTO) throws ValorePercentualeOltreCentoException{
        BuonoSconto buonoSconto = toEntity(buonoScontoDTO);
        getSession().saveOrUpdate(buonoSconto);
    }


    public static BuonoScontoDTO toDto(BuonoSconto buonoSconto){
        BuonoScontoDTO bsDTO = new BuonoScontoDTO();
        bsDTO.setCodiceSconto(buonoSconto.getCodiceSconto());
        bsDTO.setPercentuale(buonoSconto.isPercentuale());
        bsDTO.setValore(buonoSconto.getValore());
        if(buonoSconto.getPrenotazioni()!=null)
            bsDTO.setPrenotazioni(buonoSconto.getPrenotazioni());
        return bsDTO;
    }

    public static BuonoSconto toEntity(BuonoScontoDTO buonoScontoDTO) throws ValorePercentualeOltreCentoException {
        BuonoSconto buonoSconto = new BuonoSconto();
        buonoSconto.setCodiceSconto(buonoScontoDTO.getCodiceSconto());
        //importante impostare prima la percentuale, se no non funziona il controllo su valore
        buonoSconto.setPercentuale(buonoScontoDTO.isPercentuale());
        buonoSconto.setValore(buonoScontoDTO.getValore());
        if(buonoScontoDTO.getPrenotazioni()!=null)
            buonoSconto.setPrenotazioni(buonoScontoDTO.getPrenotazioni());
        return buonoSconto;
    }
}
