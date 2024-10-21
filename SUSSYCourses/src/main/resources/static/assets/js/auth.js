document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('passwordcon').value;
    const role = 'ADMIN'
    const errorMessage = document.getElementById('errorMessage');

    console.log('Email:', email);
    console.log('Password:', password);
    console.log('Confirm Password:', confirmPassword);
    console.log('Role:', role);

    errorMessage.textContent = '';

    if (password !== confirmPassword) {
        errorMessage.textContent = 'Passwords do not match';
        return;
    }

    const data = {
        email,
        password,
        role
    };

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