import {ref} from 'vue';

export const usn = ref('');
export const pwd = ref('');

export function setCredentials(username, password) {
    usn.value = username;
    pwd.value = password;
}

console.log(usn.value)
console.log(pwd.value);