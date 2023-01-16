### FinalProject_SeoJinSu

<br/>

#### 🍒 프로젝트 주제

---

##### 글 작성 및 도서 업로드, 구매, 정산, 출금이 가능한 ebook마켓으로 이루어진 ebook서비스

<br/>

#### 🍒 프로젝트 기간

---

##### 11월 22일 ~ 12월 21일 ( 이후 보완사항 추가 )

<br/>

#### 🍒 노션 페이지

---

#### 쑤북스

https://ssubooks.notion.site

#### 개인노션

https://sjinsu.notion.site

<br/>

#### 🍒 핵심기능

---

#### Member

![회원가입 로그인 아이디 찾기](https://user-images.githubusercontent.com/101937496/212610197-03baae53-7240-46a1-a65f-535da38c5cc8.gif)

##### • 로그인 - Spring Security 사용

##### • 회원가입 – 아이디, 비밀번호, 이메일 필수값

##### • 아이디 찾기 – 이메일로 아이디 찾기 가능

#### Post

![글작성](https://user-images.githubusercontent.com/101937496/212610285-3e44e45a-df72-44a0-b4c8-909aa97ea41c.gif)

##### • 제목, 태그, 내용 입력하여 글 작성 가능

##### • 토스트 에디터로 글 작성

##### • 글목록에서 태그를 누르면 태그에 따라 글이 나옴

#### Book

![작가활동 도서등록](https://user-images.githubusercontent.com/101937496/212610305-996d7976-c7c3-4728-ac46-f24f135951c0.gif)

##### • 작가활동을 시작해야 도서 등록 가능

##### • 도서등록시 태그 선택, 제목, 가격, 태그 추가 가능

#### Cart & Order

![결제 찐](https://user-images.githubusercontent.com/101937496/212614577-47ba7ce9-f50a-4e34-a018-e46826da4c35.gif)


##### • 도서 장바구니 담기

##### • 토스페이먼츠 결제 모듈과 예치금을 이용한 결제

#### Withdraw

![출금](https://user-images.githubusercontent.com/101937496/212610541-938d087a-910e-4157-9bc3-589353a40487.gif)

##### • 출금신청을 할 경우 관리자페이지에서 출금하기 신청 가능

#### Admin

![정산](https://user-images.githubusercontent.com/101937496/212610409-b94cad2d-2f45-41b3-a73b-cfcc3851cd3a.gif)

##### • 관리자 권한은 가진 admin 계정만 관리자 페이지 접근가능

##### • 정산 처리는 예치금 입금으로 이루어진다.

<br/>

#### 🍒 스택

---

##### • 개발 언어 : Java 17

##### • 개발 환경 : InteliJ IDEA

##### • front : html5, css3

##### • back : Spring Boot 2.7.2, Gradle, Spring Data JPA, Spring Security

##### • DB : mariaDB

<br/>

#### 🍒 ERD 이미지

---

<img width="890" alt="deployStructure" src="https://github.com/jinsu291/FinalProject_SeoJinSu/blob/master/ERD%20%EC%9D%B4%EB%AF%B8%EC%A7%80/Ssubooks.png">

<br/>

#### 🍒 아쉬운점 & 보완할 사항

---

##### 첫 개인 프로젝트라 아쉬운점이 많았다. 평소 배운 내용을 써먹는데도 제대로 알지도 못하여 찾아보면서코드를 쓰기도 하였고, 
##### 처음 프로젝트를 할 때 뭐부터 해야할지 몰라서 헤매기도 많이 하였다. 그렇지만프로젝트를 하다보니 자바 공부를 자주하고 틈틈히
##### 하면서 스터디를 진행하는데도 자바가 부족하다는 것을느꼈다. 그렇지만 프로젝트를 진행하다보니 어떤 부분이 중요한지 알게 되어
##### 강약 조절이 가능하였고, 배우는게 많은 유익한 시간이었다.

##### 설계를 잘못하여, Product부분과 Book부분이 엉켜있어서 장바구니 기능이 제대로 작동을 하지 않아서 다시 설계를 하였다.
##### 현재 부족한 부분은 지속적으로 수정해나갈 계획이다.
