package pl.mariusz.marciniak.locking.builders;

import org.springframework.stereotype.Component;

import pl.mariusz.marciniak.async.entities.Transaction;
import pl.mariusz.marciniak.operations.data.OperationData;

@Component
public class TransactionIdentifierBuilder implements IdentifierBuilder<Transaction> {

    public long[] getIdentifiers(OperationData<Transaction> operationData) {
        return new long[] {operationData.getValueObject().getAccountNumber()};
    }

}
