package app.controller;

import app.DTO.PrenotazioneDTO;
import app.DTO.UtenteDTO;
import app.model.Utente;
import app.service.MulteService;
import app.service.PrenotazioniService;
import app.service.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    UtentiService utentiService;

    private final MessageSource messageSource;
    private final PrenotazioniService prenotazioniService;
    private final MulteService multeService;

    public HomePageController(MessageSource messageSource, PrenotazioniService prenotazioniService, MulteService multeService) {
        this.messageSource = messageSource;
        this.prenotazioniService = prenotazioniService;
        this.multeService = multeService;
    }

    @RequestMapping(value={"/homePage"}, method = RequestMethod.GET)
    public String getHomePage(
            @RequestParam(value = "utenteDaEliminare", required = false) String utenteDaEliminare,
            @RequestParam(value = "codiceFiscaleUtenteDaEliminare", required = false) String codiceFiscaleUtenteDaEliminare,
            ModelMap model,
            HttpServletRequest request,
            RedirectAttributes redir){
        if(utenteDaEliminare!=null && utenteDaEliminare.equals("true")){
            UtenteDTO utenteDTOEliminato = utentiService.eliminaUtenteByCF(codiceFiscaleUtenteDaEliminare);
            model.addAttribute("utenteDTOEliminato", utenteDTOEliminato);
        }

        model.addAttribute("multeService", multeService);
        HttpSession session = request.getSession();
        UtenteDTO utenteLoggato;
        String codiceFiscale = (String) session.getAttribute("codiceFiscaleSessione");
//        try{
            if(codiceFiscale==null) {
                String password = request.getParameter("password");
                codiceFiscale = request.getParameter("codiceFiscaleLogin");
                //controllo che esista l'utente con CF e pwd passati
                utenteLoggato = utentiService.loginUtente(codiceFiscale, password);
            }else{
                utenteLoggato = utentiService.selezionaUtenteByCFNoPassword(codiceFiscale);
            }
            if(utenteLoggato==null || utenteLoggato.getCognome()==null){
                model.addAttribute("messaggioErrore", "Errore, username o password sbagliati");
                return "creaModificaUtente";
            }else {
                session.setAttribute("codiceFiscaleSessione", codiceFiscale); //setto dopo il login nella sessione il codice fiscale dell'utente, lo eliminerò solo al logout
                String nome = utenteLoggato.getNome();
                String cognome = utenteLoggato.getCognome();
                boolean superuser = utenteLoggato.isSuperuser();
                model.addAttribute("nome", nome);
                model.addAttribute("cognome", cognome);
                session.setAttribute("superuserSessione", superuser); //setto dopo il login, nella sessione se l'utente è superuser, lo eliminerò solo al logout
                if (superuser) {
                    List<UtenteDTO> lista = utentiService.selezionaTuttiUtentiNoPassword();
                    model.addAttribute("listaUtenti", lista);
                    return "homePage";
                } else {
                    model.addAttribute("codiceFiscaleUtentePerPrenotazioni", codiceFiscale);
                    List<PrenotazioneDTO> prenotazioniDTO = prenotazioniService.selezionaPrenotazioniByCF(codiceFiscale);
                    model.addAttribute("prenotazioniDTOUtente", prenotazioniDTO);
                    UtenteDTO utenteDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscale);
                    model.addAttribute("utenteDTOPrenotazioni", utenteDTO);
                    return "prenotazioniUtente";
                }
            }
    }

    @RequestMapping(value={"/homePage"}, method = RequestMethod.POST)
    public String postHomePage(
            HttpServletRequest request,
            ModelMap model ){      /*, RedirectAttributes redir*/

        try{
            model.addAttribute("multeService", multeService);
            HttpSession session = request.getSession();
            UtenteDTO utenteLoggato;
            String codiceFiscale = (String) session.getAttribute("codiceFiscaleSessione");
            if(codiceFiscale==null) {
                String password = request.getParameter("password");
                codiceFiscale = request.getParameter("codiceFiscaleLogin");
                //controllo che esista l'utente con CF e pwd passati
                utenteLoggato = utentiService.loginUtente(codiceFiscale, password);
            }else{
                utenteLoggato = utentiService.selezionaUtenteByCFNoPassword(codiceFiscale);
            }
            if(utenteLoggato==null || utenteLoggato.getCognome()==null){
                model.addAttribute("messaggioErrore", "Errore, username o password sbagliati");
                return "creaModificaUtente";
            }else{
                session.setAttribute("codiceFiscaleSessione", codiceFiscale); //setto dopo il login nella sessione il codice fiscale dell'utente, lo eliminerò solo al logout
                String nome = utenteLoggato.getNome();
                String cognome = utenteLoggato.getCognome();
                boolean superuser = utenteLoggato.isSuperuser();
                model.addAttribute("nome", nome);
                model.addAttribute("cognome", cognome);
                session.setAttribute("superuserSessione", superuser); //setto dopo il login, nella sessione se l'utente è superuser, lo eliminerò solo al logout
                //RequestDispatcher requestDispatcher;
                if(superuser){
                    List<UtenteDTO> lista = utentiService.selezionaTuttiUtentiNoPassword();
                    model.addAttribute("listaUtenti", lista);
                    return "homePage";
                    //requestDispatcher.forward(req, resp);
                }else{
                    model.addAttribute("codiceFiscaleUtentePerPrenotazioni", codiceFiscale);
                    List<PrenotazioneDTO> prenotazioniDTO = prenotazioniService.selezionaPrenotazioniByCF(codiceFiscale);
                    model.addAttribute("prenotazioniDTOUtente", prenotazioniDTO);
                    UtenteDTO utenteDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscale);
                    model.addAttribute("utenteDTOPrenotazioni", utenteDTO);
                    return "prenotazioniUtente";
    //                requestDispatcher.include(req, resp);
                }
            }
        }catch (NoResultException nre){
            model.addAttribute("messaggioErrore", "Errore durante il login. Username o password errate");
            return "index";
        }catch (Exception e){
            model.addAttribute("messaggioErrore", "Si è verificato un errore.");
            return "index";
        }
    }
}
