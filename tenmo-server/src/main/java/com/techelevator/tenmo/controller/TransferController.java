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
@PreAuthorize("isAuthenticated")
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
    @RequestMapping(path = "/transfers/userfrom/{id}/userto/{id}", method = RequestMethod.POST)
    public Transfers sendTransfers(@Valid @RequestBody Transfers transfers) throws TransferNotFoundException {
        return null;
//                dao.sendTransfer(transfers.getAccountFrom(), transfers.getAccountTo(),
//                transfers.setAmount(transfers.getAmount());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers/userfrom/{id}", method = RequestMethod.POST)
    public Transfers requestTransfers(@Valid @RequestBody Transfers transfers) throws TransferNotFoundException {
        return null;
//                dao.requestTransfer(transfers.getAccountFrom(), transfers.getAccountTo(),
//                        transfers.setAmount(transfers.getAmount());)
    }
//    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.PUT)
//    public Transfers updateTransferRequest(@Valid @RequestBody Transfers transfers, @PathVariable int id)
//            throws TransferNotFoundException {
//
//        return dao.updateTransferRequest(transfers,id);
//    }

}