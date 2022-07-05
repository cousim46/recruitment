# ERD 설계

<img width="918" alt="image" src="https://user-images.githubusercontent.com/67584874/177240882-a3547c91-34fc-432a-931a-1ec06e6a619b.png">

# 테이블 및 인덱스에 대한 DDL

### 회원 테이블

```
create table user_table (user_id varchar(255) not null,
    create_date_time datetime(6),
    update_date_time datetime(6),
    current_point integer not null,
    username VARCHAR(165), 
    primary key (user_id)
 )
```
### 리뷰 테이블

```
create table review (review_id varchar(255) not null, 
      create_date_time datetime(6),
      update_date_time datetime(6),
      content varchar(255), 
      content_point integer not null,
      image varchar(255),
      image_point integer not null,
      place_id varchar(255),
      special_place_point integer not null, 
      total_point integer not null, 
      user_id varchar(255),
      primary key (review_id)
  )

```

### 포인트 내역 테이블

```

create table point_history (point_id varchar(255) not null,
      create_date_time datetime(6),
      update_date_time datetime(6),
      after_point integer not null,
      before_point integer not null,
      increase_or_decrease_point integer not null, 
      user_id varchar(255),
      primary key (point_id)
 )
 ```
 
 ### 인덱스
 
 ```
 create index review_idx on review (place_id)
 ```


# API

# 회원가입
<hr/>


[POST] /

```
{
  "username": "이름"
}
```

- 요청을 하면 응답값으로 userId와 이름을 반환합니다.

# 포인트 부여
<hr/>

[POST] /events

userId : 회원가입 API에서 반환받은 값을 넣어주면 됩니다.

reviewId : 리뷰 작성에서 반환되는 reviewId 넣어주면 됩니다.


### 리뷰 작성
```

{
  "type": "REVIEW", 
  "action": "ADD", 
  "reviewId": "289ce399-f158-4a82-a1d4-d26857c9f923",
  "content": "내용",
  "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
  "userId": "367825f1-d2c5-40c1-9bf8-5337a46f0767",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```
- Action이 ADD일 경우 포인트를 부여하는 항목에 따라 포인트를 부여합니다.
- 같은 유저가 같은 장소에 대해 리뷰를 작성하는지에 대해 체크합니다.
- type, action, reviewId, userId, placeId null값인지 validation합니다.

### 리뷰 수정

```

{
  "type": "REVIEW", 
  "action": "MOD", 
  "reviewId": "289ce399-f158-4a82-a1d4-d26857c9f923",
  "content": "내용",
  "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
  "userId": "367825f1-d2c5-40c1-9bf8-5337a46f0767",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```

- 리뷰 작성때 항목에 해당하지 않았던 항목을 수정사항에서 수정을 하면 포인트를 추가해줍니다.
- 리뷰 작성때 해당되었던 항목을 수정때 해당되었던 항목에서 포인트를 부여하는 조건에 부합하지 않을경우 부여되었던 점수를 차감시킵니다.
- type, action, reviewId, userId, placeId가 null값인지 validation합니다.


### 리뷰 삭제

```

{
  "type": "REVIEW", 
  "action": "DELETE", 
  "reviewId": "289ce399-f158-4a82-a1d4-d26857c9f923",
  "content": "내용",
  "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
  "userId": "367825f1-d2c5-40c1-9bf8-5337a46f0767",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```
- 리뷰를 삭제할경우 리뷰를 작성할때 부여되었던 점수를 차감시킵니다.
- 특정한 장소에서 첫 리뷰를 작성한 사용자가 리뷰를 삭제하지 않고 두번째 사용자가 리뷰를 작성할경우 특정한 장소에서 주는 보너스 포인트를 얻지 못합니다.
- 특정한 장소에서 첫 리뷰를 작성한 사용자가 리뷰를 삭제하고 두번째 사용자가 리뷰를 작성할 경우 특정한 장소에서 주는 보너스 포인트를 얻습니다.
- type, action, reviewId, userId, placeId가 null값인지 validation합니다.

# 포인트 조회
<hr/>

[GET] /point-history?userid=유저ID값

- 리뷰 작성, 수정, 삭제에서 발생했던 포인트 부여, 차감에 대해 해당 userId값을 가진 유저의 포인트 내역을 처리합니다.
- 포인트가 변경될때마다 변경되기전, 증가 또는 감소 값, 변경된 후 값을 확인할 수 있습니다.
- 반환 값의 속성인 "last"가 false일 경우 다음 값이 존재하는 것이고 true일 경우 다음값이 존재하지 않습니다.





# 실행 방법

1. 프로젝트 경로에서 터미널에서 아래와 같은 명령어를 입력합니다.

```
docker-compose up
```

- 콘솔일 경우
```
docker exec -it mysql bash

mysql -u triple -p

password : triple

use triple

```

- workbench일 경우

  username : triple

  password : triple

  port : 3307
  

2. 프로젝트를 실행합니다.


