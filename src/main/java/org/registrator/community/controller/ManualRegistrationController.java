package org.registrator.community.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.registrator.community.entity.ResourceType;
import org.registrator.community.entity.TerritorialCommunity;
import org.registrator.community.forms.RegistrationForm;
import org.registrator.community.service.CommunityService;
import org.registrator.community.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ManualRegistrationController {

    @Autowired
    private Logger logger;
    @Autowired
    private UserService userService;
    @Autowired
    private CommunityService communityService;

    @PreAuthorize("hasRole('ROLE_COMMISSIONER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/manualregistration", method = RequestMethod.GET)
    public String showNewUserRegisterForm(Model model, HttpServletRequest request) {
        
        List<TerritorialCommunity> territorialCommunities = communityService.findAll();
        model.addAttribute("territorialCommunities", territorialCommunities);
        
        model.addAttribute("registrationForm", new RegistrationForm());
        logger.info("Loaded 'New user registration form' " + request.getRemoteAddr());
        return "regForComm";
    }

    @PreAuthorize("hasRole('ROLE_COMMISSIONER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/manualregistration", method = RequestMethod.POST)
    public String processNewUserData(@Valid RegistrationForm registrationForm, Errors result) {
        if (result.hasErrors()) {
            logger.warn("Registration form sent to server with following errors: \n" + result.getFieldErrors()
                    + "\n Error messages displayed to user.");
            return "regForComm";
        }
        userService.registerUser(registrationForm);

        logger.info("Successfully registered new user: " + registrationForm.getLogin());
        return "redirect:/";
    }
}