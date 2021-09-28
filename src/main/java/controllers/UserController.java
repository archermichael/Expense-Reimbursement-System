package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Response;
import models.User;
import services.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class UserController {
    private static UserController userController;
    UserService userService;

    private UserController(){
        userService = new UserService();
    }

    public static UserController getInstance(){
        if (userController == null)
            userController = new UserController();

        return userController;
    }

    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = new ObjectMapper().readValue(requestBody, User.class);

        User tempUser = userService.login(user);

        if(tempUser != null){
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("userObj", tempUser);

            out.println(new ObjectMapper().writeValueAsString(new Response("Logged in!", true, tempUser)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("Login failed.", false, null)));
        }
    }

    public void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, MessagingException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = new ObjectMapper().readValue(requestBody, User.class);

        User tempUser = userService.register(user);

        if(tempUser != null){
            out.println(new ObjectMapper().writeValueAsString(new Response("User created!", true, tempUser)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("Unable to create user.", false, null)));
        }
    }

    public void checkSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        User user = (User) req.getSession().getAttribute("userObj");

        if(user != null){
            out.println(new ObjectMapper().writeValueAsString(new Response("Session found!", true, user)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("No session.", false, null)));
        }
    }

    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        req.getSession().setAttribute("userObj", null);

        out.println(new ObjectMapper().writeValueAsString(new Response("Session terminated.", true, null)));
    }
}
