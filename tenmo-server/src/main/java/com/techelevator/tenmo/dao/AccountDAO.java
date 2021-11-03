package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.math.BigDecimal;

public interface AccountDAO {

    BigDecimal getBalance(int userId);
    BigDecimal addToBalance(BigDecimal amountToAdd, int id);
    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);
    public Accounts findAccountById(int id);
}
