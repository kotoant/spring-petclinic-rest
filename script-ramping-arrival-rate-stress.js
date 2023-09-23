import http from 'k6/http';
import {check} from 'k6';
import {scenario} from 'k6/execution';

export const options = {
    scenarios: {
        petclinic: {
            executor: 'ramping-arrival-rate',

            // Our test with at a rate of `startRate` iterations started per `timeUnit` (e.g second).
            startRate: 10,

            // It should start `startRate` iterations per `timeUnit`
            timeUnit: '1s',

            // It should preallocate `preAllocatedVUs` VUs before starting the test.
            preAllocatedVUs: 10,

            // It is allowed to spin up to `maxVUs` maximum VUs in order to sustain the defined
            // constant arrival rate.
            maxVUs: 1000,

            stages: [
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 10, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 350, duration: '3m' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 350, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 360, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 360, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 370, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 370, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 380, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 380, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 390, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 390, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 400, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 400, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 410, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 410, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 420, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 420, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 430, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 430, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 440, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 440, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 450, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 450, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 460, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 460, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 470, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 470, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 480, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 480, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 490, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 490, duration: '20s' },

                // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 500, duration: '20s' },
                // It should stay at `target` iterations per `timeUnit` for the following `duration`.
                { target: 500, duration: '20s' },

                // It should linearly ramp-down to 'target' iterations per `timeUnit` over the following `duration`.
                { target: 0, duration: '20s' },
            ],

            // executor: 'ramping-vus',
            // startVUs: 10,
            // stages: [
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 10, duration: '20s' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 500, duration: '1m' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 500, duration: '1m' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 1000, duration: '1m' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 1000, duration: '1m' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 1500, duration: '1m' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 1500, duration: '1m' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 2000, duration: '1m' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 2000, duration: '1m' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 2500, duration: '1m' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 2500, duration: '1m' },
            //
            //     // It should linearly ramp-up to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 3000, duration: '1m' },
            //
            //     // It should stay at `target` iterations per `timeUnit` for the following `duration`.
            //     { target: 3000, duration: '1m' },
            //
            //     // It should linearly ramp-down to 'target' iterations per `timeUnit` over the following `duration`.
            //     { target: 0, duration: '20s' },
            // ],
            // gracefulRampDown: '0s',
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
        timeout: '2s',
    };
    const lastName = `Last${colName(scenario.iterationInTest)}name`
    const owner = {
        firstName: 'George',
        lastName: lastName,
        address: '110 W. Liberty St.',
        city: 'Madison',
        telephone: '6085551023'
    };
    const postOwnerPayload = JSON.stringify(owner);
    const postOwnerRes = http.post(http.url`${BASE_API_URL}/owners`, postOwnerPayload, params);
    check(postOwnerRes, { 'post owner status was 201': (r) => r.status === 201 });
    const ownerId = postOwnerRes.json().id

    const pet = {
        name: 'Leo',
        birthDate: '2023-03-03',
        type: {
            id: 1,
            name: 'cat'
        }
    }
    const postPetPayload = JSON.stringify(pet);
    const postPetRes = http.post(http.url`${BASE_API_URL}/owners/${ownerId}/pets`, postPetPayload, params);
    check(postPetRes, { 'post pet status was 201': (r) => r.status === 201 });
    const petId = postPetRes.json().id

    const visit = {
        date: '2023-03-03',
        description: 'rabies shot'
    }
    const postVisitPayload = JSON.stringify(visit);
    const postVisitRes = http.post(http.url`${BASE_API_URL}/owners/${ownerId}/pets/${petId}/visits`, postVisitPayload, params);
    check(postVisitRes, { 'post visit status was 201': (r) => r.status === 201 });
    const visitId = postVisitRes.json().id

    const getOwnerRes = http.get(http.url`${BASE_API_URL}/owners/${ownerId}`, params);
    check(getOwnerRes, { 'get owner status was 200': (r) => r.status === 200 });

    const getPetRes = http.get(http.url`${BASE_API_URL}/pets/${petId}`, params);
    check(getPetRes, { 'get pet status was 200': (r) => r.status === 200 });

    const getVisitRes = http.get(http.url`${BASE_API_URL}/visits/${visitId}`, params);
    check(getVisitRes, { 'get visit status was 200': (r) => r.status === 200 });

    const findOwnerRes = http.get(http.url`${BASE_API_URL}/owners?lastName=${lastName}&lastId=${ownerId - 1}&pageSize=1`, params);
    check(findOwnerRes, { 'find owners status was 200': (r) => r.status === 200 });

    pet.name = 'Leopold'
    const putPetPayload = JSON.stringify(pet);
    const putPetRes = http.put(http.url`${BASE_API_URL}/pets/${petId}`, putPetPayload, params);
    check(putPetRes, { 'put pet status was 200': (r) => r.status === 200 });

    visit.date = '2023-03-04'
    const putVisitPayload = JSON.stringify(visit);
    const putVisitRes = http.put(http.url`${BASE_API_URL}/visits/${visitId}`, putVisitPayload, params);
    check(putVisitRes, { 'put visit status was 200': (r) => r.status === 200 });

    owner.telephone = '6085551024'
    const putOwnerPayload = JSON.stringify(owner);
    const putOwnerRes = http.put(http.url`${BASE_API_URL}/owners/${ownerId}`, putOwnerPayload, params);
    check(putOwnerRes, { 'put owner status was 200': (r) => r.status === 200 });
}
