package pl.mariusz.marciniak.locking.lockers;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class TransactionLocker implements ResourceLocker {

    private final Logger logger = LogManager.getLogger(TransactionLocker.class);

    private Set<Long>    lockedResources;

    @PostConstruct
    private void init() {
        lockedResources = new HashSet<Long>();
    }

    public boolean lock(long... identifiers) {
        Set<Long> resourcesIdentifiers = new HashSet<Long>();
        synchronized (lockedResources) {
            for (long identifier : identifiers) {
                if (lockedResources.contains(identifier)) {
                    return false;
                }
                resourcesIdentifiers.add(identifier);
            }
            logger.debug("acquiring lock on " + resourcesIdentifiers);
            lockedResources.addAll(resourcesIdentifiers);
        }
        return true;
    }

    public boolean unlock(long... identifiers) {
        Set<Long> resourcesIdentifiers = new HashSet<Long>();
        synchronized (lockedResources) {
            for (long identifier : identifiers) {
                if (lockedResources.contains(identifier)) {
                    resourcesIdentifiers.add(identifier);
                }
            }
            logger.debug("releasing lock on " + resourcesIdentifiers);
            lockedResources.removeAll(resourcesIdentifiers);
        }
        return true;
    }

    public boolean isLocked(long identifier) {
        return lockedResources.contains(identifier);
    }

}
