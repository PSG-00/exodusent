import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    // 1. 서로 다른 ID 200건 동시 투표
    concurrent_votes: {
      executor: 'per-vu-iterations',
      vus: 200,
      iterations: 1,
      maxDuration: '25s',
      exec: 'concurrentVotes',
    },
    // 2. 동일한 ID로 10건 동시 레이스 컨디션 테스트 (5초 뒤 시작)
    duplicate_race: {
      executor: 'shared-iterations',
      vus: 10,
      iterations: 10,
      maxDuration: '10s',
      startTime: '5s',
      exec: 'duplicateRace',
    },
  },
};

const BASE_URL = 'https://fence-ambient-lanes-opponent.trycloudflare.com';

// 1. 서로 다른 사용자 50건 동시 투표
export function concurrentVotes() {
  const uniqueId = `user-vu-${__VU}-${Date.now()}`;
  const choice = __VU % 2 === 0 ? 'jajang' : 'jjamppong';

  const payload = JSON.stringify({
    choice: choice,
    voterId: uniqueId,
  });

  const params = { headers: { 'Content-Type': 'application/json' } };
  const res = http.post(`${BASE_URL}/api/vote`, payload, params);

  check(res, {
    '정상 투표 성공 (201/200)': (r) => r.status === 201 || r.status === 200,
  });
}

// 2. 동일한 voterId로 10개 동시 요청
export function duplicateRace() {
  const duplicateId = 'same-user-race-test';

  const payload = JSON.stringify({
    choice: 'jajang',
    voterId: duplicateId,
  });

  const params = { headers: { 'Content-Type': 'application/json' } };
  const res = http.post(`${BASE_URL}/api/vote`, payload, params);

  check(res, {
    '성공(201) 또는 중복 차단(409)': (r) => r.status === 201 || r.status === 409,
    '서버 500 에러 없음': (r) => r.status !== 500,
  });
}
