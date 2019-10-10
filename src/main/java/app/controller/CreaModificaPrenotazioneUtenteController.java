package app.controller;

import app.DAO.BuoniScontoDAO;
import app.DAO.UtentiDAO;
import app.DAO.VeicoliDAO;
import app.DTO.PrenotazioneDTO;
import app.DTO.VeicoloDTO;
import app.exceptions.prenotazione.CodiceScontoInesistenteException;
import app.exceptions.prenotazione.DataInizioPiuGrandeDataFineException;
import app.exceptions.prenotazione.DataNonFuturaException;
import app.exceptions.prenotazione.VeicoloNonDisponibileDateSelezionateException;
import app.exceptions.veicolo.AnnoFuturoException;
import app.model.BuonoSconto;
import app.model.Utente;
import app.model.Veicolo;
import app.service.PrenotazioniService;
import app.service.VeicoliService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
public class CreaModificaPrenotazioneUtenteController {
    private final VeicoliService veicoliService;
    private final PrenotazioniService prenotazioniService;
    private final UtentiDAO utentiDAO;
    private final BuoniScontoDAO buoniScontoDAO;
    private final VeicoliDAO veicoliDAO;

    public CreaModificaPrenotazioneUtenteController(VeicoliService veicoliService,
                                                    PrenotazioniService prenotazioniService,
                                                    UtentiDAO utentiDAO,
                                                    BuoniScontoDAO buoniScontoDAO,
                                                    VeicoliDAO veicoliDAO) {
        this.veicoliService = veicoliService;
        this.prenotazioniService = prenotazioniService;
        this.utentiDAO = utentiDAO;
        this.buoniScontoDAO = buoniScontoDAO;
        this.veicoliDAO = veicoliDAO;
    }

    @RequestMapping(value={"/creaModificaPrenotazioneUtente"}, method = RequestMethod.GET)
    public String getCreaModificaPrenotazioneUtente(
            @RequestParam("codiceFiscaleUtentePrenotazione") String codiceFiscaleUtentePrenotazione,
            @RequestParam(value = "numeroPrenotazioneDaModificare", required = false) String numeroPrenotazioneDaModificareString,
            ModelMap model) {
        List<VeicoloDTO> veicoliDTO = veicoliService.selezionaTuttiVeicoli();
        for ( VeicoloDTO vDTO : veicoliDTO ) {
            vDTO.setDettagli(vDTO.getCasaCostruttrice() + " " + vDTO.getModello() + ". Tipologia: " + vDTO.getTipologia() + ". Al prezzo di " + vDTO.getPrezzoGiornata() + "€ al giorno.");
        }
        model.addAttribute("listaVeicoliDTO", veicoliDTO);
        model.addAttribute("codiceFiscaleUtentePrenotazione", codiceFiscaleUtentePrenotazione);
        Utente utentePrenotazione = utentiDAO.selezionaUtenteByCF(codiceFiscaleUtentePrenotazione);
        PrenotazioneDTO prenotazioneDTO;
        if(numeroPrenotazioneDaModificareString!=null && !numeroPrenotazioneDaModificareString.equals("")){
            model.addAttribute("numeroPrenotazioneDaModificare", numeroPrenotazioneDaModificareString);
            prenotazioneDTO = prenotazioniService.selezionaPrenotazioneByNumero(Integer.parseInt(numeroPrenotazioneDaModificareString));
            if(prenotazioneDTO.getBuonoSconto()!=null) {
                prenotazioneDTO.setCodiceSconto(prenotazioneDTO.getBuonoSconto().getCodiceSconto());
            }
        }else{
            prenotazioneDTO = new PrenotazioneDTO();
            prenotazioneDTO.setUtente(utentePrenotazione);
        }
        model.addAttribute("prenotazioneDTO", prenotazioneDTO);

        return "creaModificaPrenotazioneUtente";
    }

