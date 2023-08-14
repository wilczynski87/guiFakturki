package com.fakturki.gui.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fakturki.gui.data.Client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainView {

    final ApiController apiController;

    public MainView(ApiController apiController) {
        this.apiController = apiController;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public String mainView(Model model) {
        model.addAttribute("password", "6b851058-e14c-46c9-8348-96f05ed567de");
        System.out.println(model);
        return "index";
    }

    @GetMapping("/panel")
    public String panelView(Model model) {
        model.addAttribute("password", "6b851058-e14c-46c9-8348-96f05ed567de");
        return "test";
    }

    @PostMapping("/hello")
    // @PostMapping(headers = "HX-Request")
    public String furtherInfo() {
        return "fragments/invoices";
        // return "test";
    }

    @GetMapping("/clientsTable")
    public String getClientsTable(Model model) {
        model.addAttribute("clients", apiController.getClients());
        return "fragments/adminPanel :: clientsTable";
    }

    @GetMapping("/sendInvoices")
    public String sendInvoicesToClients(Model model) {
        log.info("\n\n /sendInvoices - hitted!\n");
        apiController.sendAllInvoices();
        model.addAttribute("clients", apiController.getClients());
        return "fragments/adminPanel :: clientsTable";
    }

    @GetMapping("/getClient/{nip}")
    public String getClient(@PathVariable("nip") String nip, Model model) {
        log.info("\n\n/getClient/{nip} has been hit: nip is: " + nip);
        model.addAttribute("client", apiController.getClient(nip));
        model.addAttribute("products", apiController.getProductsByClient(nip));
        model.addAttribute("invoices", apiController.getInvoicesByNip(nip));
        System.out.println(apiController.getProductsByClient(nip));
        return "fragments/client :: client";
    }

    @GetMapping("addClient")
    public String addClient(Model model) {
        model.addAttribute("addClient", true);
        model.addAttribute("client", new Client());
        return "fragments/client :: client"; 
    }

    @PostMapping(value = "/submitClient")
    public String submitClient(Client client, Model model) {
        var result = apiController.saveNewClient(client, true);
        model.addAttribute("response", result);
        
        if(result.equals("Client already exists!")) {
            return "fragments/popups :: clientStatusResultExist";
        } else {
            return "fragments/popups :: clientStatusResultSaved";
        }
    }
    
}
