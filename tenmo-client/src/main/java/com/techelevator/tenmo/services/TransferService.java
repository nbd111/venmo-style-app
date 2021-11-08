package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class TransferService {
    private  String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private Scanner scanner = new Scanner(System.in);
    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = "http://localhost:8080/";
    }

    public TransferService() {

    }


    //    public Transfers makeTransfer(){
//        Scanner scanner = new Scanner(System.in);
//
//        return transfer;
//    }

    public Transfers transfer(Transfers transfer) {

        try {
            transfer = restTemplate.exchange(BASE_URL + "transfers", HttpMethod.POST,
                    makeTransferEntity(transfer), Transfers.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
        }
        return transfer;
    }

//    public Transfers promptForTransferData(Transfers existingTransfer) {
//        Transfers newTransfer = null;
//        while (newTransfer == null) {
//
//            System.out.println("Enter transfers data as a comma separated list containing:");
//            System.out.println("transfer_type_d, transfer_status_id, account_from, account_to, amount (without dollar sign)");
//            if (existingTransfer != null) {
//                System.out.println("Transfers " + existingTransfer.getTransferTypeId() + " Data: " + existingTransfer.getTransferStatusId() +
//                        ", " + existingTransfer.getAccountFrom() + ", " + existingTransfer.getAccountTo() + ", " +
//                        existingTransfer.getAmount());
//            } else {
//                System.out.println("Example: 1, 1, 2001, 2002, 50");
//            }
//            newTransfer = makeTransferEntity();
//            if (newTransfer == null) {
//                System.out.println("Invalid entry. Please try again.");
//            }
//        }
//        return newTransfer;
//    }

    private HttpEntity<Transfers> makeTransferEntity(Transfers transfer) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(currentUser.getToken());
    HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
        return entity;
}
    private HttpEntity makeAuthEntity () {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
