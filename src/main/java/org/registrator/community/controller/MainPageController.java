package org.registrator.community.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainPageController {
/*
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage() {
        return "index";
    }
*/
    @RequestMapping("/")
    public String welcome(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (("anonymousUser").equals(auth.getName())){
            return "redirect:/login";
        }
        else {
            return "homepage";
        }
    }
}