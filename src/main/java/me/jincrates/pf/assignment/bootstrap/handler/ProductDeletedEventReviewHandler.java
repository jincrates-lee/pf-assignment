package me.jincrates.pf.assignment.bootstrap.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.assignment.application.ReviewUseCase;
import me.jincrates.pf.assignment.domain.event.ProductDeleted;
import me.jincrates.pf.assignment.shared.config.AsyncConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDeletedEventReviewHandler {

    private final ReviewUseCase useCase;

    /**
     * 상품 삭제 이벤트를 통해 연관된 리뷰를 삭제한다.
     */
    @Async(AsyncConfig.EVENT_HANDLER_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductDeleted event) {
        log.debug(
            "ProductDeletedEvent handle: {}",
            event
        );
        useCase.deleteAllByProductId(event.productId());
    }
}
