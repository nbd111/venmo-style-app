package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfers;
import org.apache.logging.log4j.spi.Terminable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCTransferDAO implements TransferDAO {

    private JdbcTemplate jdbcTemplate;
    private AccountDAO accountDAO;

    @Override // I am unsure of my SQL --- i would like to pull it up in DB vis
    public List<Transfers> getAllTransfers() {
    List<Transfers> transferList = new ArrayList<>();
    String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " +
            "JOIN accounts a ON t.account_from = a.account_id " +
            "JOIN accounts b ON t.account_to = b.account_id " +
            "JOIN users u ON a.user_id = u.user_id " +
            "JOIN users v ON b.user_id = v.user_id " +
            "WHERE a.user_id = ? OR b.user_id = ?";
    SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
    while(results.next()) {
        Transfers transfer = mapRowToTransfer(results);
        transferList.add(transfer);
        }
        return transferList;
    }

    @Override   // I am unsure of my SQL --- i would like to pull it up in DB vis
    public Transfers getTransferById(int transactionID) throws TransferNotFoundException {
        Transfers transfer = new Transfers();
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc" +
                     "FROM transfers t " +
                     "JOIN accounts a ON t.account_from = a.account_id " +
                     "JOIN accounts b ON t.account_from = b.account_id " +
                     "JOIN users u ON a.user_id = b.account_id " +
                     "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                     "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                     "WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionID);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

    @Override
    public String sendTransfer(int userFrom, int userTo, BigDecimal amount) {
        if (userFrom == userTo) {
            return "You can not send money to your self.";
        }
        if (amount.compareTo(accountDAO.getBalance(userFrom)) < 0 && amount.compareTo(new BigDecimal(0)) > 0) {
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, ?, ?, ?)";
            jdbcTemplate.update(sql, userFrom, userTo, amount);
            accountDAO.addToBalance(amount, userTo);
            accountDAO.subtractFromBalance(amount, userFrom);
            return "Transfer complete";
        } else {
            return "Transfer failed";
        }
    }

    @Override
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount) {
        if (userFrom == userTo) {
            return "You cannot send money to yourself!";
        }
        if (amount.compareTo(new BigDecimal(0))== 1) {
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                         "VALUES (1, 1, ?, ?, ?)";
            jdbcTemplate.update(sql, userFrom, userTo, amount);
            return "Request successfully sent";
        } else {
            return "There was an error sending the request";
        }
    }

    @Override
    public List<Transfers> getPendingRequests(int userId) {
        List<Transfers> output = new ArrayList<>();
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " +
                "JOIN accounts a ON t.account_from = a.account_id " +
                "JOIN accounts b ON t.account_to = b.account_id " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN users v ON b.user_id = v.user_id " +
                "WHERE transfer_status_id = 1 AND (account_from = ? OR account_to = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfers transfer = mapRowToTransfer(results);
            output.add(transfer);
        }
        return output;
    }

    @Override
    public String updateTransferRequest(Transfers transfer, int statusId) {
        if (statusId == 3) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getId());
            return "Update successful";
        }
        if (!(accountDAO.getBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) < 0)) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getId());
            accountDAO.addToBalance(transfer.getAmount(), transfer.getAccountTo());
            accountDAO.subtractFromBalance(transfer.getAmount(), transfer.getAccountFrom());
            return "Update successful";
        } else {
            return "Insufficient funds for transfer";
        }
    }


    // Helper Method
    private Transfers mapRowToTransfer(SqlRowSet results) {
        Transfers transfer = new Transfers();
        transfer.setTransferTypeId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}



