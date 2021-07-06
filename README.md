# BlogAPIServerMybatis

### 구현한 기능
1. 게시글 API
    * swagger 도입
    * 테스트코드 작성
        * mysql db를 직접 쓰지 말고 테스트용 h2 db를 도입하기


### 구현할 기능

0.  게시글 기능
    * 페이징 기능 
    * Role에 따른 리소스 인가(jwt token 설정)

1.  회원 관리 기능
    * spring security 도입
        * 기본 로그인
            * 기본 로그인 기능은 구현이 어느정도 되었음 => 테스트 잘 작성 및 리펙토링
            * 유저와 관련한 CRUD구현
            * 로그인 한 사용자의 인증 토큰 관련하여 인가 관련한 작업 추가 
        * 소셜 로그인
            * https://engkimbs.tistory.com/849
    * 회원 정보 CRUD구현

2.  검색 기능 
    * 게시글의 제목을 통한 검색
    * 추후 게시글에 tag를 달아서 검색하는등 기능 추가 예정

* 참고
    * spring boot2로 rest api만들기: https://daddyprogrammer.org/post/404/spring-boot2-design-api-interface-and-data-structure/
