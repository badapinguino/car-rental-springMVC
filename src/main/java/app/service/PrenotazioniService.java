package app.service;

import app.DAO.BuoniScontoDAO;
import app.DAO.PrenotazioniDAO;
import app.DAO.VeicoliDAO;
import app.DTO.PrenotazioneDTO;
import app.exceptions.prenotazione.CodiceScontoInesistenteException;
import app.exceptions.prenotazione.DataInizioPiuGrandeDataFineException;
import app.exceptions.prenotazione.DataNonFuturaException;
import app.exceptions.prenotazione.VeicoloNonDisponibileDateSelezionateException;
import app.model.BuonoSconto;
import app.model.Prenotazione;
import app.model.Utente;
import app.model.Veicolo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioniService {
    private final PrenotazioniDAO prenotazioniDAO;
    private final VeicoliDAO veicoliDAO;
    private final MulteService multeService;
    private final BuoniScontoDAO buoniScontoDAO;

    public PrenotazioniService(PrenotazioniDAO prenotazioniDAO,
                               VeicoliDAO veicoliDAO,
                               MulteService multeService,
                               BuoniScontoDAO buoniScontoDAO) {
        this.prenotazioniDAO = prenotazioniDAO;
        this.veicoliDAO = veicoliDAO;
        this.multeService = multeService;
        this.buoniScontoDAO = buoniScontoDAO;
    }

    public List<PrenotazioneDTO> selezionaPrenotazioniByCF(String codiceFiscale){
        List<Prenotazione> prenotazioni = prenotazioniDAO.selezionaPrenotazioniByCF(codiceFiscale);
        List<PrenotazioneDTO> prenotazioniDTO = new ArrayList<>();
        for(Prenotazione prenotazione : prenotazioni){
            try {
                PrenotazioneDTO pDTO = PrenotazioniDAO.toDto(prenotazione);
                prenotazioniDTO.add(pDTO);
            }catch(NullPointerException npe){
                //non fare nulla perché voglio mi ritorni null
            }
        }
        return prenotazioniDTO;
    }

    public PrenotazioneDTO selezionaPrenotazioneByNumero(int numeroPrenotazione){
        Prenotazione prenotazione = prenotazioniDAO.selezionaPrenotazioneByNumero(numeroPrenotazione);
        PrenotazioneDTO pDTO = null;
        try {
            pDTO = PrenotazioniDAO.toDto(prenotazione);
        }catch(NullPointerException npe){
            //non fare nulla perché voglio mi ritorni null
        }
        return pDTO;
    }

    public LocalDate selezionaDataInizioByNumero(int numeroPrenotazione){
        Prenotazione prenotazione = prenotazioniDAO.selezionaPrenotazioneByNumero(numeroPrenotazione);
        LocalDate dataInizio = null;
        try {
            dataInizio = prenotazione.getDataInizio();
        }catch(NullPointerException npe){
            //non fare nulla perché voglio mi ritorni null
        }
        return dataInizio;
    }

    public LocalDate selezionaDataFineByNumero(int numeroPrenotazione){
        Prenotazione prenotazione = prenotazioniDAO.selezionaPrenotazioneByNumero(numeroPrenotazione);
        LocalDate dataFine = null;
        try {
            dataFine = prenotazione.getDataFine();
        }catch(NullPointerException npe){
            //non fare nulla perché voglio mi ritorni null
        }
        return dataFine;
    }


    public void salvaPrenotazione(PrenotazioneDTO prenotazioneDTO) throws DataInizioPiuGrandeDataFineException, CodiceScontoInesistenteException, VeicoloNonDisponibileDateSelezionateException, DataNonFuturaException {
        // controllo che le date non siano passate e che la data inizio sia antecedente alla data fine
        if(prenotazioneDTO.getDataInizio().compareTo(LocalDate.now())<=0){
            throw new DataNonFuturaException();
        }
        if(prenotazioneDTO.getDataFine().compareTo(LocalDate.now())<=0){
            throw new DataNonFuturaException();
        }
        if(prenotazioneDTO.getDataInizio().compareTo(prenotazioneDTO.getDataFine())>0){
            throw new DataInizioPiuGrandeDataFineException();
        }

        //potrebbe verificarsi una eccezione (null pointer) se non esiste veicolo con quel codice mezzo
        Veicolo veicolo = veicoliDAO.selezionaVeicoloByCodice(prenotazioneDTO.getVeicolo().getCodiceMezzo());
        boolean controlloDataInizio = prenotazioniDAO.esistePrenotazioneDataVeicolo(prenotazioneDTO.getDataInizio(),
                veicolo, prenotazioneDTO.getNumero());
        boolean controlloDataFine = prenotazioniDAO.esistePrenotazioneDataVeicolo(prenotazioneDTO.getDataFine(),
                veicolo, prenotazioneDTO.getNumero());
        List<Prenotazione> prenotazioniVeicolo =
                prenotazioniDAO.selezionaPrenotazioniByCodiceMezzo(prenotazioneDTO.getVeicolo().getCodiceMezzo());
        boolean controlloNelMezzo = false;
        for (Prenotazione p : prenotazioniVeicolo ) {
            if( p.getNumero()!=prenotazioneDTO.getNumero() && (((p.getDataInizio()).compareTo(prenotazioneDTO.getDataInizio()) >= 0 && p.getDataInizio().compareTo(prenotazioneDTO.getDataFine()) <= 0) || (p.getDataFine().compareTo(prenotazioneDTO.getDataInizio()) >= 0 && p.getDataFine().compareTo(prenotazioneDTO.getDataFine()) <= 0))){
                controlloNelMezzo = true;
            }
        }
        if( !controlloDataInizio && !controlloDataFine && !controlloNelMezzo){
            // se tutti i controlli sono falsi procedere con inserimento della prenotazione
            if(multeService.verificaUtenteMorosoByCF(prenotazioneDTO.getUtente().getCodiceFiscale())){
                prenotazioneDTO.setApprovata(false);
            }else{
                prenotazioneDTO.setApprovata(true);
            }
            if(prenotazioneDTO.getBuonoSconto()!=null) {
                BuonoSconto buonoSconto = buoniScontoDAO.selezionaBuonoScontoByCodice(prenotazioneDTO.getBuonoSconto().getCodiceSconto());
                if (buonoSconto != null) {
                    prenotazioneDTO.setBuonoSconto(buonoSconto);
                } else {
                    throw new CodiceScontoInesistenteException();
                }
            }
            prenotazioniDAO.salvaPrenotazione(prenotazioneDTO);
        }else{
            throw new VeicoloNonDisponibileDateSelezionateException();
        }
    }
}
