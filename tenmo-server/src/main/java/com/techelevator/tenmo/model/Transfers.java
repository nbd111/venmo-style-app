package com.techelevator.tenmo.model;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfers {

    private int id;

    private int transferTypeId;

    private int transferStatusId;

    private int accountFrom;

    private int accountTo;

    private BigDecimal amount;

    public Transfers(){ }

    public Transfers(int id, int transferTypeId, int transferStatusId, int accountFrom, int accountTo,BigDecimal amount){
    this.id =id;
    this.transferTypeId = transferTypeId;
    this.transferStatusId = transferStatusId;
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
    this.amount = amount;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }
    @Positive
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}