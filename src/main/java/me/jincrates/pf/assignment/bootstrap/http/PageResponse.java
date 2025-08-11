package me.jincrates.pf.assignment.bootstrap.http;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PageResponse<T>(
    int page,
    boolean hasNext,
    List<T> contents
) {

    public static <T> PageResponse<T> of(
        int page,
        int size,
        List<T> contents
    ) {
        return PageResponse.<T>builder()
            .page(page)
            .hasNext(contents.size() > size)
            .contents(contents.subList(
                0,
                Math.min(
                    contents.size(),
                    size
                )
            ))
            .build();
    }
}
