import http from 'k6/http';
import { check, group } from 'k6';

export const options = {
    // 50명의 가상 사용자가 5초 동안 집중적으로 동시 요청
    scenarios: {
        // 1. 서로 다른 ID로 동시 투표
        concurrent_votes: {
            executor: 'per-vu-iterations',
            vus: 50,
            iterations: 1,
            maxDuration: '10s',
            exec: 'concurrentVotes',
        },
        // 2. 완전히 동일한 ID로 동시 레이스 컨디션 테스트 (10개 동시 폭격)
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

// 1. 서로 다른 사용자 동시 투표 함수
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
        '정상 투표 성공(201 or 200)': (r) => r.status === 201 || r.status === 200,
    });
}

// 2. 동일한 voterId로 10개 동시 요청 (중복 투표 방어 테스트)
export function duplicateRace() {
    const duplicateId = 'same-user-race-test';

    const payload = JSON.stringify({
        choice: 'jajang',
        voterId: duplicateId,
    });

    const params = { headers: { 'Content-Type': 'application/json' } };
    const res = http.post(`${BASE_URL}/api/vote`, payload, params);

    // 1개만 201/200 성공하고, 나머지는 409 Conflict여야 정상
    check(res, {
        '성공(201) 또는 중복방어(409)': (r) => r.status === 201 || r.status === 409,
        '500 서버 내부 에러 발생 여부 체크': (r) => r.status !== 500,
    });
}