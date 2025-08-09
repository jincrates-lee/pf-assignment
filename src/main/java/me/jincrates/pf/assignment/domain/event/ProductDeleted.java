package me.jincrates.pf.assignment.domain.event;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductDeleted(
    Long productId,
    LocalDateTime occurredAt
) implements DomainEvent {

}
