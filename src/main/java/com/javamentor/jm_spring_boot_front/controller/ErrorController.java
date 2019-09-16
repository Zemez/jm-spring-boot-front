package com.javamentor.jm_spring_boot_front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

//    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ModelAndView handleError(WebRequest webRequest) {
        return new ModelAndView("error", errorAttributes.getErrorAttributes(webRequest, true));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
