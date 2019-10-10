package app.controller;

import app.DAO.PrenotazioniDAO;
import app.DTO.MultaDTO;
import app.DTO.PrenotazioneDTO;
import app.DTO.UtenteDTO;
import app.exceptions.prenotazione.DataNonCompresaNellaPrenotazioneException;
import app.model.Prenotazione;
import app.service.MulteService;
import app.service.PrenotazioniService;
import app.service.UtentiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CreaMultaUtenteController {
    private final UtentiService utentiService;
    private final PrenotazioniService prenotazioniService;
    private final PrenotazioniDAO prenotazioniDAO;
    private final MulteService multeService;

    public CreaMultaUtenteController(UtentiService utentiService,
                                     PrenotazioniService prenotazioniService,
                                     PrenotazioniDAO prenotazioniDAO,
                                     MulteService multeService) {
        this.utentiService = utentiService;
        this.prenotazioniService = prenotazioniService;
        this.prenotazioniDAO = prenotazioniDAO;
        this.multeService = multeService;
    }

    @RequestMapping(value = "/creaMultaUtente", method = RequestMethod.GET)
    public String getCreaMultaUtente(
            @RequestParam(value = "codiceFiscaleUtenteDaMultare") String codiceFiscaleUtenteDaMultare,
            HttpServletRequest request,
            ModelMap model){

        model.addAttribute("codiceFiscaleUtenteDaMultare", codiceFiscaleUtenteDaMultare);

        HttpSession session = request.getSession();
        //controllo se sta cercando di modificare un superuser oppure se è l'utente che sta modificando i suoi dati
        if(codiceFiscaleUtenteDaMultare!=null){
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) ){
                UtenteDTO uDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscaleUtenteDaMultare);
                model.addAttribute("utenteDTODaMultare", uDTO);
                //cerco le prenotazioni dell'utente e le passo alla pagina delle multe
                List<PrenotazioneDTO> prenotazioniDTO = prenotazioniService.selezionaPrenotazioniByCF(codiceFiscaleUtenteDaMultare);
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                List<PrenotazioneDTO> prenotazioniDTOConDataPassata = new ArrayList<>();
                for ( PrenotazioneDTO pDTO : prenotazioniDTO ) {
                    pDTO.setDettagli("Numero prenotazione: " + pDTO.getNumero() + ". Veicolo: " + pDTO.getVeicolo().getCasaCostruttrice() + " " + pDTO.getVeicolo().getModello() + ". Dal: " + pDTO.getDataInizio().format(pattern) + " al: " + pDTO.getDataFine().format(pattern));
                    if(pDTO.getDataFine().compareTo(LocalDate.now())<0){
                        prenotazioniDTOConDataPassata.add(pDTO);
                    }
                }
                model.addAttribute("prenotazioniDTOUtenteDaMultare", prenotazioniDTOConDataPassata);
                model.addAttribute("multaDTO", new MultaDTO());
            }
        }

        return "creaMultaUtente";
    }

    @RequestMapping(value = "/creaMultaUtente", method = RequestMethod.POST)
    public String postCreaMultaUtente(
            @RequestParam(value = "codiceFiscaleUtenteDaMultare", required = false) String codiceFiscaleUtenteDaMultare,
            @Valid MultaDTO multaDTO,
            BindingResult result,
            HttpServletRequest request,
            ModelMap model){
        if (result.hasErrors()) {
            model.addAttribute("multaInserita", false);
            return "creaMultaUtente";
        }

        String numeroPrenotazione = multaDTO.getNumeroPrenotazione();
        Prenotazione prenotazione = prenotazioniDAO.selezionaPrenotazioneByNumero(Integer.parseInt(numeroPrenotazione));
        multaDTO.setPrenotazione(prenotazione);

        try {
            multeService.salvaMulta(multaDTO);
            model.addAttribute("multaInserita", true);
        }catch(DataNonCompresaNellaPrenotazioneException dncnpe){
            model.addAttribute("multaInserita", false);
            model.addAttribute("erroreDataNonCompresa", true);
        }catch(Exception e){
            model.addAttribute("multaInserita", false);
            e.printStackTrace();
        }
        model.addAttribute("codiceFiscaleUtenteDaMultare", codiceFiscaleUtenteDaMultare);


        HttpSession session = request.getSession();
        //controllo se sta cercando di modificare un superuser oppure se è l'utente che sta modificando i suoi dati
        if(codiceFiscaleUtenteDaMultare!=null){
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) ){
                UtenteDTO uDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscaleUtenteDaMultare);
                model.addAttribute("utenteDTODaMultare", uDTO);
                //cerco le prenotazioni dell'utente e le passo alla pagina delle multe
                List<PrenotazioneDTO> prenotazioniDTO = prenotazioniService.selezionaPrenotazioniByCF(codiceFiscaleUtenteDaMultare);
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                List<PrenotazioneDTO> prenotazioniDTOConDataPassata = new ArrayList<>();
                for ( PrenotazioneDTO pDTO : prenotazioniDTO ) {
                    pDTO.setDettagli("Numero prenotazione: " + pDTO.getNumero() + ". Veicolo: " + pDTO.getVeicolo().getCasaCostruttrice() + " " + pDTO.getVeicolo().getModello() + ". Dal: " + pDTO.getDataInizio().format(pattern) + " al: " + pDTO.getDataFine().format(pattern));
                    if(pDTO.getDataFine().compareTo(LocalDate.now())<0){
                        prenotazioniDTOConDataPassata.add(pDTO);
                    }
                }
                model.addAttribute("prenotazioniDTOUtenteDaMultare", prenotazioniDTOConDataPassata);
                model.addAttribute("multaDTO", new MultaDTO());
            }
        }

        return "creaMultaUtente";
    }
}
