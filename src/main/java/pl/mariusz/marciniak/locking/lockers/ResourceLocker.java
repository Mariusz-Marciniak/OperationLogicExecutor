package pl.mariusz.marciniak.locking.lockers;

public interface ResourceLocker {
    boolean lock(long... identifiers);
    boolean unlock(long... identifiers);
    boolean isLocked(long identifier);
}
