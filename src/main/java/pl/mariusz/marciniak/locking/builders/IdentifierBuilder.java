package pl.mariusz.marciniak.locking.builders;

import pl.mariusz.marciniak.operations.data.OperationData;

public interface IdentifierBuilder<T> {
    long[] getIdentifiers(OperationData<T> operationLogicArgs);
}
