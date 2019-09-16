package com.javamentor.jm_spring_boot_front.controller;

import com.javamentor.jm_spring_boot_front.entity.Role;
import com.javamentor.jm_spring_boot_front.entity.User;
import com.javamentor.jm_spring_boot_front.service.RoleService;
import com.javamentor.jm_spring_boot_front.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static void authorize(HttpServletRequest request, User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute("current_user", user);
    }

    private static void unAuthorize(HttpServletRequest request) {
        request.getSession().removeAttribute("current_user");
        request.getSession(false).invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    @RequestMapping(path = "/sign-up", method = RequestMethod.GET)
    public String signup(@ModelAttribute User user) {
        return "user";
    }

    @RequestMapping(path = "/sign-up", method = RequestMethod.POST)
    public String create(@ModelAttribute User user, HttpServletRequest request, RedirectAttributes attributes) {
        logger.debug("Create: {}", user);
        String redirectUrl = "redirect:";
        try {
            if (user.getRoles() == null) {
                user.setRoles(Collections.emptySet());
            }
            user = userService.create(user);
            logger.debug("Created: {}", user);
            if (user != null) {
                authorize(request, user);
                attributes.addFlashAttribute("message", "User successful registered.");
                if (user.isUser()) {
                    redirectUrl += "/user/" + user.getId();
                } else {
                    redirectUrl += "/";
                }
            } else {
                attributes.addFlashAttribute("error", "User register failed.");
                redirectUrl += request.getHeader("Referer");
            }
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("error", "Username is already taken.");
            redirectUrl += request.getHeader("Referer");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            redirectUrl += request.getHeader("Referer");
        }
        return redirectUrl;
    }

    @RequestMapping(path = "/sign-in", method = RequestMethod.GET)
    public String signin() {
        return "sign-in";
    }

    @ModelAttribute("roleList")
    public List<Role> getRoleList() {
        return roleService.findAll();
    }

}
