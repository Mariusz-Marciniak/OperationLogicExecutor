package pl.mariusz.marciniak.operations.logic;

import java.util.concurrent.Future;

import pl.mariusz.marciniak.async.exceptions.OperationFailedException;
import pl.mariusz.marciniak.operations.data.OperationData;


public interface OperationLogic<T> {
    Future<Boolean> executeOperation(OperationData<T> data) throws OperationFailedException;
}
