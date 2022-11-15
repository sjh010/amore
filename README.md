# Amore PMS Project

## Project Description
- 설명 추가

## Package Structure
- PMS Project는 계층형 구조로 package를 구성합니다.
- 패키지 구조의 경우 이견이 있을 수 있으나 해당 프로젝트는 여러 도메인을 관리하지 않는 심플한 구조라고 판단되어 계층형으로 선택하였습니다.
``` 
src_root
├── config
├── controller
├── data
│   ├── entity
│   └── repository
├── dto
├── error
├── service
├── thread
├── type
├── utils
└── validation
resources
└── application.yaml
```
- Description
    - config : Spring Configuration이 존재하는 패키지
    - controller : 컨트롤러 클래스들이 존재하는 패키지. 컨트롤러는 오직 요청과 응답만을 수행한다. (비즈니스 로직이 들어가지 않는다.)
    - data : entity &repository 클래스들이 들어가는 패키지
    - dto : Request, Response 또는 기타 DTO 클래스들이 들어가는 패키지.
    - error : 에러코드, exceptionHandler, specific한 exception 등이 들어가는 패키지
    - service : 비즈니스 로직을 처리하는 클래스들이 존재하는 패키지.
    - thread: 별도의 Thread 클래스가 들어가는 패키지
    - type: 공통으로 사용하는 Enum 클래스가 들어가는 패키지
    - utils : 다양한 Util 클래스가 존재하는 패키지.
    - validation : 객체 검증을 위한 Validation 클래스가 들어가는 패키지
- 해당 구조에 없는 패키지가 추가해야될 경우 개발자 재량에 의해 추가 할 수 있습니다.

## API Structure & Versioning
- {host}:{port}/apis/{version}/{application_domain}/{sub_tasks}
- version
  - v1, v2, v3등으로 정의합니다. (하위호환성 고려 등 상황에 따라 v1.1 가능)
  - 명확한 API 버저닝 관리 전략은 고려하지 않습니다.
- application_domain
  - order, raw_material 등
- sub_tasks
  - HTTP Method로 표현 할 수 없는 행동들에 대해서 추가적으로 정의합니다.

## API Documentation
- API 문서화는 Swagger를 사용하여 진행합니다.
- Application 실행 후 {host}:{port}/swagger-ui.html을 통해 확인 할 수 있습니다.

## Application Profile Strategy
- test
    - 테스트 코드가 실행되는 profile입니다.
    - 테스트 코드에서 의존 구성요소를(e.g. 외부 통신, 다른 서비스 등) 사용할 수 없을 때는 **테스트 더블**을 사용하여 테스트합니다.
- local
    - 로컬 환경에서 실행되는 profile입니다.
- dev, staging, prod
    - 현재 상태에서는 고려하지 않습니다.

## Build and Run (for local)
- Application 빌드는 Maven을 사용하여 빌드합니다.
- 로컬 환경에서 빌드 및 실행은 사전에 작성된 script 파일을 통해 진행됩니다.
- MAC or Linux
  - 프로젝트 root path로 이동 후 build_and_run.sh을 실행합니다. (아래는 예시)
  ```
  ./build_and_run.sh
  ```
- Windows
  - 프로젝트 root path로 이동 후 build_and_run.cmd를 실행합니다. (아래는 예시)
  ```
  build_and_run.cmd
  ```

## Test Case
- 테스트 케이스는 개발자가 개발한 로직을 <span style="color:red">**유의미**</span>히게 검증 하기 위해 사용합니다.
- 테스트 케이스는 Class 혹은 Method 별로 @DisplayName 이용하여 설명합니다.
- 기본적으로 BDD(Given-When-Then) 패턴을 통해 작성합니다.
- 단위 테스트의 경우 빌드시간을 고려하여 Mocking하여 슬라이싱 테스트로 작성합니다. (단, 구성이 힘든 경우 단위 테스트에서도 @SpringBootTest를 사용해도 무방합니다.)
- 통합 테스트의 경우 @SpringBootTest로 검증합니다.
- 통합 테스트로 검증된 클래스의 경우 별도로 단위테스트를 작성하지 않아도 무방합니다.

## CI/CD Principle
- 현재 상태에서는 고려하지 않습니다.
