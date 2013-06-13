package pl.mariusz.marciniak.operations.logic.impl;

import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.mariusz.marciniak.async.entities.Transaction;
import pl.mariusz.marciniak.async.exceptions.OperationFailedException;
import pl.mariusz.marciniak.locking.lockers.ResourceReleaser;
import pl.mariusz.marciniak.operations.data.OperationData;
import pl.mariusz.marciniak.operations.logic.AsyncOperationLogic;
import pl.mariusz.marciniak.operations.logic.OperationLogic;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AsyncTransactionProcessingLogic implements AsyncOperationLogic<Transaction> {

    private Logger logger = LogManager.getLogger(AsyncTransactionProcessingLogic.class);

    @Autowired
    @Qualifier(value="transactionProcessingLogic")
    private OperationLogic<Transaction> logic;

    private ResourceReleaser releaser;

    @Async(value="singleOperationExecutor")
    public Future<Boolean> executeOperation(OperationData<Transaction> data) throws OperationFailedException {
        return logic.executeOperation(data);
    }

    public void operationFinished() {
        logger.debug("operation finished");
        releaser.unlock();
    }
    
    public void setResourceReleaser(ResourceReleaser releaser) {
        this.releaser = releaser;
    }

}
