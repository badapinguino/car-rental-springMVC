package app.service;

import app.DAO.BuoniScontoDAO;
import app.DTO.BuonoScontoDTO;
import app.exceptions.buonoSconto.ValorePercentualeOltreCentoException;
import app.model.BuonoSconto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuoniScontoService {
    BuoniScontoDAO buoniScontoDAO;

    @Autowired(required = false)
    public BuoniScontoService(@Qualifier("buoniScontoDAO") BuoniScontoDAO buoniScontoDAO) {
        this.buoniScontoDAO = buoniScontoDAO;
    }

    public List<BuonoScontoDTO> selezionaTuttiBuoniSconto() {
        List<BuonoSconto> buoniSconto = buoniScontoDAO.selezionaTuttiBuoniSconto();
        List<BuonoScontoDTO> buoniScontoDTO = new ArrayList<>();
        if(buoniSconto!=null){
            for (BuonoSconto buono : buoniSconto) {
                BuonoScontoDTO bDTO /*= new BuonoScontoDTO()*/;
                bDTO = BuoniScontoDAO.toDto(buono);
                buoniScontoDTO.add(bDTO);
            }
        }
        return buoniScontoDTO;
    }

    public BuonoScontoDTO eliminaBuonoScontoByCodice(String codiceSconto) throws PersistenceException {
        BuonoSconto buonoSconto = buoniScontoDAO.selezionaBuonoScontoByCodice(codiceSconto);
        BuonoScontoDTO bsDTO = null;
        if(buonoSconto!=null) {
            bsDTO = BuoniScontoDAO.toDto(buonoSconto);
            buoniScontoDAO.eliminaBuonoScontoByCodice(codiceSconto);
        }
        return bsDTO;
    }

    public BuonoScontoDTO selezionaBuonoScontoByCodice(String codiceSconto){
        BuonoSconto buonoSconto = buoniScontoDAO.selezionaBuonoScontoByCodice(codiceSconto);
        BuonoScontoDTO bsDTO = null;
        try {
            bsDTO = BuoniScontoDAO.toDto(buonoSconto);
        }catch(NullPointerException npe){
            //non fare nulla perché voglio mi ritorni null
        }
        return bsDTO;
    }

    public void salvaBuonoSconto(BuonoScontoDTO buonoScontoDTO) throws ValorePercentualeOltreCentoException {
        buoniScontoDAO.salvaBuonoSconto(buonoScontoDTO);
    }

}
