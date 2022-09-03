package com.reactivespring.util;

import com.reactivespring.exception.MoviesInfoServerException;
import com.reactivespring.exception.ReviewsServerException;
import java.time.Duration;
import lombok.experimental.UtilityClass;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

@UtilityClass
public class RetryUtil {

    public Retry retrySpec() {
        return Retry.fixedDelay(3L, Duration.ofSeconds(1L))
            .filter(throwable -> throwable instanceof MoviesInfoServerException
                || throwable instanceof ReviewsServerException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()));
    }

}
