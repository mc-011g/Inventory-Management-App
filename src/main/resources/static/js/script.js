const getProduct = (selectedProduct, selectedProductId) => {
    document.getElementById("editProductName").value = selectedProduct.name;
    document.getElementById("editProductPrice").value = selectedProduct.price;
    document.getElementById("editProductCategory").value = selectedProduct.category;
    document.getElementById("editProductQuantity").value = selectedProduct.quantity;
    document.getElementById("editProductSKU").value = selectedProduct.sku;
    document.getElementById("editProductId").value = selectedProductId
}

const getProductId = (id) => {
    document.getElementById("deleteProductId").value = id;
}

const getUser = (selectedUser, selectedUserId) => {
    document.getElementById("editUserRole").value = selectedUser.role;
    document.getElementById("editUserEmail").value = selectedUser.email;
    document.getElementById("selectedUserId").value = selectedUserId;
}

const getUserId = (id) => {
    document.getElementById("deleteUserId").value = id;
}

$(document).ready(() => {

    $("#passwordDisplayToggle").click(() => {
        togglePasswordVisiblity();
    });

    $("#newPasswordDisplayToggle").click(() => {
        togglePasswordVisiblity("newPassword");
    });

    $("#editPasswordDisplayToggle").click(() => {
        togglePasswordVisiblity("editPassword");
    });

    $("#passwordDisplayToggleMatch").click(() => {
        togglePasswordVisiblity("match");
    });

    $("#passwordMatch").on("input", () => {
        checkPasswordMatch();
    });

    $("#confirmPasswordMatch").on("input", () => {
        checkPasswordMatch();
    });

    $("#confirmPasswordDisplayToggle").click(() => {
        toggleConfirmPasswordVisibility();
    });

    const togglePasswordVisiblity = (value) => {
        let passwordDisplayToggle = document.getElementById("passwordDisplayToggle");
        let passwordInput = document.getElementById("password");

        if (value === "newPassword") {
            passwordInput = document.getElementById('newUserPassword');
            passwordDisplayToggle = document.getElementById('newPasswordDisplayToggle');
        } else if (value === "editPassword") {
            passwordInput = document.getElementById('editUserPassword');
            passwordDisplayToggle = document.getElementById('editPasswordDisplayToggle');
        } else if (value === "match") {
            passwordInput = document.getElementById('passwordMatch');
            passwordDisplayToggle = document.getElementById('passwordDisplayToggleMatch');
        }

        if (passwordDisplayToggle.classList.contains('bi-eye')) {
            passwordDisplayToggle.classList.replace('bi-eye', 'bi-eye-slash');
            passwordInput.type = 'text';
        } else {
            passwordDisplayToggle.classList.replace('bi-eye-slash', 'bi-eye');
            passwordInput.type = 'password';
        }
    }

    const toggleConfirmPasswordVisibility = () => {
        let confirmNewPasswordToggle = document.getElementById('confirmPasswordDisplayToggle');
        let confirmNewPasswordInput = document.getElementById('confirmPasswordMatch');

        if (confirmNewPasswordToggle.classList.contains('bi-eye')) {
            confirmNewPasswordToggle.classList.replace('bi-eye', 'bi-eye-slash');
            confirmNewPasswordInput.type = 'text';
        } else {
            confirmNewPasswordToggle.classList.replace('bi-eye-slash', 'bi-eye');
            confirmNewPasswordInput.type = 'password';
        }
    }

    const checkPasswordMatch = () => {
        let passwordInput = document.getElementById('passwordMatch');
        let confirmPasswordInput = document.getElementById('confirmPasswordMatch');
        let createAccountButton = document.getElementById('submitButton');
        let passwordMatchMessage = document.getElementById('passwordMatchMessage');

        if ((passwordInput.value != confirmPasswordInput.value) || passwordInput.value == '') {
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

    // Show the modal if there are validation errors
    if ($('#editProductModal').hasClass('show')) {
        $('#editProductModal').modal('show');
    }
    if ($('#addProductModal').hasClass('show')) {
        $('#addProductModal').modal('show');
    }
});



