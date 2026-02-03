import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
	scenarios: {
		// OpenSearch
		os: {
			executor: 'constant-vus',
			vus: 1000,
			duration: '20s',
			exec: 'os',
		},
	},
};

export function os() {
	const res = http.get('http://localhost:8080/api/search/job-postings?q=Java&status=OPEN&page=0&size=20', {
		headers: { Connection: 'Keep-alive' },
	});
	check(res, { 'os status 200': (r) => r.status === 200 });
	sleep(0.2);
}