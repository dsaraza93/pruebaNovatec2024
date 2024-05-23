package com.credibanco.bankinc.acgr.component.persistence.dao.tables;

import com.credibanco.bankinc.acgr.model.dto.Transaction;
import com.credibanco.bankinc.acgr.model.persistence.jdbc.RowMapper.TransactionsRowMapper;
import com.credibanco.bankinc.acgr.utilitary.Utilitario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024 11:15 PM GMT -5
 */
@Component
public class TransactionsDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024 11:15 PM GMT -5
     * @param transaction
     */
    public void insert(Transaction transaction) throws Exception{
        String transactionId = Utilitario.obtenerNumeroAleatorio(15);
        String sql = "INSERT INTO transactions(creation_date,card_id, transactionId,amount,canceled) VALUES (CURRENT_TIMESTAMP,?,?,?,?)";
        jdbcTemplate.update(sql,transaction.getCardId(),transactionId,transaction.getPrice(),"n");
    }

    /**
     * * @author Daniel felipe saraza velez
     *  * @since Mayo 22 2024 11:15 PM GMT -5
     */
    public Transaction select(String transactionId) throws Exception{
        String sql = "SELECT * FROM transactions WHERE transactionId = ?";
        Transaction transaction = jdbcTemplate.queryForObject(sql, new Object[]{transactionId}, new TransactionsRowMapper());
        return transaction;
    }

    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024 11:15 PM GMT -5
     * @param field
     * @param value
     * @param transactionId
     * @return
     */
    public int update(String field,String value,String transactionId){
        String sql="UPDATE transactions SET "+field+"='"+value+"' WHERE transactionId=?";
        int count_updated_record=jdbcTemplate.update(sql,transactionId);
        return count_updated_record;
    }
}
