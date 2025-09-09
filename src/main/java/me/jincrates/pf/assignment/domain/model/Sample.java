package me.jincrates.pf.assignment.domain.model;

import lombok.Builder;

@Builder
public record Sample(
    Long id,
    String message
) {

}
