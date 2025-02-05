const formActions = () => {
    document.getElementById('passwordDisplayToggle').addEventListener('click', () => togglePasswordVisiblity("password"));
    document.getElementById('confirmPasswordDisplayToggle')?.addEventListener('click', () => togglePasswordVisiblity("confirmPassword"));

    let newPassword = document.getElementById('newPassword');
    let newPasswordDisplayToggle = document.getElementById('newPasswordDisplayToggle');
    let confirmPassword = document.getElementById('confirmPassword');

    if (newPassword) {
        newPassword.addEventListener('input', () => checkPasswordMatch("newPassword"));
        confirmPassword.addEventListener('input', () => checkPasswordMatch("newPassword"));
    } else {
        document.getElementById('password').addEventListener('input', () => checkPasswordMatch("password"));
        confirmPassword?.addEventListener('input', () => checkPasswordMatch("password"));
    }

    if (newPasswordDisplayToggle) {
        newPasswordDisplayToggle.addEventListener('click', () => togglePasswordVisiblity("newPassword"));
    }
}

const togglePasswordVisiblity = (value) => {
    let passwordDisplayToggle = '';
    let passwordInput = '';

    if (value === "newPassword") {
        passwordInput = document.getElementById('newPassword');
        passwordDisplayToggle = document.getElementById('newPasswordDisplayToggle');
    } else if (value === "editPassword") {
        passwordInput = document.getElementById('editUserPassword');
        passwordDisplayToggle = document.getElementById('editPasswordDisplayToggle');
    } else if (value === "password") {
        passwordInput = document.getElementById('password');
        passwordDisplayToggle = document.getElementById('passwordDisplayToggle');
    } else if (value === "confirmPassword") {
        passwordInput = document.getElementById('confirmPassword');
        passwordDisplayToggle = document.getElementById('confirmPasswordDisplayToggle');
    }

    if (passwordDisplayToggle.classList.contains('bi-eye')) {
        passwordDisplayToggle.classList.replace('bi-eye', 'bi-eye-slash');
        passwordInput.type = 'text';
    } else {
        passwordDisplayToggle.classList.replace('bi-eye-slash', 'bi-eye');
        passwordInput.type = 'password';
    }
}

const saveProfileChangesButtonState = () => {
    let passwordInput = document.getElementById('newPassword');
    let confirmPasswordInput = document.getElementById('confirmPasswordMatch');
    let saveChangesButton = document.getElementById('submitButton');
    let passwordMatchMessage = document.getElementById('passwordMatchMessage');

    if (passwordInput.value == '' && confirmPasswordInput.value == '') {
        createAccountButton.disabled = false;
    }

    if (passwordInput.value != confirmPasswordInput.value) {
        saveChangesButton.disabled = true;
        passwordMatchMessage.hidden = false;
    } else {
        saveChangesButton.disabled = false;
        passwordMatchMessage.hidden = true;
    }

    if ((passwordInput.value == '') && (confirmPasswordInput.value == '')) {
        passwordMatchMessage.hidden = true;
    }
}

const checkPasswordMatch = (type) => {
    let passwordInput = document.getElementById('password');

    if (type == 'newPassword') {
        passwordInput = document.getElementById('newPassword');
    }

    let confirmPasswordInput = document.getElementById('confirmPassword');
    let createAccountButton = document.getElementById('submitButton');
    let passwordMatchMessage = document.getElementById('passwordMatchMessage');

    if ((passwordInput.value != confirmPasswordInput?.value) || passwordInput.value == '') {
        createAccountButton.disabled = true;
        passwordMatchMessage.hidden = false;
    } else {
        createAccountButton.disabled = false;
        passwordMatchMessage.hidden = true;
    }

    if ((passwordInput.value == '') && (confirmPasswordInput.value == '')) {
        passwordMatchMessage.hidden = true;
    }
}


document.addEventListener('DOMContentLoaded', formActions);
