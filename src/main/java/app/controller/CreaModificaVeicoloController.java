package app.controller;

import app.DTO.VeicoloDTO;
import app.exceptions.veicolo.AnnoFuturoException;
import app.service.UtentiService;
import app.service.VeicoliService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CreaModificaVeicoloController {
    private final UtentiService utentiService;
    private final VeicoliService veicoliService;

    public CreaModificaVeicoloController(UtentiService utentiService, VeicoliService veicoliService) {
        this.utentiService = utentiService;
        this.veicoliService = veicoliService;
    }

    @RequestMapping(value={"/creaModificaVeicolo"}, method = RequestMethod.GET)
    public String getCreaModificaVeicolo(
            @RequestParam(value = "codiceMezzoDaModificare", required = false) String codiceMezzoDaModificare,
            HttpServletRequest request,
            ModelMap model) {

        HttpSession session = request.getSession();
        //controllo se sta cercando di modificare un superuser
        if(codiceMezzoDaModificare!=null){
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) ){
                VeicoloDTO vDTO = veicoliService.selezionaVeicoloByCodice(codiceMezzoDaModificare);
                model.addAttribute("datiVeicoloDaModificare", vDTO); //per controlli se si è in modifica oppure no
                model.addAttribute("veicoloDTO", vDTO);
            }
        }else{
            model.addAttribute("veicoloDTO", new VeicoloDTO());
        }

        return "creaModificaVeicolo";
    }


    @RequestMapping(value={"/creaModificaVeicolo"}, method = RequestMethod.POST)
    public String postCreaModificaVeicolo(
            @Valid VeicoloDTO veicoloDTO,
            BindingResult result,
            HttpServletRequest request,
            ModelMap model) {

        if (result.hasErrors()) {
            model.addAttribute("veicoloInserito", false);
            return "creaModificaVeicolo";
        }

        try {
            veicoliService.salvaVeicolo(veicoloDTO);
            model.addAttribute("veicoloInserito", true);
        }catch (AnnoFuturoException afe) {
            model.addAttribute("veicoloInserito", false);
            model.addAttribute("erroreAnnoFuturo", true);
        }catch(Exception e){
            model.addAttribute("veicoloInserito", false);
            e.printStackTrace();
        }

        return "creaModificaVeicolo";
    }
}