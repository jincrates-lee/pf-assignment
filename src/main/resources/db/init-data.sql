-- #################### 카테고리 ##########################

-- 1단계 카테고리 (루트 카테고리)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(1, '강아지', 1, NULL),
(2, '고양이', 1, NULL);

-- 2단계 카테고리 (강아지 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(3, '간식', 2, 1),
(4, '사료', 2, 1),
(5, '용품', 2, 1);

-- 2단계 카테고리 (고양이 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(6, '간식', 2, 2),
(7, '사료', 2, 2),
(8, '용품', 2, 2);

-- 3단계 카테고리 (강아지 > 간식 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(9, '동결/건조', 3, 3),
(10, '덴탈껌', 3, 3),
(11, '저키', 3, 3),
(12, '사사미/육포', 3, 3);

-- 3단계 카테고리 (강아지 > 사료 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(13, '건식사료', 3, 4),
(14, '어덜트(1-7세)', 3, 4),
(15, '맘마샘플', 3, 4),
(16, '퍼피(1세미만)', 3, 4),
(17, '피부/피모', 3, 4),
(18, '장/소화', 3, 4);

-- 3단계 카테고리 (강아지 > 용품 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(19, '배변용품', 3, 5);

-- 3단계 카테고리 (고양이 > 간식 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(20, '동결/건조', 3, 6),
(21, '간식 파우치', 3, 6);

-- 3단계 카테고리 (고양이 > 사료 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(22, '건식사료', 3, 7),
(23, '어덜트(1-7세)', 3, 7),
(24, '요로기계', 3, 7),
(25, '헤어볼', 3, 7),
(26, '구강/치아', 3, 7),
(27, '장/소화', 3, 7),
(28, '기타', 3, 7),
(29, '주식캔', 3, 7),
(30, '전연령', 3, 7);

-- 3단계 카테고리 (고양이 > 용품 하위)
INSERT INTO categories (id, name, depth, parent_id) VALUES
(31, '낚시대/레이져', 3, 8);
