package com.fakturki.gui.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fakturki.gui.data.Client;
import com.fakturki.gui.data.ClientTable;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Controller
public class ApiController {
    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(conn -> 
            conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

    WebClient api = WebClient.create("http://localhost:8081");
    WebClient apiWithTimeout = WebClient.builder()
        .baseUrl("http://localhost:8081")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();

    public List<ClientTable> getClients() {

        UriSpec<RequestBodySpec> uriSpec = api.post();
        
        RequestBodySpec bodySpec = uriSpec.uri("/clientsTable");

        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("data");

        Flux<Object> response = bodySpec.exchangeToFlux(res -> {
                if (res.statusCode().equals(HttpStatus.OK)) {
                    return res.bodyToFlux(ClientTable.class);
                }
                else {
                    return res.createError().flux();
                }
            });

        // var list = new ArrayList<ClientTable>();
        // list.add(new ClientTable("name1", "1111111111", "01/07/2023", BigDecimal.ZERO));
        // list.add(new ClientTable("name2", "2222222222", "01/07/2023", BigDecimal.ONE));
        // list.add(new ClientTable("name3", "3333333333", "01/07/2023", BigDecimal.ZERO));
        // return list;

        return response.cast(ClientTable.class).collectList().block();
    }

    public void sendAllInvoices() {
        System.out.println("Sending invoices: ");

        api.get()
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
        apiWithTimeout.get()
            .uri(uriBuilder -> uriBuilder
                .path("/products/{nip}")
                .build(nip))
            .retrieve()
            .bodyToMono(Client.class)
            .subscribe();

        return null;
    }
    
}