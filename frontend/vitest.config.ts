import react from '@vitejs/plugin-react'
import { defineConfig } from 'vitest/config'

// vite.config.ts와 별도 파일로 둔 이유: 프로젝트 루트 vite(빌드용)와 vitest가 내부에
// 번들링한 vite 버전이 서로 달라 하나의 defineConfig에 test 옵션을 합치면
// tsc -b 타입체크가 깨진다. 런타임(vitest 실행)에는 영향 없고 타입 충돌만 피하는 목적이다.
export default defineConfig({
  plugins: [react()],
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: './src/setupTests.ts',
  },
})
