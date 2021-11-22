package org.rdd.distribution.spike.configuration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GatewayFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String proxyForwardedHostHeader = request.getHeader("X-Forwarded-Host");

        if (proxyForwardedHostHeader == null || !proxyForwardedHostHeader.equals("localhost:8881")) {
            ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
            ((HttpServletResponse) response).setStatus(401);
            response.getOutputStream().write("Unauthorized Access, you should pass through the API gateway".getBytes());
            return;
        }
        chain.doFilter(request, response);
    }
}
