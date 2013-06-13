package pl.mariusz.marciniak.operations.logic;

public interface OperationExecutor<T> extends OperationLogic<T> {
    void setOperationLogic(OperationLogic<T> logic);
    OperationLogic<T> getOperationLogic();
}
