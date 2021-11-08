package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {

    List<Transfers> getAllTransfers();

    Transfers getTransferById(int transactionID) throws TransferNotFoundException;

    Transfers sendTransfer(Transfers transfers) throws TransferNotFoundException;


    Transfers updateFromAccount(int id, Transfers transfer);
}

//    public String requestTransfer(int userFrom, int userTo, BigDecimal amount);
//
//    public List<Transfers> getPendingRequests(int userId);
//

//}
