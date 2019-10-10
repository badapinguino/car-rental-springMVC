package app.controller;

import app.DTO.BuonoScontoDTO;
import app.DTO.VeicoloDTO;
import app.exceptions.buonoSconto.ValorePercentualeOltreCentoException;
import app.model.BuonoSconto;
import app.service.BuoniScontoService;
import app.service.UtentiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CreaModificaBuonoScontoController {
    final private UtentiService utentiService;
    private final BuoniScontoService buoniScontoService;

    public CreaModificaBuonoScontoController(UtentiService utentiService, BuoniScontoService buoniScontoService) {
        this.utentiService = utentiService;
        this.buoniScontoService = buoniScontoService;
    }

    @RequestMapping(value={"/creaModificaBuonoSconto"}, method = RequestMethod.GET)
    public String getCreaModificaVeicolo(
            @RequestParam(value = "codiceScontoDaModificare", required = false) String codiceScontoDaModificare,
            HttpServletRequest request,
            ModelMap model) {

        HttpSession session = request.getSession();
        //controllo se sta cercando di modificare un superuser
        if(codiceScontoDaModificare!=null){
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) ){
                BuonoScontoDTO bsDTO = buoniScontoService.selezionaBuonoScontoByCodice(codiceScontoDaModificare);
                model.addAttribute("datiBuonoScontoDaModificare", bsDTO);
                model.addAttribute("buonoScontoDTO", bsDTO);
            }
        }else{
            model.addAttribute("buonoScontoDTO", new BuonoScontoDTO());
        }

        return "creaModificaBuonoSconto";
    }

    @RequestMapping(value={"/creaModificaBuonoSconto"}, method = RequestMethod.POST)
    public String postCreaModificaVeicolo(
            @Valid BuonoScontoDTO buonoScontoDTO,
            BindingResult result,
            ModelMap model) {

        if(result.hasErrors()){
            model.addAttribute("buonoScontoInserito", false);
            return "creaModificaBuonoSconto";
        }

        try {
            buoniScontoService.salvaBuonoSconto(buonoScontoDTO);
            model.addAttribute("buonoScontoInserito", true);
        }catch(ValorePercentualeOltreCentoException vpoce){
            model.addAttribute("buonoScontoInserito", false);
            model.addAttribute("valorePercentualeOltreCento", true);
        }catch(Exception e){
            model.addAttribute("buonoScontoInserito", false);
            e.printStackTrace();
        }

        model.addAttribute("buonoScontoDTO", new BuonoScontoDTO());

        return "creaModificaBuonoSconto";
    }
}
