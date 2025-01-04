const getProduct = (selectedProduct, selectedProductId) => {
    document.getElementById("editProductName").value = selectedProduct.name;
    document.getElementById("editProductPrice").value = selectedProduct.price;
    document.getElementById("editProductCategory").value = selectedProduct.category;
    document.getElementById("editProductQuantity").value = selectedProduct.quantity;
    document.getElementById("editProductSKU").value = selectedProduct.sku;
    document.getElementById("editProductId").value = selectedProductId;
    document.getElementById("editProductUserId").value = selectedProduct.userId;
    document.getElementById("editProductImages").value = selectedProduct.images;
    document.getElementById("editProductCreatedAt").value = selectedProduct.createdAt;
}

const getOrder = (selectedOrder) => {
    document.getElementById("editOrderCustomerName").value = selectedOrder.customerName;
    document.getElementById("editOrderCustomerEmail").value = selectedOrder.customerEmail;
    document.getElementById("editOrderCustomerPhone").value = selectedOrder.customerPhone;
    document.getElementById("editOrderCustomerAddress").value = selectedOrder.customerAddress;
    document.getElementById("editOrderNotes").value = selectedOrder.notes;
    document.getElementById("editOrderId").value = selectedOrder.id;

    //Populate order items
    const orderItemsContainer = document.getElementById("editOrderProducts");
    orderItemsContainer.innerHTML = "";

    const hiddenStatusDiv = document.createElement("div");

    hiddenStatusDiv.innerHTML = `
        <input type="hidden" name="status" value="${selectedOrder.status}">
        <input type="hidden" name="createdAt" value="${selectedOrder.createdAt}">
        <input type="hidden" name="userId" value="${selectedOrder.userId}">
            `;
    orderItemsContainer.appendChild(hiddenStatusDiv);

    selectedOrder.orderItems.forEach((orderItem, index) => {
        const itemDiv = document.createElement("div");
        itemDiv.classList.add('form-group', 'd-flex', 'justify-content-between', 'mb-1');
        itemDiv.innerHTML = `

        <div class="form-check flex-grow-1">
            <input class="form-check-input" type="checkbox" name="orderItems[${index}].selected" value="${orderItem.selected}" id="productCheckboxEditOrder${orderItem.product.id}" ${orderItem.selected ? 'checked' : ''} onchange="updateTotalPrice('editOrder')">
            <div class="d-flex flex-row justify-content-between">                
                <div class="d-flex">
                    <div class="orderProductImage">
                        <i class="bi bi-image d-flex orderProductIcon"></i>
                    </div>
                    <div class="ms-2 me-5">
                        <div>${orderItem.product.name}</div>                                              
                        <div class="form-group d-flex">
                            <label for="editQuantity${index}">Qty:</label>
                            <input type="number" class="ms-1 form-control orderProductQuantity" name="orderItems[${index}].quantity" id="editQuantity${orderItem.product.id}" value="${orderItem.quantity}" required step="1" min="0" onchange="updateTotalPrice('editOrder')">
                        </div>
                    </div>
                </div>
                    <span>$${orderItem.product.price}</span>                              
            </div>

            <input type="hidden" name="orderItems[${index}].product.id" value="${orderItem.product.id}">
            <input type="hidden" name="orderItems[${index}].selected" value="${orderItem.product.id}" id="hiddenSelectedEditOrder${index}">
            <input type="hidden" name="orderItems[${index}].price" id="hiddenPriceEditOrder${orderItem.product.id}" value="${orderItem.product.price}">
          
        </div>
        `;
        orderItemsContainer.appendChild(itemDiv);
    });

    // The inital value of the total price
    updateTotalPrice('editOrder');
}

// Event listener for the form submission
const editOrderForm = document.getElementById("editOrderForm");
if (editOrderForm) {
    editOrderForm.addEventListener("submit", function (event) {
        const checkboxes = document.querySelectorAll(".form-check-input");
        checkboxes.forEach((checkbox, index) => {
            const hiddenSelectedEditOrder = document.getElementById(`hiddenSelectedEditOrder${index}`);
            if (hiddenSelectedEditOrder) {
                hiddenSelectedEditOrder.value = checkbox.checked ? "true" : "false";
            }
        });
    });
}

const updateTotalPrice = (formType) => {
    let quantities;

    if (formType === 'editOrder') {
        quantities = document.querySelectorAll(`#editOrderModal input[name^='orderItems'][name$='quantity']`);
    } else if (formType === 'createOrder') {
        quantities = document.querySelectorAll(`#addOrderModal input[name^='orderItems'][name$='quantity']`);
    }

    let totalPrice = 0;

    quantities.forEach((quantityInput) => {
        let productId;
        let priceElement;
        let checkbox;

        if (formType === 'editOrder') {
            productId = quantityInput.id.split('editQuantity')[1];
            if (productId) {
                priceElement = document.getElementById(`hiddenPriceEditOrder${productId}`);
                checkbox = document.getElementById(`productCheckboxEditOrder${productId}`);
            }
        } else if (formType === 'createOrder') {
            productId = quantityInput.id.split('addQuantity')[1];
            if (productId) {
                priceElement = document.getElementById(`hiddenPriceAddOrder${productId}`);
                checkbox = document.getElementById(`productCheckboxAddOrder${productId}`);
            }
        }

        if (productId && priceElement && checkbox) {
            const quantity = parseInt(quantityInput.value);
            if (checkbox.checked) {
                const price = parseFloat(priceElement.value);
                totalPrice += quantity * price;
            }
        }
    });

    if (formType === 'editOrder') {
        document.getElementById("totalPriceSpanEditOrder").innerHTML = `$${totalPrice.toFixed(2)}`;
    } else if (formType === 'createOrder') {
        document.getElementById("totalPriceSpanCreateOrder").innerHTML = `$${totalPrice.toFixed(2)}`;
    }
}

// Event listeners for quantity and checkbox changes in the Create Order modal
document.querySelectorAll("#addOrderModal input[name^='orderItems'][name$='quantity']").forEach(input => {
    input.addEventListener('change', () => updateTotalPrice('createOrder'));
});

document.querySelectorAll("#addOrderModal input[type='checkbox']").forEach(checkbox => {
    checkbox.addEventListener('change', () => updateTotalPrice('createOrder'));
});

const getProductId = (id) => {
    document.getElementById("deleteProductId").value = id;
}

const getOrderId = (id) => {
    document.getElementById("deleteOrderId").value = id;
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
    if ($('#editOrderModal').hasClass('show')) {
        $('#editOrderModal').modal('show');
    }
    if ($('#addOrderModal').hasClass('show')) {
        $('#addOrderModal').modal('show');
    }
});