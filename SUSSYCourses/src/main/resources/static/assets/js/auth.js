document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const fullName = document.getElementById("fullName").value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const passwordCon = document.getElementById('passwordCon').value;
    const role = 'ADMIN'
    const errorMessage = document.getElementById('errorMessage');

    errorMessage.textContent = '';

    if (password !== passwordCon) {
        errorMessage.textContent = 'Passwords do not match';
        return;
    }

    const data = {
        fullName,
        email,
        password,
        role
    };

    console.log(data);

    fetch('/register', {
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
                window.location.href = '/login';
            } else {
                errorMessage.textContent = 'An error occurred. Please try again.';
            }
        })
        .catch(error => {
            errorMessage.textContent = 'An error occurred: ' + error.message;
        });
});