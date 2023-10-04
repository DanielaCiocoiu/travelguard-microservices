package com.mentoring.TravelInsurance.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException, ServletException {

    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    final Exception exception = (Exception) request.getAttribute("exception");

    String message;
    // If yes then use it to create the response message else use the authException
    if (exception != null) {

      byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", exception.toString()));
      response.getOutputStream().write(body);
    } else {
      if (authException.getCause() != null) {
        message = authException.getCause().toString() + " " + authException.getMessage();
      } else {
        message = authException.getMessage();
      }

      byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

      response.getOutputStream().write(body);
    }

/*    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);*/
  }

}
