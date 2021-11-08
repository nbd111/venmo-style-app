package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JDBCTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated")
public class TransferController {

    // I am unsure of what the URL is for this /transfers is my assumption at this time
    private JDBCTransferDAO dao;

    public TransferController(JDBCTransferDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfers getTransferById(@PathVariable int id) throws TransferNotFoundException {
        return dao.getTransferById(id);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfers> getAllTransfers() {
        return dao.getAllTransfers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfers sendTransfer(@RequestBody Transfers transfers) throws TransferNotFoundException {

        return dao.sendTransfer(transfers);
        }
}



//      Account toAcoount = new Account
//
//      Inside create make account new account twice
//                          accountdao set accountTo + amount
//
//          Account fromAccount = new Account
//                         accountdao set accountFrom - amount
//                          return balance
//    }

