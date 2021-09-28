package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reimbursement;
import models.Response;
import services.ReimbursementService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ReimbursementController {
    private static ReimbursementController reimbursementController;
    ReimbursementService reimbursementService;

    private ReimbursementController(){
        reimbursementService = new ReimbursementService();
    }

    public static ReimbursementController getInstance(){
        if (reimbursementController == null)
            reimbursementController = new ReimbursementController();

        return reimbursementController;
    }

    public void getAllReimbursements(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Reimbursement> reimbursementList = reimbursementService.getAllReimbursements();
        if (reimbursementList.size() > 0){
            out.println(new ObjectMapper().writeValueAsString(new Response("Reimbursements fetched.", true, reimbursementList)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("Permission denied.", false, null)));
        }
    }

    public void getAllReimbursementsGivenId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Integer id =  Integer.parseInt(req.getQueryString().replace("id=", ""));

        List<Reimbursement> reimbursementList = reimbursementService.getAllReimbursementsGivenId(id);

        if (reimbursementList.size() > 0) {
            out.println(new ObjectMapper().writeValueAsString(new Response("Reimbursements fetched.", true, reimbursementList)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("Unable to fetch.", false, null)));
        }
    }

    public void createReimbursement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        Reimbursement reimbursement = new ObjectMapper().readValue(requestBody, Reimbursement.class);

        if (reimbursementService.createReimbursement(reimbursement)){
            out.println(new ObjectMapper().writeValueAsString(new Response("Reimbursement created!", true, reimbursement)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("Unable to create reimbursement.", false, null)));
        }
    }

    public void updateReimbursementStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        Reimbursement reimbursement = new ObjectMapper().readValue(requestBody, Reimbursement.class);

        if (reimbursementService.updateReimbursementStatus(reimbursement)){
            out.println(new ObjectMapper().writeValueAsString(new Response("Status updated!", true, reimbursement)));
        } else {
            out.println(new ObjectMapper().writeValueAsString(new Response("Unable to update status.", false, null)));
        }
    }
}
