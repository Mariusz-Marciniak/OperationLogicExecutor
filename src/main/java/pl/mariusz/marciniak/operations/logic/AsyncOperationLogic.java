package pl.mariusz.marciniak.operations.logic;

import pl.mariusz.marciniak.locking.lockers.ResourceReleaser;


public interface AsyncOperationLogic<T> extends OperationLogic<T> {
    
    public void operationFinished();
    
    public void setResourceReleaser(ResourceReleaser releaser);
    
}
