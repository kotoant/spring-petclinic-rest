import http from 'k6/http';
import {check} from 'k6';
import {scenario} from 'k6/execution';

export const options = {
    scenarios: {
        petclinic: {
            // executor: 'ramping-arrival-rate',
            //
            // // Our test with at a rate of `startRate` iterations started per `timeUnit` (e.g second).
            // startRate: 10,
            //
            // // It should start `startRate` iterations per `timeUnit`
            // timeUnit: '1s',
            //
            // // It should preallocate `preAllocatedVUs` VUs before starting the test.
            // preAllocatedVUs: 10,
            //
            // // It is allowed to spin up to `maxVUs` maximum VUs in order to sustain the defined
            // // constant arrival rate.
            // maxVUs: 1000,
            //
            // stages: [
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 10, duration: '20s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 100, duration: '30s' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 100, duration: '30s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 140, duration: '30s' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 140, duration: '30s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 180, duration: '30s' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 180, duration: '30s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 220, duration: '30s' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 220, duration: '30s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 260, duration: '30s' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 260, duration: '30s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 300, duration: '30s' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 300, duration: '30s' },
            //
            // //     // It should linearly ramp-down to 'target' iterations per `timeUnit` over the following `duration`.
            // //     { target: 0, duration: '20s' },
            // ],

            executor: 'ramping-vus',
            startVUs: 10,
            stages: [
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 10, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 1000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 1000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 1500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 1500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 2000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 2000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 2500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 2500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 3000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 3000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 3500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 3500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 4000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 4000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 4500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 4500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 5000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 5000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 5500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 5500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 6000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 6000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 6500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 6500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 7000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 7000, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 7500, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 7500, duration: '30s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 8000, duration: '30s' },

                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 8000, duration: '30s' },
                
                // It should linearly ramp-down to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 0, duration: '20s' },
            ],
            gracefulRampDown: '0s',
        },
    },
};

function colName(n) {
    var ordA = 'a'.charCodeAt(0);
    var ordZ = 'z'.charCodeAt(0);
    var len = ordZ - ordA + 1;

    var s = "";
    while(n >= 0) {
        s = String.fromCharCode(n % len + ordA) + s;
        n = Math.floor(n / len) - 1;
    }
    return s;
}

export default function () {
    const BASE_API_URL = 'https://springpetclinicrest.ru/api'
    // const BASE_API_URL = 'http://localhost:9966/api'
    const params = {
        headers: { 'Content-Type': 'application/json' },
        timeout: '30s',
    };

    const getSleepRes = http.get(http.url`${BASE_API_URL}/sleep?times=10&millis=2`, params);
    check(getSleepRes, { 'get sleep status was 200': (r) => r.status === 200 });

}
