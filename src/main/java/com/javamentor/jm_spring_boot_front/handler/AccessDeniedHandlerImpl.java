package com.javamentor.jm_spring_boot_front.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("accessDeniedHandler")
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException, ServletException {
        String redirectUrl;
        logger.debug("AccessDeniedException: {}", ex.getMessage());
        request.getSession().setAttribute("error", ex.getMessage());
        if (request.getRequestURI().contains("/api/") || request.getHeader("Referer") == null) {
            redirectUrl = "/";
        } else {
            redirectUrl = request.getHeader("Referer");
        }
        response.sendRedirect(redirectUrl);
    }

}
