package app.controller;

import app.DAO.UtentiDAO;
import app.DTO.UtenteDTO;
import app.service.UtentiService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@MultipartConfig
@Controller
public class CreaModificaUtenteController {
    private final UtentiService utentiService;
    private final UtentiDAO utentiDAO;
    private final MessageSource messageSource;

    @Autowired(required = false)
    public CreaModificaUtenteController(@Qualifier("utentiService") UtentiService utentiService,
                                        @Qualifier("utentiDAO") UtentiDAO utentiDAO,
                                        @Qualifier("messageSource") MessageSource messageSource) {
        this.utentiService = utentiService;
        this.utentiDAO = utentiDAO;
        this.messageSource = messageSource;
    }

    @RequestMapping(value={"/creaModificaUtente"}, method = RequestMethod.GET)
    public String getCreaModificaUtente(HttpServletRequest req, ModelMap model) {
        HttpSession session = req.getSession();
        String codiceFiscaleUtenteDaModificare = req.getParameter("codiceFiscaleUtenteDaModificare");
        //controllo se sta cercando di modificare un superuser oppure se è l'utente che sta modificando i suoi dati
        UtenteDTO uDTO = new UtenteDTO();
        if(codiceFiscaleUtenteDaModificare!=null){
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) || (codiceFiscaleUtenteDaModificare.equals((String) session.getAttribute("codiceFiscaleSessione")))){
                uDTO = utentiService.selezionaUtenteByCFNoPassword(codiceFiscaleUtenteDaModificare);
                model.addAttribute("datiUtenteDaModificare", uDTO);
                if(uDTO==null || uDTO.getCodiceFiscale()==null || uDTO.getCodiceFiscale().equals("")){
                    uDTO = new UtenteDTO();
                }
            }
        }
        model.addAttribute("utenteDTO", uDTO);
        return "creaModificaUtente";
    }

    @RequestMapping(value={"/creaModificaUtente"}, method = RequestMethod.POST)
    public String postCreaModificaUtente(
            @RequestParam(value = "file", required = false) CommonsMultipartFile file,
            @RequestParam(value = "vecchioFile", required = false) String vecchioFile,
            @Valid UtenteDTO utenteDTO,
            BindingResult result,
            HttpServletRequest req,
            ModelMap model){
        HttpSession session = req.getSession();
        if (result.hasErrors()) {
            model.addAttribute("utenteInserito", false);
            return "creaModificaUtente";
        }
        try {
            if(utentiService.controllaSeSuperuserByCF( (String) session.getAttribute("codiceFiscaleSessione")) || (utenteDTO.getCodiceFiscale().equals((String) session.getAttribute("codiceFiscaleSessione")))) {

                if(file!=null && file.getSize()>0 && file.getSize()>0) {
                    String path = "/images/";
                    String filename = file.getOriginalFilename();
                    System.out.println(path  + filename);
                    File convFile = new File(path  + filename);
                    convFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(convFile);
                    fos.write(file.getBytes());
                    fos.close();
                    System.out.println(convFile.getName());

                    utenteDTO.setImmagine(path + filename);
                }else{
                    utenteDTO.setImmagine(vecchioFile);
                }

                try {
                    if (utenteDTO.getCodiceFiscale() != null && utentiService.controllaSeSuperuserByCF(utenteDTO.getCodiceFiscale())) {
                        utenteDTO.setSuperuser(true);
                        utentiDAO.salvaUtente(utenteDTO);
                    } else {
                        utenteDTO.setSuperuser(false);
                        utentiDAO.salvaUtente(utenteDTO);
                    }
                }catch (NullPointerException npe){
                    utenteDTO.setSuperuser(false);
                    utentiDAO.salvaUtente(utenteDTO);
                }
                model.addAttribute("utenteInserito", true);
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            model.addAttribute("utenteInserito", false);
            e.printStackTrace();
        }
        return "creaModificaUtente";
    }


    private String extractFileName(Part part){
        String contentDisp =  part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for(String s : items){
            if(s.trim().startsWith("filename")){
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }


    @RequestMapping(value = "/immagineProfilo", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@RequestParam("CFUtente") String CFUtente)  {
        UtenteDTO utenteDTO = utentiService.selezionaUtenteByCFNoPassword(CFUtente);
        Path path = Paths.get(utenteDTO.getImmagine());
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
