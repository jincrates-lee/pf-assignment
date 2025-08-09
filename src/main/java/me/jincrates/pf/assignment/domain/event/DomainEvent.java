package me.jincrates.pf.assignment.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {

    LocalDateTime occurredAt();
}
