package com.javamentor.jm_spring_boot_front.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            msg = "You are signed out successfully.";
        }
        request.getSession().removeAttribute("current_user");
        request.getSession().setAttribute("message", msg);
        response.sendRedirect("/auth/sign-in?logout");
    }

}
