package pl.mariusz.marciniak.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.mariusz.marciniak.async.entities.Transaction;
import pl.mariusz.marciniak.async.exceptions.OperationFailedException;
import pl.mariusz.marciniak.operations.data.OperationData;
import pl.mariusz.marciniak.operations.data.TransactionData;
import pl.mariusz.marciniak.operations.logic.OperationLogic;

public class AsyncMainTest {

    private static final String BUY_TRANSACTION  = "Buy";
    private static final int    ACCOUNT_2        = 125593085;
    private static final String SELL_TRANSACTION = "Sell";
    private static final int    ACCOUNT_1        = 203002322;
    private ApplicationContext  appContext;
    private static Logger       logger           = LogManager.getLogger(AsyncMainTest.class);

    public static void main(String[] args) {
        try {
            AsyncMainTest amTest = new AsyncMainTest();
            amTest.init();
            amTest.testAsyncTransactionWithImmediateLock();
            
            //amTest.testAsyncTransaction();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void testAsyncTransaction() throws OperationFailedException {
        logger.info("starting asynchronous test");
        executeOperationOnTransaction("transactionProcessingExecutor", prepareOperationDataForTransaction(prepareTransaction(SELL_TRANSACTION, ACCOUNT_1)));
        executeOperationOnTransaction("transactionProcessingExecutor", prepareOperationDataForTransaction(prepareTransaction(SELL_TRANSACTION, ACCOUNT_2)));
        executeOperationOnTransaction("transactionProcessingExecutor", prepareOperationDataForTransaction(prepareTransaction(BUY_TRANSACTION, ACCOUNT_1)));
        logger.info("executing other logic ...");
    }

    private void testAsyncTransactionWithImmediateLock() throws OperationFailedException {
        logger.info("starting asynchronous test with immediate lock");
        executeOperationOnTransaction("asyncTransactionProcessingExecutor", prepareOperationDataForTransaction(prepareTransaction(SELL_TRANSACTION, ACCOUNT_1)));
        executeOperationOnTransaction("asyncTransactionProcessingExecutor", prepareOperationDataForTransaction(prepareTransaction(SELL_TRANSACTION, ACCOUNT_2)));
        executeOperationOnTransaction("asyncTransactionProcessingExecutor", prepareOperationDataForTransaction(prepareTransaction(BUY_TRANSACTION, ACCOUNT_1)));
        logger.info("executing other logic ...");
    }

    private void executeOperationOnTransaction(String operationLogicBeanName, OperationData<Transaction> operationData) throws OperationFailedException {
        OperationLogic<Transaction> logic = appContext.getBean(operationLogicBeanName, OperationLogic.class);
        logic.executeOperation(operationData);
    }

    private OperationData<Transaction> prepareOperationDataForTransaction(Transaction transaction) {
        OperationData<Transaction> transactionData = new TransactionData("complicated " + transaction.getTransactionType() + " operation on account");
        transactionData.setValueObject(transaction);
        return transactionData;
    }

    private Transaction prepareTransaction(String transactionType, long accountNumber) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setTransactionType(transactionType);
        return transaction;
    }

    private void init() {
        appContext = new ClassPathXmlApplicationContext("spring-context/operationLogic.xml");
    }
}