    @RequestMapping(value = {"/creaModificaPrenotazioneUtente"}, method = RequestMethod.POST)
    public String postCreaModificaPrenotazioneUtente(
            @RequestParam("codiceFiscaleUtentePrenotazione") String codiceFiscaleUtentePrenotazione,
            @Valid PrenotazioneDTO prenotazioneDTO,
            BindingResult result, /*HttpServletRequest req,*/
            ModelMap model){

        if (result.hasErrors()) {
            model.addAttribute("prenotazioneInserita", false);
            return "creaModificaPrenotazioneUtente";
        }
        try {
            Utente utente = utentiDAO.selezionaUtenteByCF(prenotazioneDTO.getCodiceFiscale());
            prenotazioneDTO.setUtente(utente);
            if(prenotazioneDTO.getCodiceSconto()!=null && !prenotazioneDTO.getCodiceSconto().equals("")){
                BuonoSconto buonoSconto = buoniScontoDAO.selezionaBuonoScontoByCodice(prenotazioneDTO.getCodiceSconto());
                if(buonoSconto!=null) {
                    prenotazioneDTO.setBuonoSconto(buonoSconto);
                }else{
                    throw new CodiceScontoInesistenteException();
                }
            }
            Veicolo veicolo = veicoliDAO.selezionaVeicoloByCodice(prenotazioneDTO.getCodiceMezzo());
            prenotazioneDTO.setVeicolo(veicolo);
            prenotazioniService.salvaPrenotazione(prenotazioneDTO);
            //calcolo costo totale prenotazione e valore buoni sconto
            float costoTotalePrenotazione = prenotazioneDTO.getVeicolo().getPrezzoGiornata() *
                    (DAYS.between(prenotazioneDTO.getDataInizio(), prenotazioneDTO.getDataFine())+1);
            float costoFinalePrenotazione = costoTotalePrenotazione;
            if(prenotazioneDTO.getBuonoSconto()!=null && prenotazioneDTO.getCodiceSconto()!=""){
                float valoreCodiceSconto = prenotazioneDTO.getBuonoSconto().getValore();
                boolean percentualeCodiceSconto = prenotazioneDTO.getBuonoSconto().isPercentuale();
                if(percentualeCodiceSconto){
                    costoFinalePrenotazione = costoTotalePrenotazione - ( costoTotalePrenotazione * (valoreCodiceSconto/100) );
                }else{
                    costoFinalePrenotazione = costoTotalePrenotazione - valoreCodiceSconto;
                }
                model.addAttribute("valoreCodiceSconto", valoreCodiceSconto);
                model.addAttribute("percentualeCodiceSconto", percentualeCodiceSconto);
            }
            model.addAttribute("costoTotalePreScontoPrenotazione", costoTotalePrenotazione);
            model.addAttribute("costoFinalePrenotazione", costoFinalePrenotazione);

            model.addAttribute("prenotazioneInserita", true);
        } catch (VeicoloNonDisponibileDateSelezionateException vnddse) {
            model.addAttribute("prenotazioneInserita", false);
            model.addAttribute("erroreVeicoloNonDisponibile", true);
        } catch (DataInizioPiuGrandeDataFineException dipgdfe) {
            model.addAttribute("prenotazioneInserita", false);
            model.addAttribute("erroreDataInizioMaggioreDataFine", true);
        } catch (DataNonFuturaException dnfe) {
            model.addAttribute("prenotazioneInserita", false);
            model.addAttribute("erroreDataNonFutura", true);
        } catch (CodiceScontoInesistenteException csie) {
            model.addAttribute("prenotazioneInserita", false);
            model.addAttribute("erroreCodiceScontoInesistente", true);
        } catch (Exception e){
            model.addAttribute("prenotazioneInserita", false);
            e.printStackTrace();
        }

//        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/creaModificaPrenotazioneUtente.jsp");
//        requestDispatcher.forward(req, resp);

        List<VeicoloDTO> veicoliDTO = veicoliService.selezionaTuttiVeicoli();
        for ( VeicoloDTO vDTO : veicoliDTO ) {
            vDTO.setDettagli(vDTO.getCasaCostruttrice() + " " + vDTO.getModello() + ". Tipologia: " + vDTO.getTipologia() + ". Al prezzo di " + vDTO.getPrezzoGiornata() + "€ al giorno.");
        }
        model.addAttribute("listaVeicoliDTO", veicoliDTO);
        model.addAttribute("codiceFiscaleUtentePrenotazione", codiceFiscaleUtentePrenotazione);
        Utente utentePrenotazione = utentiDAO.selezionaUtenteByCF(codiceFiscaleUtentePrenotazione);
        PrenotazioneDTO prenotazioneDTONuova;
            prenotazioneDTONuova = new PrenotazioneDTO();
            prenotazioneDTONuova.setUtente(utentePrenotazione);
        model.addAttribute("prenotazioneDTO", prenotazioneDTONuova);

        return "creaModificaPrenotazioneUtente";
    }
}
