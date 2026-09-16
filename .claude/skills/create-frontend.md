---
name: create-frontend
description: React 프론트엔드 컴포넌트 및 로직 구현 SOP
version: 9.0.0
---

# ⚙️ 스킬: 프론트엔드 구현 SOP (Standard Operating Procedure)

## 1. Objective
* 뷰와 비즈니스 로직(Redux, API)이 완벽히 분리된 UI 컴포넌트를 구현한다.

## 2. Pre-conditions
* `architecture.md`의 **"4. 프론트엔드 설계 및 코드 제약"**을 숙지한다.

## 3. Execution Pipeline
**Step 1: Saga 및 API 구현**
* `services/api`에 Axios 통신 로직을 작성한다.
* `store/ducks/.../sagas.ts`에 제너레이터 함수(`yield call`) 기반의 비동기 이펙트를 구현한다.

**Step 2: Custom Hook (로직 레이어) 작성**
* 뷰 컴포넌트와 동일한 디렉토리에 `use{컴포넌트명}.ts` 훅을 생성한다.
* 내부에 `useSelector`, `useDispatch`, `useEffect`를 선언하고, 뷰가 필요한 상태와 액션 함수만 리턴한다.

**Step 3: View Component 및 MUI 스타일링**
* `makeStyles`를 사용해 스타일 객체를 선언한다. (MUI v4)
* View 컴포넌트는 오직 Step 2에서 만든 Hook을 호출해 데이터를 렌더링하는 데만 집중한다.

## 4. Constraints
* View 내부에 비즈니스 로직 하드코딩 금지.
* `styled-components` 사용 금지.
