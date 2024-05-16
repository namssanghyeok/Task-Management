# 스프링 기초 개인 과제

9단계까지 진행했습니다.

## api명세서

|기능|method|url|request|response|
|---|---|---|---|---|
|task 생성|POST|/api/task|{"title": "머리 자르기","content": "예약","assignee": "hello@gmail.com","password": "password"}|{"id": 0,"title": "string","content": "string","assignee": "string","createdAt": "2024-05-16T13:54:43.422Z","updatedAt": "2024-05-16T13:54:43.422Z"}|
|task 조회|GET|/api/task/{id}|-|{"id": 0,"title": "string","content": "string","assignee": "string","createdAt": "2024-05-16T13:56:39.322Z","updatedAt": "2024-05-16T13:56:39.322Z"}|
|task 수정|PUT|/api/task/{id}|{"title": "string","content": "string","assignee": "string","password": "string"}|{"id": 0,"title": "string","content": "string","assignee": "string","createdAt": "2024-05-16T13:56:39.322Z","updatedAt": "2024-05-16T13:56:39.322Z"}|
|task 삭제|DELETE|/api/task/{id}|{"password": "string"}|{"id": 0,"title": "string","content": "string","assignee": "string","createdAt": "2024-05-16T13:56:39.322Z","updatedAt": "2024-05-16T13:56:39.322Z"}|
|task 전체조회|GET|/api/task|-|[{"id": 0,"title": "string","content": "string","assignee": "string","createdAt": "2024-05-16T13:56:39.322Z","updatedAt": "2024-05-16T13:56:39.322Z"}]|
|task 첨부파일 조회|GET|/api/task/{id}/attachment|-|[{"id": 0,"title": "string","content": "string","assignee": "string","createdAt": "2024-05-16T13:56:39.322Z","updatedAt": "2024-05-16T13:56:39.322Z"}]|
|task 첨부파일 zip 다운|GET|/api/task/{id}/attachment/download|-|-|
|파일 다운로드|GET|/api/file/{id}|-|-|
|파일 삭제|DELETE|/api/file/{id}|{"password": "string"}|-|








## ERD

[dbdocs](https://dbdocs.io/simian114/task?table=upload_file&schema=public&view=table_structure)


![erd](https://github.com/namssanghyeok/Task-Management/assets/155861999/21945109-9f47-4602-aef8-b13673e9a97a)
