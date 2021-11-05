package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
//@PreAuthorize("isAuthenticated") // roles
public class AccountController {


    private AccountDAO accountDAO;
    private UserDao userDao;


    public AccountController(AccountDAO accountDAO, UserDao userDao) {
        this.accountDAO = accountDAO;
        this.userDao = userDao;
    }

    @RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@Valid @PathVariable int id) {
        BigDecimal balance = accountDAO.getBalance(id);
        return balance;
    }
}
