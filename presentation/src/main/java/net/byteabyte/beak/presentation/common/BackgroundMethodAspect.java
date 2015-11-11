package net.byteabyte.beak.presentation.common;

import java.util.concurrent.Executors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class BackgroundMethodAspect {
  @Around("execution(* *(..)) && @annotation(BackgroundTask)")
  public Object around(final ProceedingJoinPoint point) {
    Executors.newSingleThreadExecutor().execute(new Runnable() {
      @Override public void run() {
        try {
          point.proceed();
        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
      }
    });

    return null;
  }
}
