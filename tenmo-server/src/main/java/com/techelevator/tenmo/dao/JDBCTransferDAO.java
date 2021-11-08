package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCTransferDAO implements TransferDAO {

    private JdbcTemplate jdbcTemplate;
    public JDBCTransferDAO(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}


    public static List<Transfers> transfersList = new ArrayList<>();


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
        Transfers transfer = new Transfers(); // Transfers transfers = null?
        String sql = "SELECT *\n" +
                "FROM transfers\n" +
                "WHERE transfer_id =?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionID);
        if (results.next()) {     //if is correct because we only expect one row
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Transfers sendTransfer(Transfers newTransfers ) throws TransferNotFoundException {
        String sql2 ="(SELECT account_id FROM accounts WHERE user_id = ?)";
        String sql ="INSERT INTO transfers (transfer_type_id, transfer_status_id, account_to, account_from, amount)" +
                "VALUES (2,2,"+sql2+","+sql2+",?) RETURNING transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,newTransfers.getAccountTo(),
            newTransfers.getAccountFrom(), newTransfers.getAmount());
        if(newId == null){
            return null;
        }
        return getTransferById(newId);
    }

    @Override
    public Transfers updateFromAccount(int id, Transfers transfer) {
            String sqlUpdateBalance = "UPDATE accounts SET balance = balance - ? "
                    + "WHERE user_id = ? ";

            return null; //jdbcTemplate.update(sqlUpdateBalance, transfer.getAmount(),id());

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



