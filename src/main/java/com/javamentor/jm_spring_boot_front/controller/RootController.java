package com.javamentor.jm_spring_boot_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class RootController {

//    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String root() {
        return "index";
    }

}
