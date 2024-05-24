package com.credibanco.bankinc.dfsv.model.persistence.jdbc.RowMapper;

import com.credibanco.bankinc.dfsv.model.dto.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */
public class TransactionsRowMapper implements RowMapper<Transaction> {

    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {

        Transaction transaction = new Transaction();
        transaction.setId(rs.getString("id"));
        transaction.setCardId(rs.getString("card_id"));
        transaction.setTransactionId(rs.getString("transactionId"));
        transaction.setPrice(rs.getString("amount"));
        transaction.setCanceled (rs.getString("canceled"));
        transaction.setCreation_date(rs.getDate("creation_date"));
        return transaction;
    }

}
