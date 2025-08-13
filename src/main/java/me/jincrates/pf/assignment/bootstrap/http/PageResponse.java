package me.jincrates.pf.assignment.bootstrap.http;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Schema(description = "공통 페이지 응답 객체")
@Builder(access = AccessLevel.PRIVATE)
public record PageResponse<T>(
    @Schema(description = "현재 페이지", example = "0")
    int page,
    @Schema(description = "조회 리스트 수", example = "5")
    int itemCount,
    @Schema(description = "조회 리스트")
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
