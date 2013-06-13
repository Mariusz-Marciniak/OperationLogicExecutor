package pl.mariusz.marciniak.async.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Transaction {
    private long   accountNumber;
    private String transactionType;

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Transaction) {
            if (obj == this) {
                return true;
            } else {
                Transaction that = (Transaction) obj;
                return new EqualsBuilder().append(this.accountNumber, that.accountNumber).append(this.transactionType, that.transactionType).isEquals();
            }
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(accountNumber).append(transactionType).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("transaction type", transactionType).append("account number", accountNumber).toString();
    }
}
