package app.service;

import app.DAO.MulteDAO;
import app.DAO.PrenotazioniDAO;
import app.DTO.MultaDTO;
import app.exceptions.prenotazione.DataNonCompresaNellaPrenotazioneException;
import app.model.Multa;
import app.model.Prenotazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MulteService {
    MulteDAO multeDAO;
    PrenotazioniDAO prenotazioniDAO;

    @Autowired(required = false)
    public MulteService(
            @Qualifier("multeDAO") MulteDAO multeDAO,
            @Qualifier("prenotazioniDAO") PrenotazioniDAO prenotazioniDAO ) {
        this.multeDAO = multeDAO;
        this.prenotazioniDAO = prenotazioniDAO;
    }

    public List<MultaDTO> selezionaMulteByCF(String codiceFiscale){
        List<Multa> multe = multeDAO.selezionaMulteByCF(codiceFiscale);
        List<MultaDTO> multeDTO = new ArrayList<>();
        for(Multa multa : multe) {
            try {
                MultaDTO mDTO = MulteDAO.toDto(multa);
                multeDTO.add(mDTO);
            } catch (NullPointerException npe) {
                //non fare nulla perché voglio mi ritorni null
            }
        }
        return multeDTO;
    }

    public boolean verificaUtenteMorosoByCF(String codiceFiscale){
        List<Multa> multe = multeDAO.selezionaMulteByCF(codiceFiscale);
        boolean utenteMoroso = false;
        for(Multa multa : multe) {
            if(multa!=null || multa.getDescrizione()!=null){
                utenteMoroso=true;
            }
        }
        return utenteMoroso;
    }

    public void salvaMulta(MultaDTO multaDTO) throws DataNonCompresaNellaPrenotazioneException {
        if(multaDTO.getDataProblema()!=null){
            LocalDate dataInizioPrenotazione = multaDTO.getPrenotazione().getDataInizio();
            LocalDate dataFinePrenotazione = multaDTO.getPrenotazione().getDataFine();
            if(multaDTO.getDataProblema().compareTo(dataInizioPrenotazione) < 0 ||
                    multaDTO.getDataProblema().compareTo(dataFinePrenotazione) > 0){
                throw new DataNonCompresaNellaPrenotazioneException();
            }else{
                multaDTO.setDataProblema(multaDTO.getDataProblema());
            }
        }

        multeDAO.salvaMulta(multaDTO);
    }
}
