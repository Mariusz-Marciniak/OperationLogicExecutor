package pl.mariusz.marciniak.locking.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import pl.mariusz.marciniak.locking.annotations.ResourceLock;
import pl.mariusz.marciniak.locking.builders.IdentifierBuilder;
import pl.mariusz.marciniak.locking.lockers.ResourceLocker;
import pl.mariusz.marciniak.locking.lockers.ResourceReleaser;
import pl.mariusz.marciniak.operations.data.OperationData;
import pl.mariusz.marciniak.operations.logic.AsyncOperationLogic;
import pl.mariusz.marciniak.operations.logic.OperationExecutor;

@Aspect
@Component
public class ResourceLockAspect {

    private Logger             logger = LogManager.getLogger(ResourceLockAspect.class);

    @Autowired
    private ApplicationContext appContext;

    @Around("execution(* pl.mariusz.marciniak.operations.logic.OperationLogic.executeOperation(pl.mariusz.marciniak.operations.data.OperationData))"
           +" && @annotation(resourceLock)")
    private <T> Object execute(ProceedingJoinPoint pjp, ResourceLock resourceLock) throws Throwable {
        Object result = null;
        IdentifierBuilder<T> builder = resolveIdentifierBuilder(resourceLock);
        ResourceLocker locker = resolveResourceLocker(resourceLock);
        OperationData<T> operationData = (OperationData<T>) pjp.getArgs()[0];
        long[] objectsToLockIdentifiers = createIdentifiers(builder, operationData);
        boolean releaseResourceDefined = resourceLock.releaseResourceBeanName().length()>0;
        initResourceReleaserOnAsyncOperationLogic(pjp.getTarget(), resourceLock.releaseResourceBeanName(), locker, objectsToLockIdentifiers);

        if (locker.lock(objectsToLockIdentifiers)) {
            try {
                result = pjp.proceed();
            } finally {
                if(!releaseResourceDefined)
                    locker.unlock(objectsToLockIdentifiers);
            }
        } else {
            logger.info("Cannot perform operation " + operationData.getName() + ". Locked resources "
                            + new StrBuilder().appendWithSeparators(ArrayUtils.toObject(objectsToLockIdentifiers), ",").toString());
        }
        return result;
    }

    private void initResourceReleaserOnAsyncOperationLogic(Object target, String resourceReleaseBeanName, ResourceLocker locker, long[] objectsToLockIdentifiers) {
        AsyncOperationLogic<?> asyncOperationLogic = null;
        if (target instanceof AsyncOperationLogic<?>) {
            asyncOperationLogic = (AsyncOperationLogic<?>)target;
        } else if(target instanceof OperationExecutor<?>) {
            OperationExecutor<?> operationExecutor = (OperationExecutor<?>)target;
            if (operationExecutor.getOperationLogic() instanceof AsyncOperationLogic<?>) {
                asyncOperationLogic = (AsyncOperationLogic<?>)operationExecutor.getOperationLogic();
            }            
        }
        if(asyncOperationLogic != null) {
            ResourceReleaser releaser = appContext.getBean(ResourceReleaser.class);
            releaser.setResourceLocker(locker);
            releaser.setResourcesIdentifiers(objectsToLockIdentifiers);
            asyncOperationLogic.setResourceReleaser(releaser);
        }
    }

    private <T> long[] createIdentifiers(IdentifierBuilder<T> builder, OperationData<T> operationData) {
        return builder.getIdentifiers(operationData);
    }

    private <T> IdentifierBuilder<T> resolveIdentifierBuilder(ResourceLock annotation) {
        Class<? extends IdentifierBuilder<T>> identifierBuilder = (Class<? extends IdentifierBuilder<T>>) annotation.identifierBuilder();
        return appContext.getBean(identifierBuilder);
    }

    private ResourceLocker resolveResourceLocker(ResourceLock annotation) {
        Class<? extends ResourceLocker> resourceLockerClass = annotation.resourceLocker();
        String resourceLockerBeanName = annotation.resourceLockerBeanName();
        return appContext.getBean(resourceLockerBeanName, resourceLockerClass);
    }

}
