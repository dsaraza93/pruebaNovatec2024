package com.credibanco.bankinc.acgr.controller;

import com.credibanco.bankinc.acgr.application.ACGRBankIncRestServiceApplication;
import com.credibanco.bankinc.acgr.model.dto.Card;
import com.credibanco.bankinc.acgr.model.dto.Transaction;
import com.credibanco.bankinc.acgr.response.GenericResponse;
import com.credibanco.bankinc.acgr.service.BankIncACGRService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Se implementa estandar  HATEOAS
 *
 * Con HATEOAS,
 * un cliente interactúa con una aplicación de red cuyos servidores de aplicación proporcionan
 * información dinámicamente a través de hipermedia.  Un cliente REST necesita poco o
 * ningún conocimiento previo sobre cómo interactuar con una aplicación o servidor
 * más allá de un conocimiento genérico de los hipermedia.
 *
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */

@RestController
public class BankIncACGRController {

    private static final Logger log = LoggerFactory.getLogger(ACGRBankIncRestServiceApplication.class);

    @Autowired
    BankIncACGRService bankIncACGRService;

    @PostConstruct
    public void init() {
        bankIncACGRService.inicializarBaseDatosH2();
    }

    /**
     *  @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     */
    @GetMapping(value="/AllAvailableCards", produces = "application/json")
    public CollectionModel<Card> getAllAvailableCards(){
        try{
            List<Card> l_cards = bankIncACGRService.getAllAvailableCards();
            for(Card card:l_cards){
                card.add(linkTo(methodOn(BankIncACGRController.class).getAllAvailableCards()).withSelfRel());
            }
            CollectionModel<Card> collectionModel = CollectionModel.of(l_cards);
            collectionModel.add(linkTo(methodOn(BankIncACGRController.class).getAllAvailableCards()).withSelfRel());
            return collectionModel;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     */
    @GetMapping(value="/AllTransactions", produces = "application/json")
    public CollectionModel<Transaction> getAllTransactions(){
        try{
            List<Transaction> l_trx  = bankIncACGRService.getAllTransactions();
            for(Transaction transaction:l_trx){
                transaction.add(linkTo(methodOn(BankIncACGRController.class).getAllAvailableCards()).withSelfRel());
            }
            CollectionModel<Transaction> collectionModel = CollectionModel.of(l_trx);
            collectionModel.add(linkTo(methodOn(BankIncACGRController.class).getAllTransactions()).withSelfRel());
            return collectionModel;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 1.
     * Generar número de tar jeta
     * Tipo de método: GET
     * Recurso: /card/{productId}/number
     *
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     */

    @GetMapping("/card/{productId}")
    public RepresentationModel<GenericResponse> generarNumeroTarjeta(@PathVariable String productId){
        try{
            GenericResponse successfulResponse = new GenericResponse();
            String cardId = bankIncACGRService.generarNumeroTarjeta(productId);
            successfulResponse.setId(200);
            successfulResponse.setDescription("OK cardId generado="+cardId);
            RepresentationModel<GenericResponse> collectionModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
            collectionModel.add(linkTo(methodOn(BankIncACGRController.class).generarNumeroTarjeta(productId)).withSelfRel());
            return collectionModel;

        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 3.
     * Bloquear tarjeta
     * Tipo de método: DELETE
     * Recurso: /card/{cardId}
     *
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @param
     * @return
     * @throws Exception
     */

    @DeleteMapping("/card/{cardId}")
    public RepresentationModel<GenericResponse> blockCard(@PathVariable String cardId) throws Exception{
        try{
            GenericResponse successfulResponse = new GenericResponse();
            int records_updated = bankIncACGRService.blockCard(cardId);
            if (records_updated==1){
                successfulResponse.setId(200);
                successfulResponse.setDescription("OK Tarjeta Bloqueada "+cardId);
                RepresentationModel<GenericResponse> representationModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
                representationModel.add(linkTo(methodOn(BankIncACGRController.class).blockCard(cardId)).withSelfRel());
                return representationModel;
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 2.
     * Activar tarjeta
     * Tipo de método: POST
     * Recurso: /card/enroll
     *
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @param
     * @return
     * @throws Exception
     */

    @PostMapping(value="/card/enroll", consumes = "application/json", produces = "application/json")
    public RepresentationModel<GenericResponse> activateCardEnroll(@RequestBody Card card) throws Exception{
        try{
            GenericResponse successfulResponse = new GenericResponse();
            int records_updated = bankIncACGRService.activateCardEnroll(card.getCardId());
            if (records_updated==1){
                successfulResponse.setId(200);
                successfulResponse.setDescription("OK Tarjeta Activada "+card.getCardId());
                RepresentationModel<GenericResponse> collectionModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
                collectionModel.add(linkTo(methodOn(BankIncACGRController.class).activateCardEnroll(card)).withSelfRel());
                return collectionModel;
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 4.
     * Recargar saldo
     * Tipo de método: POST
     * Recurso: /card/balance
     *
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @param card
     * @return
     * @throws Exception
     */

    @PostMapping(value="/card/balance", consumes = "application/json", produces = "application/json")
    public RepresentationModel<GenericResponse> rechargeBalance(@RequestBody Card card) throws Exception{
        try{
            GenericResponse successfulResponse = new GenericResponse();
            int records_updated = bankIncACGRService.rechargeBalance(card);
            if (records_updated==1){
                successfulResponse.setId(200);
                successfulResponse.setDescription("OK Tarjeta  "+card.getCardId()+ " Saldo Recargado");
                RepresentationModel<GenericResponse> representationModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
                representationModel.add(linkTo(methodOn(BankIncACGRController.class).rechargeBalance(card)).withSelfRel());
                return representationModel;
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 5.
     * Consulta de saldo
     * Tipo de método: GET
     * Recurso: /card/balance/{cardId}
     *
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @param cardId
     * @return
     * @throws Exception
     */
    @GetMapping("/card/balance/{cardId}")
    public RepresentationModel<GenericResponse> getCurrentBalance(@PathVariable String cardId) throws Exception{
        try{
            String balance = bankIncACGRService.getCurrentBalance(cardId);
            GenericResponse successfulResponse = new GenericResponse();
            successfulResponse.setId(200);
            successfulResponse.setDescription("OK Tarjeta  "+cardId+ " Saldo "+balance);
            RepresentationModel<GenericResponse> representationModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
            representationModel.add(linkTo(methodOn(BankIncACGRController.class).getCurrentBalance(cardId)).withSelfRel());
            return representationModel;
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 6.
     * T ransacción de compra
     * Tipo de método: POST
     * Recurso: /transaction/purchase
     *
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @param transaction
     * @return
     * @throws Exception
     */
    @PostMapping(value="/transaction/purchase", consumes = "application/json", produces = "application/json")
    public RepresentationModel<GenericResponse> transactionBuy(@RequestBody  Transaction transaction) throws Exception {
        try{
            String count_record = bankIncACGRService.transactionBuy(transaction);
            GenericResponse successfulResponse = new GenericResponse();
            successfulResponse.setId(200);
            successfulResponse.setDescription("OK Tarjeta  "+transaction.getCardId()+ " Transaccion exitosa ");
            RepresentationModel<GenericResponse> representationModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
            representationModel.add(linkTo(methodOn(BankIncACGRController.class).transactionBuy(transaction)).withSelfRel());
            return representationModel;
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @param transactionId
     * @return
     * @throws Exception
     */
    @GetMapping("/transaction/{transactionId}")
    public RepresentationModel<Transaction> queryTransaction(@PathVariable String transactionId) throws Exception{
        try{
            Transaction transaction = bankIncACGRService.queryTransaction(transactionId);
            /*GenericResponse successfulResponse = new GenericResponse();
            successfulResponse.setId(200);
            successfulResponse.setDescription("OK Tarjeta  "+transaction.getCardId()+ " Transaccion exitosa ");*/
            RepresentationModel<Transaction> representationModel = (RepresentationModel<Transaction>) RepresentationModel.of(transaction);
            representationModel.add(linkTo(methodOn(BankIncACGRController.class).queryTransaction(transactionId)).withSelfRel());
            return representationModel;
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }

    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     * @return
     * @throws Exception
     */
    @PostMapping(value="/transaction/anulation", consumes = "application/json", produces = "application/json")
    public RepresentationModel<GenericResponse> cancelTransaction(@RequestBody Transaction transaction) throws Exception{
        try{
            int count_updated_record = bankIncACGRService.cancelTransaction(transaction);
            GenericResponse successfulResponse = new GenericResponse();
            successfulResponse.setId(200);
            successfulResponse.setDescription("OK Tarjeta  con transactionId"+transaction.getTransactionId()+ " anulada ");
            RepresentationModel<GenericResponse> representationModel = (RepresentationModel<GenericResponse>) RepresentationModel.of(successfulResponse);
            representationModel.add(linkTo(methodOn(BankIncACGRController.class).cancelTransaction(transaction)).withSelfRel());
            return representationModel;
        }catch(Exception e){
            log.error(e.toString());
        }
        return null;
    }
}