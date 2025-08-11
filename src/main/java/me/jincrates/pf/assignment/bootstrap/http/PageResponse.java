package me.jincrates.pf.assignment.bootstrap.http;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PageResponse<T>(
    int page,
    int itemCount,
    List<T> items
) {

    public static <T> PageResponse<T> of(
        final int page,
        final int size,
        final List<T> contents
    ) {
        final int minSize = Math.min(
            contents.size(),
            size
        );
        return PageResponse.<T>builder()
            .page(page)
            .itemCount(minSize)
            .items(contents.subList(
                0,
                minSize
            ))
            .build();
    }
}
