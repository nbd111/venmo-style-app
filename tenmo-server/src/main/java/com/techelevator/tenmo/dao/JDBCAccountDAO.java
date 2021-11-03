package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JDBCAccountDAO implements AccountDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCAccountDAO() {}

    public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE use_id = ? ";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving data");
        }
        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        Accounts accounts = findAccountById(id);
        BigDecimal newBalance = accounts.getBalance().add(amountToAdd);
        System.out.println(newBalance);
        String sql = "UPDATE accounts SET balance =  ? WHERE user_id = ? ";
        try {
            jdbcTemplate.update(sql, newBalance, id);
        } catch (DataAccessException e) {
            System.out.println("Error retrieving data");
        }
        return accounts.getBalance();
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        Accounts accounts = findAccountById(id);
        BigDecimal newBalance = accounts.getBalance().subtract(amountToSubtract);
        String sql = "UPDATE accounts SET balance = ?  WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, id);
        } catch (DataAccessException e) {
            System.out.println("Error retrieving data");
        }
        return accounts.getBalance();
    }


    @Override
    public Accounts findAccountById(int id) {
        Accounts accounts = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            accounts = mapRowToAccount(results);
        }
        return accounts;
    }

    private Accounts mapRowToAccount(SqlRowSet result) {
        Accounts account = new Accounts();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountId(result.getInt("account_id"));
        return account;
    }
}
