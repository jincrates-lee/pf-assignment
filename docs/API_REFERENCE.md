# API Reference
- **Swagger UI**: [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)
- **API Docs**: [http://localhost:8090/v3/api-docs](http://localhost:8090/v3/api-docs)

## API 목록

| **Method** | **URI** | **Description** |
| --- | --- | --- |
| POST | `/api/products` | 상품 등록 |
| PATCH | `/api/products/{productId}` | 상품 수정 |
| DELETE | `/api/products/{productId}` | 상품 삭제 |
| GET | `/api/products` | 카테고리별 상품 목록 조회 |
| GET | `/api/products/{productId}/reviews` | 상품별 리뷰 목록 조회 |
| POST | `/api/reviews` | 리뷰 등록 |
| PATCH | `/api/reviews/{reviewId}` | 리뷰 수정 |
| DELETE | `/api/reviews/{reviewId}` | 리뷰 삭제 |

### 공통 응답 구조
모든 API는 다음과 같은 공통 응답 형식을 사용합니다.
``` json
{
  "success": boolean,
  "message": string | null,
  "data": object | null
}
```
### 페이징 응답 형식
목록 조회 API는 다음과 같은 페이징 응답 형식을 사용합니다:
``` json
{
  "success": true,
  "message": null,
  "data": {
    "page": number,
    "itemCount": number,
    "items": array
  }
}
```
<br/>

## 상품 API

### 상품 등록 API

```http request
POST http://localhost:8090/api/products
Content-Type: application/json
```

#### Request

```json
{
  "name": "촉촉트릿 북어 80g",
  "sellingPrice": 15000,
  "discountPrice": 2100,
  "brand": "촉촉트릿",
  "categoryIds": [1, 2, 3]
}
```

- `name`: 상품 이름은 필수입니다. (공백 불가)
- `sellingPrice`: 상품 판매가는 필수이며 0원보다 커야합니다.
- `discountPrice`: 상품 할인가는 필수이며 0원 이상이여야 합니다.
- `brand`: 상품 브랜드는 필수입니다. (공백 불가)
- `categoryIds`: 상품 카테고리 목록은 필수입니다. (빈 배열 불가)

#### Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "productId": 1
  }
}
```

### 상품 수정 API

```http request
PATCH http://localhost:8090/api/products/{productId}
Content-Type: application/json
```

#### Request

```json
{
  "name": "촉촉트릿 북어 80g (수정)",
  "sellingPrice": 16000,
  "discountPrice": 2500,
  "brand": "촉촉트릿",
  "categoryIds": [1, 3]
}
```

- `productId`: URL 경로에 포함되는 수정할 상품 ID (필수) 
- 모든 필드는 선택사항이며, 제공된 필드만 업데이트됩니다.
- 각 필드의 검증 규칙은 등록 API와 동일합니다.

#### Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "productId": 3
  }
}
```

### 상품 삭제 API

```http request
DELETE http://localhost:8090/api/products/{productId}
```

#### Request

- `productId`: URL 경로에 포함되는 삭제할 상품 ID (필수) 

#### Response

```json
{
  "success": true,
  "message": null,
  "data": null
}
```

### 카테고리별 상품 조회 API

```http request
GET http://localhost:8090/api/products?categoryId=1&sort=price_asc&page=0&size=5
```

#### Request

- `categoryId`: 조회할 카테고리 ID (필수) 
- `sort`: 정렬 기준 (기본값: "price_asc") 
- `page`: 페이지 번호 (기본값: 0) 
- `size`: 페이지 크기 (기본값: 5) 

#### Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "page": 0,
    "size": 5,
    "content": [
      {
        "id": 1,
        "name": "촉촉트릿 북어 80g",
        "sellingPrice": 15000,
        "discountPrice": 2100,
        "brand": "촉촉트릿",
        "categories": [
          {
            "id": 1,
            "name": "카테고리명",
            "depth": 1,
            "parent": null
          }
        ],
        "discountRate": 14.0,
        "reviewAverageScore": 4.5
      }
    ]
  }
}
```

### 상품별 리뷰 목록 조회 API

```http request
GET http://localhost:8090/api/products/{productId}/reviews?sort=created_desc&page=0&size=5
```

#### Request

- `productId`: URL 경로에 포함되는 상품 ID (필수) 
- `sort`: 정렬 기준 (기본값: "created_desc") 
- `page`: 페이지 번호 (기본값: 0) 
- `size`: 페이지 크기 (기본값: 5) 

#### Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "page": 0,
    "itemCount": 5,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "촉촉트릿 북어 80g",
        "score": 5,
        "content": "우리 강아지가 정말 좋아해요!",
        "createdAt": "2999-12-31 23:59:59"
      }
    ]
  }
}
```
<br/>

## 리뷰 API

### 리뷰 등록 API
```http request
POST http://localhost:8090/api/reviews
Content-Type: application/json
```

#### Request
```json
{
  "productId": 1,
  "content": "우리 강아지가 정말 좋아해요!",
  "score": 5
}
```
- `productId`: 상품 ID는 필수입니다.
- `content`: 리뷰 내용은 필수입니다. (공백 불가, 500자 이하) 
- `score`: 리뷰 점수는 필수이며 1점에서 5점 사이여야 합니다. 

#### Response
```json
{
  "success": true,
  "message": null,
  "data": {
    "reviewId": 1
  }
}
```

### 리뷰 수정 API
```http request
PATCH http://localhost:8090/api/reviews/{reviewId}
Content-Type: application/json
```

#### Request
```json
{
  "content": "수정된 리뷰 내용입니다.",
  "score": 4
}
```
- `reviewId`: URL 경로에 포함되는 수정할 리뷰 ID (필수) 
- `content`: 리뷰 내용은 선택사항입니다. (500자 이하) 
- `score`: 리뷰 점수는 선택사항이며 1점에서 5점 사이여야 합니다. 


#### Response
```json
{
  "success": true,
  "message": null,
  "data": {
    "reviewId": 1
  }
}
```

### 리뷰 삭제 API
```http request
DELETE http://localhost:8090/api/reviews/{reviewId}
```

#### Request
- `reviewId`: URL 경로에 포함되는 삭제할 리뷰 ID (필수) 


#### Response
```json
{
  "success": true,
  "message": null,
  "data": null
}
```
