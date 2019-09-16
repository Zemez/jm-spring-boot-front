package com.javamentor.jm_spring_boot_front.handler;

import com.javamentor.jm_spring_boot_front.entity.User;
import com.javamentor.jm_spring_boot_front.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerImpl extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private UserService userService;

    public AuthenticationSuccessHandlerImpl() {
    }

    public AuthenticationSuccessHandlerImpl(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
        handle(request, response, auth);
        clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            msg = "You are signed in successfully.";
        }
        request.getSession().setAttribute("message", msg);

        User user = userService.findByUsername(auth.getName());
        request.getSession().setAttribute("current_user", user);
        logger.debug("current_user: {}", request.getSession().getAttribute("current_user"));

        if (!response.isCommitted()) {
            String targetUrl;
            if (user.isAdmin()) {
                targetUrl = "/admin";
            } else if (user.isUser()) {
                targetUrl = "/user";
            } else {
                targetUrl = determineTargetUrl(request, response);
            }
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
