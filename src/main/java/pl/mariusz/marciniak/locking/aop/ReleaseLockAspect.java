package pl.mariusz.marciniak.locking.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import pl.mariusz.marciniak.operations.logic.AsyncOperationLogic;

@Aspect
@Component
public class ReleaseLockAspect {

    private Logger             logger = LogManager.getLogger(ReleaseLockAspect.class);

    @After("execution(* pl.mariusz.marciniak.operations.logic.AsyncOperationLogic.executeOperation(pl.mariusz.marciniak.operations.data.OperationData))")
    private void release(JoinPoint jp) {
        logger.debug("releasing resources");
        ((AsyncOperationLogic<?>)jp.getTarget()).operationFinished();
    }


}
