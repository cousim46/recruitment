# test
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

reviewId : 리뷰 작성에서 반환되는 값을 넣어주면 됩니다.


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
- 같은 유저가 같은 장소의 리뷰를 작성하는지 체크합니다.

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


## 실행 방법

1. 프로젝트 경로에 아래와 같은 명령어를 입력합니다.
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


