package me.jincrates.pf.assignment.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;

import java.util.Optional;
import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryUseCase 테스트")
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService categoryService;

    private Category rootCategory;
    private Category childCategory;
    private Category grandChildCategory;

    @BeforeEach
    void setUp() {
        rootCategory = Category.builder()
            .id(1L)
            .name("반려동물")
            .depth(1)
            .parent(null)
            .build();

        childCategory = Category.builder()
            .id(2L)
            .name("강아지")
            .depth(2)
            .parent(rootCategory)
            .build();

        grandChildCategory = Category.builder()
            .id(3L)
            .name("소형견")
            .depth(3)
            .parent(childCategory)
            .build();
    }

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class CreateCategoryTest {

        @Nested
        @DisplayName("루트 카테고리 생성")
        class CreateRootCategoryTest {

            @Test
            @DisplayName("정상적으로 루트 카테고리를 생성한다")
            void shouldCreateRootCategorySuccessfully() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "반려동물",
                    null
                    // parentId가 null이면 루트 카테고리
                );

                Category savedCategory = Category.builder()
                    .id(1L)
                    .name("반려동물")
                    .depth(1)
                    .parent(null)
                    .build();

                given(repository.save(any(Category.class)))
                    .willReturn(savedCategory);

                // when
                CreateCategoryResponse response = categoryService.create(request);

                // then
                assertNotNull(response);
                assertEquals(
                    1L,
                    response.categoryId()
                );

                verify(repository).save(argThat(category ->
                    category.name().equals("반려동물") &&
                        category.depth().equals(1) &&
                        category.parent() == null
                ));
                verify(
                    repository,
                    never()
                ).findById(any());
            }

            @Test
            @DisplayName("루트 카테고리는 depth가 1로 설정된다")
            void shouldSetDepthToOneForRootCategory() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "반려동물",
                    null
                );

                Category savedCategory = Category.builder()
                    .id(1L)
                    .name("반려동물")
                    .depth(1)
                    .parent(null)
                    .build();

                given(repository.save(any(Category.class)))
                    .willReturn(savedCategory);

                // when
                categoryService.create(request);

                // then
                verify(repository).save(argThat(category ->
                    category.depth().equals(1)
                ));
            }

            @Test
            @DisplayName("빈 이름으로 루트 카테고리 생성 시 도메인 검증에 의해 예외가 발생한다")
            void shouldThrowExceptionWhenNameIsBlank() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "   ",
                    // 빈 이름
                    null
                );

                // when & then
                BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categoryService.create(request)
                );

                assertEquals(
                    "카테고리 이름은 필수입니다.",
                    exception.getMessage()
                );
                verify(
                    repository,
                    never()
                ).save(any(Category.class));
            }

            @Test
            @DisplayName("null 이름으로 루트 카테고리 생성 시 도메인 검증에 의해 예외가 발생한다")
            void shouldThrowExceptionWhenNameIsNull() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    null,
                    null
                );

                // when & then
                BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categoryService.create(request)
                );

                assertEquals(
                    "카테고리 이름은 필수입니다.",
                    exception.getMessage()
                );
                verify(
                    repository,
                    never()
                ).save(any(Category.class));
            }
        }

        @Nested
        @DisplayName("하위 카테고리 생성")
        class CreateChildCategoryTest {

            @Test
            @DisplayName("정상적으로 하위 카테고리를 생성한다")
            void shouldCreateChildCategorySuccessfully() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "강아지",
                    1L
                    // 상위 카테고리 ID
                );

                Category savedCategory = Category.builder()
                    .id(2L)
                    .name("강아지")
                    .depth(2)
                    .parent(rootCategory)
                    .build();

                given(repository.findById(1L))
                    .willReturn(Optional.of(rootCategory));
                given(repository.save(any(Category.class)))
                    .willReturn(savedCategory);

                // when
                CreateCategoryResponse response = categoryService.create(request);

                // then
                assertNotNull(response);
                assertEquals(
                    2L,
                    response.categoryId()
                );

                verify(repository).findById(1L);
                verify(repository).save(argThat(category ->
                    category.name().equals("강아지") &&
                        category.depth().equals(2) &&
                        category.parent().equals(rootCategory)
                ));
            }

            @Test
            @DisplayName("하위 카테고리의 depth는 상위 카테고리 depth + 1로 설정된다")
            void shouldSetDepthToParentDepthPlusOne() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "소형견",
                    2L
                    // 2depth 카테고리의 하위 카테고리
                );

                Category savedCategory = Category.builder()
                    .id(3L)
                    .name("소형견")
                    .depth(3)
                    .parent(childCategory)
                    .build();

                given(repository.findById(2L))
                    .willReturn(Optional.of(childCategory));
                given(repository.save(any(Category.class)))
                    .willReturn(savedCategory);

                // when
                categoryService.create(request);

                // then
                verify(repository).findById(2L);
                verify(repository).save(argThat(category ->
                    category.depth().equals(3) &&
                        category.parent().equals(childCategory)
                ));
            }

            @Test
            @DisplayName("존재하지 않는 상위 카테고리 ID로 생성 시 예외가 발생한다")
            void shouldThrowExceptionWhenParentCategoryNotFound() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "강아지",
                    999L
                    // 존재하지 않는 상위 카테고리 ID
                );

                given(repository.findById(999L))
                    .willReturn(Optional.empty());

                // when & then
                BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categoryService.create(request)
                );

                assertEquals(
                    "상위 카테고리를 찾을 수 없습니다.",
                    exception.getMessage()
                );
                verify(repository).findById(999L);
                verify(
                    repository,
                    never()
                ).save(any(Category.class));
            }

            @Test
            @DisplayName("빈 이름으로 하위 카테고리 생성 시 도메인 검증에 의해 예외가 발생한다")
            void shouldThrowExceptionWhenChildCategoryNameIsBlank() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "",
                    1L
                );

                given(repository.findById(1L))
                    .willReturn(Optional.of(rootCategory));

                // when & then
                BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categoryService.create(request)
                );

                assertEquals(
                    "카테고리 이름은 필수입니다.",
                    exception.getMessage()
                );
                verify(repository).findById(1L);
                verify(
                    repository,
                    never()
                ).save(any(Category.class));
            }
        }

        @Nested
        @DisplayName("깊은 계층 카테고리 생성")
        class CreateDeepLevelCategoryTest {

            @Test
            @DisplayName("3단계 깊이의 카테고리를 생성할 수 있다")
            void shouldCreateThirdLevelCategory() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "소형견",
                    2L
                    // 2단계 카테고리의 하위
                );

                Category savedCategory = Category.builder()
                    .id(3L)
                    .name("소형견")
                    .depth(3)
                    .parent(childCategory)
                    .build();

                given(repository.findById(2L))
                    .willReturn(Optional.of(childCategory));
                given(repository.save(any(Category.class)))
                    .willReturn(savedCategory);

                // when
                CreateCategoryResponse response = categoryService.create(request);

                // then
                assertNotNull(response);
                assertEquals(
                    3L,
                    response.categoryId()
                );

                verify(repository).findById(2L);
                verify(repository).save(argThat(category ->
                    category.name().equals("소형견") &&
                        category.depth().equals(3) &&
                        category.parent().equals(childCategory)
                ));
            }

            @Test
            @DisplayName("4단계 깊이의 카테고리를 생성할 수 있다")
            void shouldCreateFourthLevelCategory() {
                // given
                CreateCategoryRequest request = new CreateCategoryRequest(
                    "치와와",
                    3L
                    // 3단계 카테고리의 하위
                );

                Category savedCategory = Category.builder()
                    .id(4L)
                    .name("치와와")
                    .depth(4)
                    .parent(grandChildCategory)
                    .build();

                given(repository.findById(3L))
                    .willReturn(Optional.of(grandChildCategory));
                given(repository.save(any(Category.class)))
                    .willReturn(savedCategory);

                // when
                CreateCategoryResponse response = categoryService.create(request);

                // then
                assertNotNull(response);
                assertEquals(
                    4L,
                    response.categoryId()
                );

                verify(repository).findById(3L);
                verify(repository).save(argThat(category ->
                    category.name().equals("치와와") &&
                        category.depth().equals(4) &&
                        category.parent().equals(grandChildCategory)
                ));
            }
        }
    }

    @Nested
    @DisplayName("카테고리 생성 헬퍼 메서드 테스트")
    class HelperMethodsTest {

        @Test
        @DisplayName("parentId가 null인 경우 루트 카테고리로 판단한다")
        void shouldIdentifyRootCategoryWhenParentIdIsNull() {
            // given
            CreateCategoryRequest rootRequest = new CreateCategoryRequest(
                "반려동물",
                null
            );

            Category savedCategory = Category.builder()
                .id(1L)
                .name("반려동물")
                .depth(1)
                .parent(null)
                .build();

            given(repository.save(any(Category.class)))
                .willReturn(savedCategory);

            // when
            categoryService.create(rootRequest);

            // then
            verify(repository).save(argThat(category ->
                category.depth().equals(1) &&
                    category.parent() == null
            ));
            verify(
                repository,
                never()
            ).findById(any());
        }

        @Test
        @DisplayName("parentId가 존재하는 경우 하위 카테고리로 판단한다")
        void shouldIdentifyChildCategoryWhenParentIdExists() {
            // given
            CreateCategoryRequest childRequest = new CreateCategoryRequest(
                "강아지",
                1L
            );

            Category savedCategory = Category.builder()
                .id(2L)
                .name("강아지")
                .depth(2)
                .parent(rootCategory)
                .build();

            given(repository.findById(1L))
                .willReturn(Optional.of(rootCategory));
            given(repository.save(any(Category.class)))
                .willReturn(savedCategory);

            // when
            categoryService.create(childRequest);

            // then
            verify(repository).findById(1L);
            verify(repository).save(argThat(category ->
                category.depth().equals(2) &&
                    category.parent().equals(rootCategory)
            ));
        }
    }

    @Nested
    @DisplayName("응답 변환 테스트")
    class ResponseConversionTest {

        @Test
        @DisplayName("저장된 카테고리를 올바른 응답 형태로 변환한다")
        void shouldConvertSavedCategoryToResponse() {
            // given
            CreateCategoryRequest request = new CreateCategoryRequest(
                "반려동물",
                null
            );

            Category savedCategory = Category.builder()
                .id(1L)
                .name("반려동물")
                .depth(1)
                .parent(null)
                .build();

            given(repository.save(any(Category.class)))
                .willReturn(savedCategory);

            // when
            CreateCategoryResponse response = categoryService.create(request);

            // then
            assertNotNull(response);
            assertEquals(
                1L,
                response.categoryId()
            );
        }

        @Test
        @DisplayName("하위 카테고리도 올바른 응답 형태로 변환한다")
        void shouldConvertChildCategoryToResponse() {
            // given
            CreateCategoryRequest request = new CreateCategoryRequest(
                "강아지",
                1L
            );

            Category savedCategory = Category.builder()
                .id(2L)
                .name("강아지")
                .depth(2)
                .parent(rootCategory)
                .build();

            given(repository.findById(1L))
                .willReturn(Optional.of(rootCategory));
            given(repository.save(any(Category.class)))
                .willReturn(savedCategory);

            // when
            CreateCategoryResponse response = categoryService.create(request);

            // then
            assertNotNull(response);
            assertEquals(
                2L,
                response.categoryId()
            );
        }
    }

    @Nested
    @DisplayName("트랜잭션 경계 테스트")
    class TransactionTest {

        @Test
        @DisplayName("카테고리 생성은 트랜잭션 내에서 실행된다")
        void shouldExecuteInTransaction() {
            // given
            CreateCategoryRequest request = new CreateCategoryRequest(
                "반려동물",
                null
            );

            Category savedCategory = Category.builder()
                .id(1L)
                .name("반려동물")
                .depth(1)
                .parent(null)
                .build();

            given(repository.save(any(Category.class)))
                .willReturn(savedCategory);

            // when
            CreateCategoryResponse response = categoryService.create(request);

            // then
            assertNotNull(response);
        }
    }
}
