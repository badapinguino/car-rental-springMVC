package app.controller;

import app.DAO.PrenotazioniDAO;
import app.DTO.PrenotazioneDTO;
import app.DTO.UtenteDTO;
import app.service.MulteService;
import app.service.PrenotazioniService;
import app.service.UtentiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PrenotazioniUtenteController {
    private final PrenotazioniService prenotazioniService;
    private final UtentiService utentiService;
    private final MulteService multeService;
    private final PrenotazioniDAO prenotazioniDAO;

    public PrenotazioniUtenteController(PrenotazioniService prenotazioniService,
                                        UtentiService utentiService,
                                        MulteService multeService, PrenotazioniDAO prenotazioniDAO) {
        this.prenotazioniService = prenotazioniService;
        this.utentiService = utentiService;
        this.multeService = multeService;
        this.prenotazioniDAO = prenotazioniDAO;
    }

    @RequestMapping(value = "/prenotazioniUtente", method = RequestMethod.GET)
    public String getPrenotazioniUtente(
            @RequestParam(value = "codiceFiscaleUtentePerPrenotazioni", required = false) String codiceFiscaleUtentePerPrenotazioni,
            HttpServletRequest request,
            ModelMap model){
        model.addAttribute("multeService", multeService);
        //prendo il CF e lo inserisco di nuovo perché serve nei form per le post di approvazione ed eliminazione
        model.addAttribute("codiceFiscaleUtentePerPrenotazioni", codiceFiscaleUtentePerPrenotazioni);
        HttpSession session = request.getSession();
        String codiceFiscaleSessione = (String) session.getAttribute("codiceFiscaleSessione");
        //controllo se sta cercando di modificare un superuser oppure se è l'utente che sta visualizzando le sue prenotazioni
        if(codiceFiscaleUtentePerPrenotazioni!=null){
            if(utentiService.controllaSeSuperuserByCF(codiceFiscaleSessione) || (codiceFiscaleUtentePerPrenotazioni.equals(codiceFiscaleSessione))){
                //richiesta dati prenotazioni
                List<PrenotazioneDTO> prenotazioniDTO = prenotazioniService.selezionaPrenotazioniByCF(codiceFiscaleUtentePerPrenotazioni);
                model.addAttribute("prenotazioniDTOUtente", prenotazioniDTO);
                // cerca l'utente e lo passa alla JSP per poter scrivere nome e cognome utente in cima alla tabella
                UtenteDTO utenteDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscaleUtentePerPrenotazioni);
                model.addAttribute("utenteDTOPrenotazioni", utenteDTO);
            }
        }
        return "prenotazioniUtente";
    }

    @RequestMapping(value = "/prenotazioniUtente", method = RequestMethod.POST)
    public String postPrenotazioniUtente(
            @RequestParam(value = "numeroPrenotazioneDaApprovare", required = false) String numeroPrenotazioneDaApprovare,
            @RequestParam(value = "numeroPrenotazioneDaEliminare", required = false) String numeroPrenotazioneDaEliminare,
            @RequestParam("codiceFiscaleUtentePerPrenotazioni") String codiceFiscaleUtentePerPrenotazioni,
            HttpServletRequest request,
            ModelMap model){
        if(numeroPrenotazioneDaApprovare!=null && !numeroPrenotazioneDaApprovare.equals("")){
            //approva la prenotazione e visualizza un messaggio nella jsp
            prenotazioniDAO.approvaLaPrenotazione(Integer.parseInt(numeroPrenotazioneDaApprovare));
            //aggiungere un messaggio da visualizzare nella JSP
            model.addAttribute("prenotazioneApprovata", true);
        }
        if(numeroPrenotazioneDaEliminare!=null && !numeroPrenotazioneDaEliminare.equals("")){
            //elimina la prenotazione e visualizza un messaggio nella jsp
            prenotazioniDAO.eliminaPrenotazioneByNumero(Integer.parseInt(numeroPrenotazioneDaEliminare));
            //aggiungere un messaggio da visualizzare nella JSP
            model.addAttribute("prenotazioneEliminata", true);
        }


        model.addAttribute("multeService", multeService);
        //prendo il CF e lo inserisco di nuovo perché serve nei form per le post di approvazione ed eliminazione
        //se non prendo e riaggiungo l'attributo CF mi perde l'utente, quindi nella pagina stampa prenotazioni per l'utente null null
        model.addAttribute("codiceFiscaleUtentePerPrenotazioni", codiceFiscaleUtentePerPrenotazioni);
        HttpSession session = request.getSession();
        String codiceFiscaleSessione = (String) session.getAttribute("codiceFiscaleSessione");
        //controllo se sta cercando di modificare un superuser oppure se è l'utente che sta visualizzando le sue prenotazioni
        if(codiceFiscaleUtentePerPrenotazioni!=null){
            if(utentiService.controllaSeSuperuserByCF(codiceFiscaleSessione) || (codiceFiscaleUtentePerPrenotazioni.equals(codiceFiscaleSessione))){
                //richiesta dati prenotazioni
                List<PrenotazioneDTO> prenotazioniDTO = prenotazioniService.selezionaPrenotazioniByCF(codiceFiscaleUtentePerPrenotazioni);
                model.addAttribute("prenotazioniDTOUtente", prenotazioniDTO);
                // cerca l'utente e lo passa alla JSP per poter scrivere nome e cognome utente in cima alla tabella
                UtenteDTO utenteDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscaleUtentePerPrenotazioni);
                model.addAttribute("utenteDTOPrenotazioni", utenteDTO);
            }
        }
        return "prenotazioniUtente";
    }
}
