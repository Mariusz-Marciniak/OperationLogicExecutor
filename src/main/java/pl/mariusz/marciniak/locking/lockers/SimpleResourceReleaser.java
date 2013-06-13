package pl.mariusz.marciniak.locking.lockers;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleResourceReleaser implements ResourceReleaser {
    private long[]         identifiers;
    private ResourceLocker locker;

    public void setResourcesIdentifiers(long... identifiers) {
        this.identifiers = identifiers;
    }

    public void setResourceLocker(ResourceLocker locker) {
        this.locker = locker;
    }

    public boolean unlock() {
        return locker.unlock(identifiers);
    }

}
