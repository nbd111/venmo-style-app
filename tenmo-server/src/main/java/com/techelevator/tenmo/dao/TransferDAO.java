package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {

    public List<Transfers> getAllTransfers();

    public Transfers getTransferById(int transactionID) throws TransferNotFoundException;

    public String sendTransfer(int userFrom, int userTo, BigDecimal amount);

    public String requestTransfer(int userFrom, int userTo, BigDecimal amount);

    public List<Transfers> getPendingRequests(int userId);

    Transfers updateTransferRequest(Transfers transfer, int statusId);
}
