package com.javamentor.jm_spring_boot_front.controller;

import com.javamentor.jm_spring_boot_front.entity.Role;
import com.javamentor.jm_spring_boot_front.entity.User;
import com.javamentor.jm_spring_boot_front.service.RoleService;
import com.javamentor.jm_spring_boot_front.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String USER_NOT_FOUND = "User not found.";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public String admin(ModelMap model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        return "admin";
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String readById(@PathVariable(name = "id") Long id, ModelMap model) {
        try {
            User user = userService.findById(id);
            logger.debug("User: {}", user);
            if (user == null) {
                user = new User();
                model.addAttribute("error", USER_NOT_FOUND);
            }
            model.addAttribute(user);
        } catch (EmptyResultDataAccessException e) {
            model.addAttribute("user", new User());
            model.addAttribute("error", USER_NOT_FOUND);
        }
        return "user::user-form";
    }

    @RequestMapping(path = "/name/{username}", method = RequestMethod.GET)
    public String readByUsername(@PathVariable(name = "username") String username, ModelMap model) {
        try {
            logger.debug("Username: {}", username);
            User user = userService.findByUsername(username);
            logger.debug("User: {}", user);
            model.addAttribute(user);
        } catch (EmptyResultDataAccessException e) {
            model.addAttribute(new User());
            model.addAttribute("error", USER_NOT_FOUND);
        }
        return "user::user-form";
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public String readAll(ModelMap model) {
        try {
            List<User> users = userService.findAll();
//            logger.debug("Users: {}", users);
            model.addAttribute("users", users);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            model.addAttribute("users", Collections.EMPTY_LIST);
            model.addAttribute("error", e.getMessage());
        }
        return "users::users";
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public String create(@ModelAttribute User user) {
        return "user::user-form-full";
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute User user, HttpServletRequest request, RedirectAttributes attributes) {
        logger.debug("Create: {}", user);
        try {
            user = userService.create(user);
            if (user != null) {
                attributes.addFlashAttribute("message", "User successful created.");
            } else {
                attributes.addFlashAttribute("error", "User create failed.");
            }
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("error", "Username is already taken.");
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(path = "/update", method = {RequestMethod.POST, RequestMethod.PUT})
    public String update(@ModelAttribute User user, HttpServletRequest request, RedirectAttributes attributes) {
        logger.debug("Update: {}", user);
        try {
            user = userService.update(user);
            if (user == null) {
                attributes.addFlashAttribute("error", "User update failed.");
            } else {
                attributes.addFlashAttribute("message", "User successful updated.");
            }
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(path = "/delete", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    public String delete(@RequestParam Long id, HttpServletRequest request, Authentication auth, RedirectAttributes attributes) {
        logger.debug("Delete: {}", id);
        try {
            User user = userService.findById(id);
            if (user != null) {
                userService.deleteById(id);
                attributes.addFlashAttribute("message", "User successful deleted.");
            } else {
                attributes.addFlashAttribute("error", "Attempt to delete a nonexistent user.");
            }
        } catch (InvalidDataAccessApiUsageException e) {
            attributes.addFlashAttribute("error", "Attempt to delete a nonexistent user.");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @ModelAttribute("roleList")
    public List<Role> getRoleList() {
        return roleService.findAll();
    }

}
