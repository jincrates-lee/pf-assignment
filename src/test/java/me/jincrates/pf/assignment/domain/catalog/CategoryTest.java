package me.jincrates.pf.assignment.domain.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CategoryTest {

    @Test
    @DisplayName("유효한 카테고리를 생성할 수 있다")
    void createValidCategory() {
        // given & when & then
        assertDoesNotThrow(() -> {
            Category category = Category.builder()
                .id(1L)
                .name("강아지")
                .depth(1)
                .parent(null)
                .build();

            assertThat(category.id()).isEqualTo(1L);
            assertThat(category.name()).isEqualTo("강아지");
            assertThat(category.depth()).isEqualTo(1);
            assertThat(category.parent()).isNull();
        });
    }

    @Test
    @DisplayName("부모 카테고리가 있는 카테고리를 생성할 수 있다")
    void createCategoryWithParent() {
        // given
        Category parentCategory = Category.builder()
            .id(1L)
            .name("강아지")
            .depth(1)
            .parent(null)
            .build();

        // when & then
        assertDoesNotThrow(() -> {
            Category childCategory = Category.builder()
                .id(2L)
                .name("간식")
                .depth(parentCategory.depth() + 1)
                .parent(parentCategory)
                .build();

            assertThat(childCategory.parent()).isEqualTo(parentCategory);
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("카테고리 이름이 null이거나 공백이면 예외가 발생한다")
    void throwExceptionWhenNameIsNullOrBlank(String invalidName) {
        // given & when & then
        assertThatThrownBy(() -> Category.builder()
            .id(1L)
            .name(invalidName)
            .depth(1)
            .parent(null)
            .build()
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("카테고리 이름은 필수입니다.");
    }

    @Test
    @DisplayName("카테고리 단계가 null이면 예외가 발생한다")
    void throwExceptionWhenDepthIsNull() {
        // given & when & then
        assertThatThrownBy(() -> Category.builder()
            .id(1L)
            .name("카테고리")
            .depth(null)
            .parent(null)
            .build()
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("카테고리 단계는 필수입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -100})
    @DisplayName("카테고리 단계가 0 이하이면 예외가 발생한다")
    void throwExceptionWhenDepthIsZeroOrNegative(int invalidDepth) {
        // given & when & then
        assertThatThrownBy(() -> Category.builder()
            .id(1L)
            .name("카테고리")
            .depth(invalidDepth)
            .parent(null)
            .build()
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("카테고리 단계는 0보다 커야합니다.");
    }
}
