package app.controller;

import app.DTO.MultaDTO;
import app.DTO.UtenteDTO;
import app.service.MulteService;
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
public class MulteUtenteController {
    private final UtentiService utentiService;
    private final MulteService multeService;

    public MulteUtenteController(UtentiService utentiService, MulteService multeService) {
        this.utentiService = utentiService;
        this.multeService = multeService;
    }

    @RequestMapping(value = {"/multeUtente" }, method = RequestMethod.GET)
    public String getMulteUtente(
            @RequestParam("codiceFiscaleUtenteConMulte") String codiceFiscaleUtenteConMulte,
            HttpServletRequest req,
            ModelMap model){
        HttpSession session = req.getSession();
        //controllo se sta cercando di modificare un superuser oppure se è l'utente che sta visualizzando le sue prenotazioni
        if(codiceFiscaleUtenteConMulte!=null){
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) || (codiceFiscaleUtenteConMulte.equals((String) session.getAttribute("codiceFiscaleSessione")))){
                //richiesta dati multe
                List<MultaDTO> multeDTO = multeService.selezionaMulteByCF(codiceFiscaleUtenteConMulte);
                model.addAttribute("multeDTOUtente", multeDTO);
                // cerca l'utente e lo passa alla JSP per poter scrivere nome e cognome utente in cima alla tabella
                UtenteDTO utenteDTO = utentiService.selezionaUtenteByCFNoPassword(req.getParameter("codiceFiscaleUtenteConMulte"));
                model.addAttribute("utenteDTOMulte", utenteDTO);
            }
        }

        return "multeUtente";
    }
}
