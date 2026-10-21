package com.blogmanagement.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

/*
findAndRegisterModules(): Automatically finds and registers Jackson modules that are on the classpath.
 For example:
jackson-datatype-jsr310 → supports LocalDate, LocalDateTime, etc.
jackson-datatype-jdk8 → supports Optional
protected final: available to subclasses and does not change after construction
 (also preserves the thread-safety of ObjectMapper since it is a shared instance).
*/

    protected <T> T readRequest(
            HttpServletRequest request,
            Class<T> type) throws IOException {

        return objectMapper.readValue(request.getInputStream(), type);
    }

    protected void sendJson(
            HttpServletResponse response,
            int status,
            Object data) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), data);
    }
}
