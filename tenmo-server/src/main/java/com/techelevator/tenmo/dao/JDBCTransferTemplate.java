package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.Transfers;
import org.apache.logging.log4j.spi.Terminable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCTransferTemplate implements TransferDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Transfers> getAllTransfers(int id) {
    List<Transfers> transferList = new ArrayList<>();
    String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " +
            "JOIN accounts a ON t.account_from = a.account_id " +
            "JOIN accounts b ON t.account_to = b.account_id " +
            "JOIN users u ON a.user_id = u.user_id " +
            "JOIN users v ON b.user_id = v.user_id " +
            "WHERE a.user_id = ? OR b.user_id = ?";
    SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
    while(results.next()) {
        Transfers transfer = mapRowToTransfer(results);
        transferList.add(transfer);
        }
        return transferList;
    }

    @Override
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
            return "You cannot send money to yourself";




        }
        return null;
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
        return null;
    }

    @Override
    public String updateTransferRequest(Transfers transfer, int statusId) {
        return null;
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



