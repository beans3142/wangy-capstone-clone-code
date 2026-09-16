---
name: review-frontend-state
description: [Master Agent] 프론트엔드 React 렌더링 루프 및 Saga 통신 리뷰
version: 7.0.0
---

# 🕵️ 스킬: 프론트엔드 상태관리 및 렌더 리뷰

React SPA의 성능 저하와 잘못된 상태 동기화를 잡아냅니다.

## 🚨 거절(Reject) 기준
1. **비동기 통신 규약 위반:** Redux Saga(`sagas.ts`)를 사용하지 않고, UI 컴포넌트(`*.tsx`) 내부의 `useEffect` 등에서 `axios.get()`을 직접 호출한 경우.
2. **무한 렌더 루프:** `useEffect`의 의존성 배열(`dependency array`)이 누락되었거나, 의도치 않게 참조가 변경되는 객체를 넣어 무한 리렌더링 및 무한 API 호출이 발생할 여지가 있는 경우.
3. **Redux 오염:** 모달 창의 열림/닫힘 상태나 입력 폼의 텍스트 같은 극도로 지엽적인(ephemeral) UI 상태를 전역 Redux Store(`reducer.ts`)에 저장한 경우. (반드시 `useState` 로컬 상태로 처리해야 함).
