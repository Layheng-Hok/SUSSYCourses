document.getElementById("signupForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('passwordcon').value;
    const errorMessage = document.getElementById('errorMessage');

    errorMessage.textContent = '';

    if (password !== confirmPassword) {
        errorMessage.textContent = 'Passwords do not match';
        return;
    }

    const data = {
        username,
        password
    };

    fetch('/stay-with-me/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.status === 409) {
                errorMessage.textContent = 'User already exists';
            } else if (response.status === 200) {
                window.location.href = '/stay-with-me/login';
            } else {
                errorMessage.textContent = 'An error occurred. Please try again.';
            }
        })
        .catch(error => {
            errorMessage.textContent = 'An error occurred: ' + error.message;
        });
});