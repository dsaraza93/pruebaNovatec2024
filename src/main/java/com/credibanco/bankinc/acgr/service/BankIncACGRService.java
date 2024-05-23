package com.credibanco.bankinc.acgr.service;

import com.credibanco.bankinc.acgr.component.persistence.CE.BankIncACGRCE;
import com.credibanco.bankinc.acgr.model.dto.Card;
import com.credibanco.bankinc.acgr.model.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */
@Service
public class BankIncACGRService {

    @Autowired
    BankIncACGRCE bankIncACGRCE;

    public void inicializarBaseDatosH2(){
        bankIncACGRCE.inicializarBaseDatosH2();
    }

    public List<Card> getAllAvailableCards(){
        return bankIncACGRCE.getAllAvailableCards();
    }

    public List<Transaction> getAllTransactions() throws Exception{
        return bankIncACGRCE.getAllTransactions();
    }
    public String generarNumeroTarjeta(String productId) throws Exception{
        return bankIncACGRCE.generarNumeroTarjeta(productId);
    }
    public int activateCardEnroll(String cardId) throws Exception{
        return bankIncACGRCE.activateCardEnroll(cardId);
    }
    public int blockCard(String cardId) throws Exception{
        return bankIncACGRCE.blockCard(cardId);
    }
    public int rechargeBalance(Card card) throws Exception{
        return bankIncACGRCE.rechargeBalance(card);
    }
    public String getCurrentBalance(String cardId) throws Exception{
        return bankIncACGRCE.getCurrentBalance(cardId);
    }
    public String transactionBuy(Transaction transaction) throws Exception {
        return bankIncACGRCE.transactionBuy(transaction);
    }
    public Transaction queryTransaction(String transactionId) throws Exception{
        return bankIncACGRCE.queryTransaction(transactionId);
    }
    public int cancelTransaction(Transaction transaction) throws Exception{
        return bankIncACGRCE.cancelTransaction(transaction);
    }
}
