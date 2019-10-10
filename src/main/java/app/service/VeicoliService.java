package app.service;

import app.DAO.VeicoliDAO;
import app.DTO.VeicoloDTO;
import app.exceptions.veicolo.AnnoFuturoException;
import app.model.Veicolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VeicoliService {
    @Autowired
    VeicoliDAO veicoliDAO;

    public VeicoloDTO selezionaVeicoloByCodice(String codiceMezzo){
        Veicolo veicolo = veicoliDAO.selezionaVeicoloByCodice(codiceMezzo);
        VeicoloDTO vDTO = null;
        try {
            vDTO = VeicoliDAO.toDto(veicolo);
        }catch(NullPointerException npe){
            //non fare nulla perché voglio mi ritorni null
        }
        return vDTO;
    }

    public List<VeicoloDTO> selezionaTuttiVeicoli(){
        List<Veicolo> veicoli = veicoliDAO.selezionaTuttiVeicoli();
        List<VeicoloDTO> veicoliDTO = new ArrayList<VeicoloDTO>();
        for (Veicolo veicolo : veicoli) {
            VeicoloDTO vDTO = VeicoliDAO.toDto(veicolo);
            veicoliDTO.add(vDTO);
        }
        return veicoliDTO;
    }

    public VeicoloDTO eliminaVeicoloByCodice(String codiceMezzo) throws PersistenceException {
        Veicolo veicolo = veicoliDAO.selezionaVeicoloByCodice(codiceMezzo);
        VeicoloDTO vDTO = null;
        if(veicolo!=null) {
            vDTO = new VeicoloDTO();
            vDTO.setCodiceMezzo(veicolo.getCodiceMezzo());
            vDTO.setTarga(veicolo.getTarga());
            vDTO.setCasaCostruttrice(veicolo.getCasaCostruttrice());
            vDTO.setModello(veicolo.getModello());
            vDTO.setCodiceMezzo(veicolo.getCodiceMezzo());
            veicoliDAO.eliminaVeicoloByCodice(codiceMezzo);
        }
        return vDTO;
    }

    public void salvaVeicolo(VeicoloDTO veicoloDTO) throws AnnoFuturoException {
        //salva l'oggetto (entity) Utente attraverso il DAO relativo
        veicoliDAO.salvaVeicolo(veicoloDTO);
    }
}
