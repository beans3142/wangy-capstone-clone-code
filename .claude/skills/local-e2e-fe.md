---
name: local-e2e-fe
description: Playwright로 실제 브라우저를 띄워 프론트엔드 핵심 화면 흐름(로그인→피드→작성→좋아요→팔로우)이 백엔드와 실제로 붙어 동작하는지 검증한다.
version: 1.0.0
---

# 🎭 스킬: 로컬 E2E 프론트엔드 검증 (Playwright)

`npm run test`(단위 테스트)는 컴포넌트/훅 로직만 검증한다 — 실제 브라우저에서 화면이 렌더링되고 백엔드와 통신까지 되는지는 확인하지 못한다. 이 스킬은 `local-e2e-be.md`로 백엔드가 전부 떠 있는 상태를 전제로 한다.

## 1. 준비 (MUST)
1. `local-e2e-be.md` 절차로 백엔드 전체(게이트웨이 포함)가 떠 있어야 한다.
2. `frontend/`에서 `npx playwright install`(최초 1회) → `npm run dev`로 프론트 개발서버 기동.
3. Playwright 설정이 없으면 `frontend/playwright.config.ts`에 `baseURL: http://localhost:5173`(또는 실제 dev 포트) 추가.

## 2. 시나리오 (MUST, `frontend/e2e/core-flow.spec.ts`)
```ts
import { test, expect } from '@playwright/test';

test('회원가입부터 좋아요까지 핵심 플로우', async ({ page }) => {
  const email = `e2e-${Date.now()}@example.com`;

  await page.goto('/signup');
  await page.fill('[name=email]', email);
  await page.fill('[name=password]', 'Passw0rd!');
  await page.fill('[name=nickname]', 'e2e');
  await page.click('button[type=submit]');

  await expect(page).toHaveURL(/login/);
  await page.fill('[name=email]', email);
  await page.fill('[name=password]', 'Passw0rd!');
  await page.click('button[type=submit]');

  await expect(page).toHaveURL(/feed/);

  await page.fill('[name=content]', 'Playwright E2E 트윗');
  await page.click('button:has-text("작성")');
  await expect(page.locator('text=Playwright E2E 트윗')).toBeVisible();

  await page.click('[data-testid=like-button]');
  await expect(page.locator('[data-testid=like-count]')).not.toHaveText('0');
});
```

## 3. 판정 기준 (MUST)
* 인증되지 않은 상태로 `/feed` 직접 접근 시 `/login`으로 리다이렉트되는 케이스도 별도 테스트로 포함한다.
* 네트워크 요청 실패(백엔드가 안 떠 있는 경우) 시 화면이 하얗게 죽지 않고 에러 상태를 보여주는지 확인한다.
* `data-testid` 속성이 컴포넌트에 없으면, 셀렉터를 텍스트 기반으로 짜지 말고 컴포넌트에 `data-testid`를 추가하는 쪽을 우선한다(테스트 안정성).

## 4. 실행
```bash
npx playwright test frontend/e2e/core-flow.spec.ts --headed=false
```
CI 등 이 프로젝트 범위 밖에서는 헤드리스로만 돌린다. 실패 시 `playwright-report/`의 스크린샷/트레이스로 원인을 확인한다.
