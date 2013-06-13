package pl.mariusz.marciniak.locking.lockers;


public interface ResourceReleaser {

    void setResourcesIdentifiers(long... identifiers);
    
    void setResourceLocker(ResourceLocker locker);

    boolean unlock();
    

}
