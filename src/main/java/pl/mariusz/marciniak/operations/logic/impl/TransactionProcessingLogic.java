package pl.mariusz.marciniak.operations.logic.impl;

import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import pl.mariusz.marciniak.async.entities.Transaction;
import pl.mariusz.marciniak.async.exceptions.OperationFailedException;
import pl.mariusz.marciniak.operations.data.OperationData;
import pl.mariusz.marciniak.operations.logic.OperationLogic;

@Component
public class TransactionProcessingLogic implements OperationLogic<Transaction> {

    private Logger logger = LogManager.getLogger(TransactionProcessingLogic.class);

    public Future<Boolean> executeOperation(OperationData<Transaction> data) throws OperationFailedException {
        boolean result = false;
        try {
            Object obj = data.getValueObject();
            if (obj instanceof Transaction) {
                Transaction transaction = (Transaction) obj;
                long startTime = System.currentTimeMillis();
                do {
                    logger.debug("processing " + transaction.getTransactionType() + " operation on account number: " + transaction.getAccountNumber());
                    Thread.sleep(500);
                } while (startTime + 2000 > System.currentTimeMillis());
                result = true;
            }
        } catch(InterruptedException e) {
            logger.error(e);
        }
        return new AsyncResult<Boolean>(result);
    }

}
