# CDRI TASK

### prerequisite
- java 11, gradle, h2 <br>
- TaskApplicationTests의 insertBookData() 실행하여 초기 도서 데이터 저장
- application.yml 내용은 하단 참고
  - 처음 실행하는 경우에만 ddl-auto: create, 이후에는 none

## API 문서
> https://documenter.getpostman.com/view/15331545/UzR1KNCu

## 구현 기능
- [X] 도서는 하나 이상의 카테고리에 속할 수 있다.
  - 도서 생성하거나 카테고리 변경 시 하나 이상의 카테고리를 넣을 수 있다.
- [X] 도서는 지은이, 제목의 정보를 가지고 있다.
- [X] 신규 도서는 항상 카테고리가 필요하다.
  - 신규 도서 저장 가능
  - 카테고리를 넣지 않을 경우 400 Bad Request
- [X] 도서는 훼손 또는 분실 등의 이유로 대여가 중단 될 수 있다.
  - 도서 조회 시 대여 가능한 도서만 조회된다.
  - 도서 상태 변경 가능
  - 도서 상태 option : 정상 || 분실 || 훼손
- [X] 도서는 카테고리가 변경될 수 있다.
- [X] 카테고리 별로 도서를 검색 할 수 있다.
- [X] 지은이와 제목으로 도서를 검색 할 수 있다.
  - 지은이로 검색하기 / 제목으로 검색하기 / 키워드로 지은이 혹은 제목 검색

## 구조
```
├─ config
  └─ exception(ExceptionHandler)
├─ controller
├─ domain(Entity)
├─ dto
  └─ req  
  └─ res  
├─ repository
├─ service(Business Logic)
└─ utils(Enum)
```

## application.yml
```
  spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/book
    username: cdri
    password:
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
      path: /h2-console

  jpa:
    hibernate:
      #      ddl-auto: create
      ddl-auto: none
    properties:
      hibernate:
        #        show_sql: true
        format_sql: true
        dialect: org.hibernate.dialect.H2Dialect
        default_batch_fetch_size: 100
    open-in-view: false

logging:
  level:
    org.hibernate.SQL: debug
#    org.hibernate.type: trace

server:
  error:
    include-exception: false
    include-stacktrace: never
```