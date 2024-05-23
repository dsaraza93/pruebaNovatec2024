package com.credibanco.bankinc.acgr.component.persistence.CE;

import com.credibanco.bankinc.acgr.application.ACGRBankIncRestServiceApplication;
import com.credibanco.bankinc.acgr.component.persistence.dao.tables.CardsDAO;
import com.credibanco.bankinc.acgr.component.persistence.dao.tables.TransactionsDAO;
import com.credibanco.bankinc.acgr.model.dto.Card;
import com.credibanco.bankinc.acgr.model.dto.Transaction;
import com.credibanco.bankinc.acgr.model.persistence.jdbc.RowMapper.CardsRowMapper;
import com.credibanco.bankinc.acgr.model.persistence.jdbc.RowMapper.TransactionsRowMapper;
import com.credibanco.bankinc.acgr.utilitary.BankIncACGRConstants;
import com.credibanco.bankinc.acgr.utilitary.Utilitario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024 11:15 PM GMT -5
 */
@Component
public class BankIncACGRCE {

    private static final Logger log = LoggerFactory.getLogger(ACGRBankIncRestServiceApplication.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CardsDAO cardsDAO;

    @Autowired
    TransactionsDAO transactionDAO;

    /**
     * Este metodo se llama desde el init del BankIncACGRController pasando
     * por controller->service->component para crear e inicializar la base de datos
     * en memoria principal con H2
     *
     * @author Daniel Saraza
     * @since mayo 2024
     */
    public void inicializarBaseDatosH2(){
        try{
            log.info("Creation of table cards started");
            jdbcTemplate.execute("DROP TABLE cards IF EXISTS");
            jdbcTemplate.execute("CREATE TABLE cards(" + "id SERIAL, creation_date TIMESTAMP, expiry_date TIMESTAMP, first_name VARCHAR(20), last_name VARCHAR(20), productId VARCHAR(20), cardId VARCHAR(20), balance VARCHAR(20),active VARCHAR(2), blocked VARCHAR(2))");
            jdbcTemplate.execute("INSERT INTO cards(creation_date,expiry_date,first_name, last_name,productId,cardId,balance,active,blocked) VALUES (CURRENT_TIMESTAMP,DATEADD('YEAR',3, CURRENT_DATE),'First Customer','First Customer','549782','','0','n','n')");
            jdbcTemplate.execute("INSERT INTO cards(creation_date,expiry_date,first_name, last_name,productId,cardId,balance,active,blocked) VALUES (CURRENT_TIMESTAMP,DATEADD('YEAR',3, CURRENT_DATE),'Second Customer','Second Customer','367542','','0','n','n')");
            log.info("Creation of table cards ended successfully");
            log.info("Creation of table trx started");
            jdbcTemplate.execute("DROP TABLE trx IF EXISTS");
            jdbcTemplate.execute("CREATE TABLE transactions(id SERIAL, creation_date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,card_id VARCHAR(20), transactionId VARCHAR(20), amount VARCHAR(20),canceled VARCHAR(2))");
            log.info("Creation of table trx successful");
        }catch(Exception e){
            log.error("ERROR GENERAL "+e.toString());
        }
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */
    public List<Card> getAllAvailableCards(){
        try{
            List<Card> l_result =  jdbcTemplate.query("SELECT * FROM cards",new CardsRowMapper());
            return l_result;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public List<Transaction> getAllTransactions() throws Exception{
        List<Transaction> l_result = jdbcTemplate.query("SELECT * FROM transactions",new TransactionsRowMapper());
        return l_result;
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    private String consultarNumeroTarjeta(String productId) throws Exception{
        Card card1 = cardsDAO.select(null,productId);
        return card1.getCardId();
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public String actualizarNumeroTarjetaAPartirProductID(String productId) throws Exception{
        String cardId= Utilitario.obtenerNumeroAleatorio(10);
        String sql_update="UPDATE cards SET cardId=? WHERE productId=?";
        int cuantos=jdbcTemplate.update(sql_update,cardId,productId);
        return cardId;
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public String generarNumeroTarjeta(String productId) throws Exception{
        return this.actualizarNumeroTarjetaAPartirProductID(productId);
    }

    /**
     * @author daniel saraza
     * @since mato 2024
     */

    public int activateCardEnroll(String cardId) throws Exception{
        String blocked=this.checkIfCardisBlocked(cardId);
        if (blocked!=null&&blocked.equals("y")){
            return BankIncACGRConstants.CARD_BLOCKED;
        }
        return this.activateCard(cardId);
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public int activateCard(String cardId){
        String sql_update="UPDATE cards SET active='y' WHERE cardId=?";
        int records_updated=jdbcTemplate.update(sql_update,cardId);
        if (records_updated==0) {
            return BankIncACGRConstants.INVALID_CARD_ID;
        }
        return records_updated;
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    private String checkIfCardisBlocked(String cardId) throws Exception{
        String sql = "SELECT * FROM cards WHERE cardId = ?";
        Card card1 = jdbcTemplate.queryForObject(sql, new Object[]{cardId}, new CardsRowMapper());
        return card1.getBlocked();
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    private String checkIfCardisActive(String cardId) throws Exception{
        String sql = "SELECT * FROM cards WHERE cardId = ?";
        Card card1 = jdbcTemplate.queryForObject(sql, new Object[]{cardId}, new CardsRowMapper());
        return card1.getActive();
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */


    public String getCurrentBalance(String cardId) throws Exception{
        String blocked=this.checkIfCardisBlocked(cardId);
        if (blocked!=null&&blocked.equals("y")){
            return String.valueOf(BankIncACGRConstants.CARD_BLOCKED);
        }
        return this.getCurrentBalanceAux(cardId);
    }
    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public String getCurrentBalanceAux(String cardId) throws Exception{
        String sql = "SELECT * FROM cards WHERE cardId = ?";
        Card card1 = jdbcTemplate.queryForObject(sql, new Object[]{cardId}, new CardsRowMapper());
        return card1.getBalance();
    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public int blockCard(String cardId) throws Exception{
        String sql="UPDATE cards SET blocked='y' WHERE cardId=?";
        int count_updated_records=jdbcTemplate.update(sql,cardId);
        if (count_updated_records==0){
            return BankIncACGRConstants.INVALID_CARD_ID;
        }
        String active=this.checkIfCardisActive(cardId);
        if (active.equals("n")){
            return BankIncACGRConstants.INACTIVE_CARD;
        }
        return count_updated_records;
    }

    /**
     * 4.
     * Recargar saldo
     * Tipo de método: POST
     * Recurso: /card/balance
     * @author daniel saraza
     * @since mayo 2024
     */

    public int rechargeBalance(Card card) throws Exception{
        String blocked=this.checkIfCardisBlocked(card.getCardId());
        if (blocked!=null&&blocked.equals("y")){
            return BankIncACGRConstants.CARD_BLOCKED;
        }
        String active=this.checkIfCardisActive(card.getCardId());
        if (active.equals("n")){
            return BankIncACGRConstants.INACTIVE_CARD;
        }
        return rechargeBalanceAux(card.getCardId(),card.getBalance());
    }
    /**
     * @author daniel saraza
     * @since mayo 2024
     */

    public int rechargeBalanceAux(String cardId,String new_balance) throws Exception{
        String sql = "SELECT * FROM cards WHERE cardId = ?";
        Card card1 = jdbcTemplate.queryForObject(sql, new Object[]{cardId}, new CardsRowMapper());
        double new_balance1 = Double.valueOf(card1.getBalance()).doubleValue()+Double.valueOf(new_balance).doubleValue();
        String sql_update="UPDATE cards SET balance=? WHERE cardId=?";
        int udpated_records=jdbcTemplate.update(sql_update,String.valueOf(new_balance1),cardId);
        if (udpated_records==0){
            return BankIncACGRConstants.INVALID_OPERATION;
        }
        return udpated_records;
    }

    /**
     *
     * 6.
     * T ransacción de compra
     * Tipo de método: POST
     * Recurso: /transaction/purchase
     * @author daniel saraza
     * @since mayo 2024
     * @param transaction
     * @return
     * @throws Exception
     */

    public String transactionBuy(Transaction transaction) throws Exception {
        //______________________________________________________________
        // En este bloque validamos que la tarjeta no este bloqueada.
        // Si la tarjeta esta bloqueada, se retorna código de error
        //______________________________________________________________
        String blocked=this.checkIfCardisBlocked(transaction.getCardId());
        if (blocked!=null&&blocked.equals("y")){
            return String.valueOf(BankIncACGRConstants.CARD_BLOCKED);
        }
        //______________________________________________________________
        // En este bloque validamos que la tarjeta este activa.  Si no esta activa,
        // retorna código de error
        //______________________________________________________________
        String active=this.checkIfCardisActive(transaction.getCardId());
        if (active.equals("n")){
            return String.valueOf(BankIncACGRConstants.INACTIVE_CARD);
        }
        //______________________________________________________________
        // En este bloque validamos que no se vaya a llevar a cabo una transaccion
        // si no hay los fondos suficientes
        //______________________________________________________________
        String current_balance=this.getCurrentBalance(transaction.getCardId());
        double current_balance_ = Double.valueOf(current_balance).doubleValue();
        if (current_balance_<Double.valueOf(transaction.getPrice()).doubleValue()){
            return String.valueOf(BankIncACGRConstants.INSUFFICIENT_FONDS);
        }
        transactionDAO.insert(transaction); // Aqui registramos la transaccion en la base de datos
        double new_balance = current_balance_-Double.valueOf(transaction.getPrice()).doubleValue(); // Aqui calculamos el nuevo saldo
        return this.updateNewBalance(String.valueOf(new_balance),transaction.getCardId()); // Aqui actualizamos el nuevo saldo, restando al saldo anterior, ya que no es recarga, sino compra

    }

    /**
     * @author daniel saraza
     * @since mayo 2024
     * @return
     * @throws Exception
     */

    public String updateNewBalance(String new_balance,String cardId) throws Exception{
        String sql="UPDATE cards SET balance=? WHERE cardId=?";
        int count_updated_records=jdbcTemplate.update(sql,String.valueOf(new_balance),cardId);
        return String.valueOf(count_updated_records);
    }

    /**
     * 6.
     * Transacción de compra
     * Tipo de método: POST
     * Recurso: /transaction/purchase
     * @author daniel saraza
     * @since mayo 2024
     * @param transactionId
     * @return
     * @throws Exception
     */
    public Transaction queryTransaction(String transactionId) throws Exception{
        return transactionDAO.select(transactionId);
    }

    /**
     * 7.
     * Consultar transacción
     * Tipo de método: GET
     * Recurso: /transaction/{transactionId}
     * @author daniel saraza
     * @since mayo 2024
     * @return
     * @throws Exception
     */
    public int cancelTransaction(Transaction transaction) throws Exception{
        Transaction transaction1 = transactionDAO.select(transaction.getTransactionId());
        transactionDAO.update("canceled","y",transaction.getTransactionId());
        Card card = cardsDAO.select(transaction.getCardId(),null);
        card.setBalance(String.valueOf(Double.valueOf(card.getBalance()).doubleValue()+Double.valueOf(transaction1.getPrice()).doubleValue()));
        return cardsDAO.update("balance",card.getBalance(),card.getCardId());
    }
}