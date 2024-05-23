package com.credibanco.bankinc.acgr.model.persistence.jdbc.RowMapper;

import com.credibanco.bankinc.acgr.model.dto.Card;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */
public class CardsRowMapper  implements RowMapper<Card> {

    @Override
    public Card mapRow(ResultSet rs, int rowNum) throws SQLException {

        Card card = new Card();
        card.setFirst_name(rs.getString("first_name"));
        card.setLast_name(rs.getString("last_name"));
        card.setProductId(rs.getString("productId"));
        card.setCardId(rs.getString("cardId"));
        card.setBalance(rs.getString("balance"));
        card.setActive(rs.getString("active"));
        card.setBlocked(rs.getString("blocked"));
        card.setCreation_date(rs.getString("creation_date"));
        card.setExpiry_date(rs.getString("expiry_date"));
        return card;

    }
}
