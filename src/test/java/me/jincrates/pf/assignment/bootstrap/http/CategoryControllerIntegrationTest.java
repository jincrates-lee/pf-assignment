package me.jincrates.pf.assignment.bootstrap.http;

import me.jincrates.pf.assignment.IntegrationTestSupport;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("카테고리 API 통합 테스트")
class CategoryControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private CategoryRepository repository;

    private Category parentCategory;

    @BeforeEach
    void setUp() {
        parentCategory = repository.save(
            Category.builder()
                .name("category-parent")
                .depth(1)
                .build()
        );
    }

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class CreateCategoryTest {

        @Test
        @DisplayName("상위 카테고리 없이 카테고리를 생성하면 1단계 루트 카테고리가 생성된다")
        void createRootCategoryWhenNoParentProvided() {
            // given
            String requestBody = """
                {
                    "name": "category-1"
                }
                """;

            // when
            webTestClient.post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.categoryId").isNumber();
        }

        @Test
        @DisplayName("상위 카테고리를 지정하여 카테고리를 생성하면 하위 카테고리가 생성된다")
        void createChildCategoryWhenParentCategoryExists() {
            // given
            String requestBody = String.format(
                """
                    {
                         "name": "category-1",
                         "parentId": %d
                    }
                    """,
                parentCategory.id()
            );

            // when
            webTestClient.post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(null)
                .jsonPath("$.data.categoryId").isNumber();
        }

        @Test
        @DisplayName("존재하지 않는 상위 카테고리를 지정하면 카테고리 생성이 실패한다")
        void failToCreateCategoryWhenParentCategoryNotExists() {
            // given
            Long nonExistentParentId = 99999L;
            String requestBody = String.format(
                """
                    {
                         "name": "category-1",
                         "parentId": %d
                    }
                    """,
                nonExistentParentId
            );

            // when
            webTestClient.post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("상위 카테고리를 찾을 수 없습니다.")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("카테고리명이 비어있으면 카테고리 생성이 실패한다")
        void failToCreateCategoryWhenNameIsEmpty() {
            // given
            String requestBody = """
                {
                    "name": ""
                }
                """;

            // when
            webTestClient.post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("카테고리 이름은 필수입니다. 입력된 값: [name=]")
                .jsonPath("$.data").isEmpty();
        }

        @Test
        @DisplayName("카테고리명이 null이면 카테고리 생성이 실패한다")
        void failToCreateCategoryWhenNameIsNull() {
            // given
            String requestBody = """
                {
                     "name": null
                }
                """;

            // when
            webTestClient.post()
                .uri("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("카테고리 이름은 필수입니다. 입력된 값: [name=null]")
                .jsonPath("$.data").isEmpty();
        }
    }
}
