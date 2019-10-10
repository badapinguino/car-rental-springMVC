package app.controller;

import app.service.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UtentiService utentiService;

    @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public String loginUtente(ModelMap model) {
        return "index";
    }

    @RequestMapping(value = {"/LogOut", "/logout", "/Logout"}, method = RequestMethod.GET)
    public String logoutUtente(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        session.removeAttribute("codiceFiscaleSessione");
        session.removeAttribute("superuserSessione");
        session.getMaxInactiveInterval();
//        return "../index";
        return "index";
    }

}
