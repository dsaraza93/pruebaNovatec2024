package com.credibanco.bankinc.dfsv.component.persistence.dao.tables;

/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */

import com.credibanco.bankinc.dfsv.model.dto.Card;
import com.credibanco.bankinc.dfsv.model.persistence.jdbc.RowMapper.CardsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CardsDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     */

    public Card select(String cardId,String productId) throws Exception{
        Card card = null;
        if ((cardId==null)&&(productId!=null)){
            String sql = "SELECT * FROM cards WHERE productId = ?";
            card = jdbcTemplate.queryForObject(sql, new Object[]{productId}, new CardsRowMapper());
        }else if ((cardId!=null)&&(productId==null)){
            String sql = "SELECT * FROM cards WHERE cardId = ?";
            card = jdbcTemplate.queryForObject(sql, new Object[]{cardId}, new CardsRowMapper());
        }
        return card;
    }

    /**
     ** @author Daniel felipe saraza velez
     *  @since Mayo 22 2024
     * @param field
     * @param value
     * @return
     */
    public int update(String field,String value,String cardId){
        String sql="UPDATE cards SET "+field+"='"+value+"' WHERE cardId=?";
        int count_updated_record=jdbcTemplate.update(sql,cardId);
        return count_updated_record;
    }
}