package pl.mariusz.marciniak.operations.logic.impl;

import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import pl.mariusz.marciniak.async.entities.Transaction;
import pl.mariusz.marciniak.async.exceptions.OperationFailedException;
import pl.mariusz.marciniak.locking.annotations.ResourceLock;
import pl.mariusz.marciniak.locking.builders.TransactionIdentifierBuilder;
import pl.mariusz.marciniak.locking.lockers.ResourceReleaser;
import pl.mariusz.marciniak.locking.lockers.TransactionLocker;
import pl.mariusz.marciniak.operations.data.OperationData;
import pl.mariusz.marciniak.operations.logic.OperationExecutor;
import pl.mariusz.marciniak.operations.logic.OperationLogic;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionProcessingExecutor implements OperationExecutor<Transaction> {

    private OperationLogic<Transaction> logic;

    @ResourceLock(identifierBuilder = TransactionIdentifierBuilder.class, resourceLocker = TransactionLocker.class, resourceLockerBeanName = "transactionLocker")
    @Async(value="singleOperationExecutor")
    public Future<Boolean> executeOperation(OperationData<Transaction> data) throws OperationFailedException {
        return logic.executeOperation(data);
    }

    @Autowired
    @Qualifier(value = "transactionProcessingLogic")
    public void setOperationLogic(OperationLogic<Transaction> logic) {
        this.logic = logic;
    }

    public OperationLogic<Transaction> getOperationLogic() {
        return logic;
    }

}
