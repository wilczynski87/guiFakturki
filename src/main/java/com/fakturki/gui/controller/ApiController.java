package com.fakturki.gui.controller;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import com.fakturki.gui.data.Client;
import com.fakturki.gui.data.ClientTable;
import com.fakturki.gui.data.Invoice;
import com.fakturki.gui.data.Product;
import com.fakturki.gui.data.ProductEnum;
import com.fakturki.gui.data.UtilityReading;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Controller
public class ApiController {

    @Value("${api:localhost}")
    private String myApi;

    @Value(value = "${my.port:8081}")
    private String myUrl;

    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(conn -> 
            conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

    private WebClient webClient() {
        return WebClient.builder()
        .baseUrl(String.format("http://%s:%s", myApi, myUrl))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
        .clientConnector(new ReactorClientHttpConnector(this.httpClient))
        .build();
    }

    public List<ClientTable> getClients() {
        return webClient().post()
            .uri("/clientsTable")
            .retrieve()
            .bodyToFlux(ClientTable.class)
            .collectList()
            .block()
            ;
    }

    public void sendAllInvoices() {
        System.out.println("Sending invoices: ");

        webClient().get()
            .uri("/invoicesSending")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess(x -> log.info("Greate Succes: " + x.getStatusCode()))
            .onErrorComplete(x -> {
                log.error("Greate Failure! " + x.getLocalizedMessage()); 
                 return true;})
            .subscribe()
            ;
        System.out.println("Finish Sending invoices: ");
    }

    public Client getClient(String nip) {
        Mono<Client> client;
        var myClient = webClient().post()
            .uri("/getClient/{nip}", nip)
            .retrieve()
            .bodyToMono(Client.class)
            ;

        return myClient.block();
    }

    public void sendInvoiceToClient(String nip, String month, String year) {
        webClient().get()
            .uri(uriBuilder -> uriBuilder
                .path("/???")
                .queryParam("nip", "{nip}")
                .queryParam("month", "{month}")
                .queryParam("year", "{year}")
                .build(nip, month, year))
            .retrieve()
            .bodyToMono(void.class)
            .block()
            ;
    }
    
    public List<Product> getProductsByClient(String nip) {
        return webClient().post()
            .uri("/getProductsByClient/{nip}", nip)
            .retrieve()
            .bodyToFlux(Product.class)
            .collectList()
            .block();
    }

    public List<Invoice> getInvoicesByNip(String nip) {
        return webClient().post()
            .uri("/getInvoicesByNip/{nip}", nip)
            .retrieve()
            .bodyToFlux(Invoice.class)
            .collectList()
            .block();
    }

    public String saveNewClient(Client client, boolean isNew) {
        return webClient().post()
            .uri("/saveClient")
            .bodyValue(client)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(String.class);
                } else if (response.statusCode().is4xxClientError()) {
                    return Mono.just("Client Error");
                } else if (response.statusCode().is5xxServerError()) {
                    return Mono.just("Server Error");
                } else {
                    return Mono.just("Other Error");
                }
            })
            .block()
            ;
    }

    public String addNewProduct(Product product) {
        record ProductDto(String clientNip, String productDesc, float unitPrice, float quantity, int vatRate) {}
        var productDto = new ProductDto(product.getNip(), product.getProductEnum().toString(), product.getUnitPrice().floatValue(), product.getQuantity().floatValue(), product.getVatRate());

        return webClient().post()
            .uri("/saveProductTemplate")
            .bodyValue(productDto)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(String.class);
                } else if (response.statusCode().is4xxClientError()) {
                    return Mono.just("Product Error");
                } else if (response.statusCode().is5xxServerError()) {
                    return Mono.just("Server Error");
                } else {
                    return Mono.just("Other Error");
                }
            }).block();
    }

    public UtilityReading getLastReading(String nip, ProductEnum product) {
        
        return webClient().post()
            .uri("/getLastReadingForNip/{nip}/{product}", nip, product)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(UtilityReading.class);
                } else if (response.statusCode().is4xxClientError()) {
                    return Mono.just(new UtilityReading());
                } else if (response.statusCode().is5xxServerError()) {
                    return Mono.just(new UtilityReading());
                } else {
                    return Mono.just(new UtilityReading());
                }
            }).block();
        // return null;
    }
    public List<UtilityReading> getLastReadings(String nip) {
        var utilityList = List.of(ProductEnum.PRAD, ProductEnum.WODA);
        return utilityList.stream().map(product -> getLastReading(nip, product)).filter(reading -> reading.getId() != null).toList();
    }

    public List<Invoice> getUtilityInvoicesByNip(String nip) {
        return webClient().post()
            .uri(uriBuilder -> uriBuilder
                .path("/getUtilityInvoicesForClient/{nip}")
                .queryParam("productEnum", ProductEnum.PRAD)
                .build(nip))
            .retrieve()
            .bodyToFlux(Invoice.class)
            .collectList()
            .block();
    }

    public String saveUtilityReading(UtilityReading utilityReading) {
        String uriPath = "saveUtilityReading";
        return webClient().post()
            .uri(uriPath)
            .bodyValue(utilityReading)
            .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode().is4xxClientError()) {
                        return Mono.just("Bad request Error");
                    } else if (response.statusCode().is5xxServerError()) {
                        return Mono.just("Server Error");
                    } else return Mono.just("Other Error");
                })
            .block();
    }

    public String createUtilityInvoice(String nip) {
        return webClient().post()
            .uri("/createUtilityInvoice/{nip}", nip)
            .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode().is4xxClientError()) {
                        return Mono.just("Bad request Error");
                    } else if (response.statusCode().is5xxServerError()) {
                        return Mono.just("Server Error");
                    } else return Mono.just("Other Error");
                })
            .block();
    }
}