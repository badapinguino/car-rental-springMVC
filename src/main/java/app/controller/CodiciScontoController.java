package app.controller;

import app.DTO.BuonoScontoDTO;
import app.service.BuoniScontoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.PersistenceException;
import java.util.List;

@Controller
public class CodiciScontoController {
    final private BuoniScontoService buoniScontoService;

    public CodiciScontoController(BuoniScontoService buoniScontoService) {
        this.buoniScontoService = buoniScontoService;
    }

    @RequestMapping(value = {"/codiciSconto" }, method = RequestMethod.GET)
    public String getMulteUtente(ModelMap model){
        List<BuonoScontoDTO> listaBuoniSconto = buoniScontoService.selezionaTuttiBuoniSconto();
        model.addAttribute("listaBuoniSconto", listaBuoniSconto);
        return "codiciSconto";
    }

    @RequestMapping(value = {"/codiciSconto" }, method = RequestMethod.POST)
    public String postMulteUtente(
            @RequestParam(value = "buonoScontoDaEliminare", required = false) String buonoScontoDaEliminare,
            @RequestParam(value = "codiceScontoDaEliminare", required = false) String codiceScontoDaEliminare,
            ModelMap model){
        if(buonoScontoDaEliminare!=null && buonoScontoDaEliminare.equals("true")){
            try {
                BuonoScontoDTO buonoScontoDTOEliminato = buoniScontoService.eliminaBuonoScontoByCodice(codiceScontoDaEliminare);
                model.addAttribute("buonoScontoDTOEliminato", buonoScontoDTOEliminato);
            }catch (PersistenceException pe){
                model.addAttribute("erroreChiaveEsterna", "Errore. Impossibile eliminare il buono sconto in quanto è già stato utilizzato in almeno una prenotazione.");
            }
        }

        List<BuonoScontoDTO> listaBuoniSconto = buoniScontoService.selezionaTuttiBuoniSconto();
        model.addAttribute("listaBuoniSconto", listaBuoniSconto);
        return "codiciSconto";
    }
}
