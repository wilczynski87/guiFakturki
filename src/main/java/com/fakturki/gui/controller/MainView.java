package com.fakturki.gui.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.fakturki.gui.data.Product;
import com.fakturki.gui.data.ProductEnum;
import com.fakturki.gui.data.UtilityReading;

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
        // log.info("\n\n/getClient/{nip} has been hit: nip is: " + nip);
        model.addAttribute("client", apiController.getClient(nip));
        model.addAttribute("products", apiController.getProductsByClient(nip));
        model.addAttribute("invoices", apiController.getInvoicesByNip(nip));
        // System.out.println(apiController.getProductsByClient(nip));
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

    @GetMapping("addProduct/{nip}")
    public String addProduct(@PathVariable String nip, Model model) {
        model.addAttribute("productNames", ProductEnum.values());
        model.addAttribute("products", apiController.getProductsByClient(nip));
        model.addAttribute("addProduct", true);
        model.addAttribute("product", new Product());
        model.addAttribute("clientNip", nip);
        return "fragments/product :: addProduct"; 
    }

    @PostMapping(value = "/addNewProduct")
    public String submitProduct(Product product, Model model) {
        var result = apiController.addNewProduct(product);
        model.addAttribute("isProductSaved", result);
        model.addAttribute("productNames", ProductEnum.values());
        model.addAttribute("products", apiController.getProductsByClient(product.getNip()));
        model.addAttribute("addProduct", true);
        model.addAttribute("clientNip", product.getNip());
        model.addAttribute("product", new Product());

        if(result.equals("Product Saved!")) {
            model.addAttribute("alertSeverity", "success");
        } else model.addAttribute("alertSeverity", "danger");

        return "fragments/product :: addProduct"; 
    }

    @GetMapping("getUtility/{nip}")
    public String getUtility(@PathVariable String nip, Model model) {
        model.addAttribute("onlyUtilitiesInvoices", true);
        model.addAttribute("clientNip", nip);
        model.addAttribute("lastReadings", apiController.getLastReadings(nip));
        model.addAttribute("invoices", apiController.getUtilityInvoicesByNip(nip));
        return "fragments/product :: addUtilityReading"; 
    }

    @PostMapping("/submitReadings")
    public String submitReadings(UtilityReading utilityReading, Model model) {
        System.out.println(utilityReading);
        var nip = utilityReading.getClientNip();
        model.addAttribute("onlyUtilitiesInvoices", true);
        model.addAttribute("response", apiController.saveUtilityReading(utilityReading));
        model.addAttribute("clientNip", nip);
        model.addAttribute("lastReadings", apiController.getLastReadings(nip));
        model.addAttribute("invoices", apiController.getUtilityInvoicesByNip(nip));
        return "fragments/product :: addUtilityReading"; 
    }

    @PostMapping("/createUtilityInvoice/{nip}")
    public String createUtilityInvoice(@PathVariable String nip, Model model) {
        model.addAttribute("response", apiController.createUtilityInvoice(nip));
        model.addAttribute("onlyUtilitiesInvoices", true);
        model.addAttribute("clientNip", nip);
        model.addAttribute("lastReadings", apiController.getLastReadings(nip));
        model.addAttribute("invoices", apiController.getUtilityInvoicesByNip(nip));
        return "fragments/product :: addUtilityReading"; 
    }
    
}
