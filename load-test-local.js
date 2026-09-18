import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    // 1. 서로 다른 ID로 200명 동시 투표
    concurrent_votes: {
      executor: 'per-vu-iterations',
      vus: 200,
      iterations: 1,
      maxDuration: '25s',
      exec: 'concurrentVotes',
    },
    // 2. 동일한 ID로 50건 동시 경쟁 (레이스 컨디션 중복 방어 테스트, 3초 뒤 시작)
    duplicate_race: {
      executor: 'shared-iterations',
      vus: 50,
      iterations: 50,
      maxDuration: '10s',
      startTime: '3s',
      exec: 'duplicateRace',
    },
  },
};

const BASE_URL = 'http://localhost:8080';

// 1. 서로 다른 사용자 200명 동시 투표
export function concurrentVotes() {
  const uniqueId = `local-user-${__VU}-${Date.now()}`;
  const choice = __VU % 2 === 0 ? 'jajang' : 'jjamppong';

  const payload = JSON.stringify({
    choice: choice,
    voterId: uniqueId,
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  const res = http.post(`${BASE_URL}/api/vote`, payload, params);

  check(res, {
    '정상 투표 성공 (201 Created)': (r) => r.status === 201 || r.status === 200,
  });
}

// 2. 완전히 동일한 voterId로 50개 동시 폭격 (중복 방어 검증)
const raceUserId = `local-race-user-${Date.now()}`;

export function duplicateRace() {
  const payload = JSON.stringify({
    choice: 'jajang',
    voterId: raceUserId,
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  const res = http.post(`${BASE_URL}/api/vote`, payload, params);

  check(res, {
    '성공(201) 또는 중복 차단(409 Conflict)': (r) => r.status === 201 || r.status === 409,
    '서버 내부 500 에러 없음': (r) => r.status !== 500,
  });
}
