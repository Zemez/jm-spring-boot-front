package com.javamentor.jm_spring_boot_front.controller;

import com.javamentor.jm_spring_boot_front.entity.Role;
import com.javamentor.jm_spring_boot_front.entity.User;
import com.javamentor.jm_spring_boot_front.service.RoleService;
import com.javamentor.jm_spring_boot_front.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String USER_NOT_FOUND = "User not found.";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public String user() {
        return "index";
    }
//    public String user(ModelMap model, Authentication auth) {
//        User user;
//        if (auth.isAuthenticated()) {
//                user = userService.findByUsername(auth.getName());
//            } else {
//                user = new User();
//            }
//        model.addAttribute("user", user);
//        return "user";
//    }

    @RequestMapping(path = "/update", method = {RequestMethod.POST, RequestMethod.PUT})
    public String update(HttpServletRequest request, Authentication auth, RedirectAttributes attributes) {
        try {
            User user = userService.findByUsername(auth.getName());
            logger.debug("User update: {}", user);
            if (user != null) {
                user = userService.update(user);
                if (user != null) {
                    attributes.addFlashAttribute("message", "User successful updated.");
                } else {
                    attributes.addFlashAttribute("error", "User update failed.");
                }
            } else {
                attributes.addFlashAttribute("error", USER_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(path = "/delete", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public String delete(HttpServletRequest request, Authentication auth, RedirectAttributes attributes) {
        String redirectUrl = "redirect:";
        try {
            User user = userService.findByUsername(auth.getName());
            logger.debug("User delete: {}", user);
            if (user != null) {
                userService.deleteById(user.getId());
                attributes.addFlashAttribute("message", "User successful deleted.");
                redirectUrl += "/";
                unAuthorize(request);
            } else {
                attributes.addFlashAttribute("error", USER_NOT_FOUND);
            }
        } catch (InvalidDataAccessApiUsageException e) {
            attributes.addFlashAttribute("error", "Attempt to delete a nonexistent user.");
            redirectUrl += request.getHeader("Referer");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", e.getMessage());
            redirectUrl += request.getHeader("Referer");
        }
        return redirectUrl;
    }

    @ModelAttribute("roleList")
    public List<Role> getRoleList() {
        return roleService.findAll();
    }

    private void unAuthorize(HttpServletRequest request) {
        request.getSession().removeAttribute("current_user");
        request.getSession(false).invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

}
