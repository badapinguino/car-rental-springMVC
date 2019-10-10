package app.controller;

import app.DTO.VeicoloDTO;
import app.service.VeicoliService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.PersistenceException;
import java.util.List;

@Controller
public class ParcoAutoController {
    final private VeicoliService veicoliService;

    public ParcoAutoController(VeicoliService veicoliService) {
        this.veicoliService = veicoliService;
    }

    @RequestMapping(value = "/parcoAuto", method = RequestMethod.GET)
    public String getParcoAuto(ModelMap model){
        List<VeicoloDTO> listaVeicoli = veicoliService.selezionaTuttiVeicoli();
        model.addAttribute("listaVeicoli", listaVeicoli);
        return "parcoAuto";
    }

    @RequestMapping(value = "/parcoAuto", method = RequestMethod.POST)
    public String postParcoAuto(@RequestParam("veicoloDaEliminare") String veicoloDaEliminare,
                    @RequestParam(value = "codiceMezzoDaEliminare", required = false) String codiceMezzoDaEliminare,
                    ModelMap model){
        if(veicoloDaEliminare!=null && veicoloDaEliminare.equals("true")){
            try {
                VeicoloDTO veicoloDTOEliminato = veicoliService.eliminaVeicoloByCodice(codiceMezzoDaEliminare);
                model.addAttribute("veicoloDTOEliminato", veicoloDTOEliminato);
            }catch (PersistenceException pe){
                model.addAttribute("erroreChiaveEsterna", "Errore. Impossibile eliminare il veicolo in quanto è già registrato in almeno una prenotazione.");
            }
        }

        List<VeicoloDTO> listaVeicoli = veicoliService.selezionaTuttiVeicoli();
        model.addAttribute("listaVeicoli", listaVeicoli);
        return "parcoAuto";
    }

}
