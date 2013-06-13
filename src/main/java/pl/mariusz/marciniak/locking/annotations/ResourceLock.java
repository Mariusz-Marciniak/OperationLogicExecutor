package pl.mariusz.marciniak.locking.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.mariusz.marciniak.locking.builders.IdentifierBuilder;
import pl.mariusz.marciniak.locking.lockers.ResourceLocker;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceLock {
    Class<? extends IdentifierBuilder<?>> identifierBuilder();
    Class<? extends ResourceLocker> resourceLocker();
    String resourceLockerBeanName();
    String releaseResourceBeanName() default "";
}
