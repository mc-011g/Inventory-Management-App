const getProduct = (selectedProduct, selectedProductId) => {

    document.getElementById("editProductName").value = selectedProduct.name;
    document.getElementById("editProductPrice").value = selectedProduct.price;
    document.getElementById("editProductQuantity").value = selectedProduct.quantity;
    document.getElementById("editProductSKU").value = selectedProduct.sku;
    document.getElementById("editProductId").value = selectedProductId;
    document.getElementById("editProductUserId").value = selectedProduct.userId;
    document.getElementById("editProductImages").value = selectedProduct.images;
    document.getElementById("editProductCreatedAt").value = selectedProduct.createdAt;

    //Select the product's correct category
    const editProductCategorySelect = document.getElementById("editProductCategory");
    const options = Array.from(editProductCategorySelect.options);

    options.forEach((option) => {
        if (option.value == selectedProduct.categoryId) {
            option.selected = true;
        }
    });
}

const getCategory = (selectedCategory) => {
    document.getElementById("editCategoryName").value = selectedCategory.name;
    document.getElementById("editCategoryDescription").value = selectedCategory.description;
    document.getElementById("editCategoryId").value = selectedCategory.id;
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
            <input class="form-check-input" type="checkbox" name="orderItems[${index}].selected" value="${orderItem.selected}" id="productCheckboxEditOrder${orderItem.product.id}" ${orderItem.selected ? 'checked' : ''} ${selectedOrder.status === 'Pending' ? '' : 'disabled'} onchange="updateTotalPrice('editOrder')">
            <div class="d-flex flex-row justify-content-between">                
                <div class="d-flex">
                    <div class="orderProductImage">
                        <i class="bi bi-image d-flex orderProductIcon"></i>
                    </div>
                    <div class="ms-2 me-5">
                        <div>${orderItem.product.name}</div>                                              
                        <div class="form-group d-flex">
                            <label for="editQuantity${index}">Qty:</label>
                            <input type="number" class="ms-1 form-control orderProductQuantity" name="orderItems[${index}].quantity" id="editQuantity${orderItem.product.id}" value="${orderItem.quantity}" required step="1" min="0" ${selectedOrder.status === 'Pending' ? '' : 'disabled'} onchange="updateTotalPrice('editOrder')">
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

const getCategoryId = (id) => {
    document.getElementById("deleteCategoryId").value = id;
}

const getCancelOrderId = (id) => {
    document.getElementById("cancelOrderId").value = id;
}


const getUser = (selectedUser, selectedUserId) => {
    document.getElementById("editUserRole").value = selectedUser.role;
    document.getElementById("editUserEmail").value = selectedUser.email;
    document.getElementById("selectedUserId").value = selectedUserId;
}

const getUserId = (id) => {
    document.getElementById("deleteUserId").value = id;
}

document.addEventListener('DOMContentLoaded', (event) => {
    const resetProfileChanges = () => {
        document.getElementById('cancelButton').addEventListener('click', function () {
            document.getElementById('profileForm').reset();
            document.getElementById('submitButton').disabled = false;
            document.getElementById('passwordMatchMessage').hidden = true;
        });
    }
    resetProfileChanges();
});

const getGraphMetricMaxSalesValue = (metricBars) => {
    let maxSalesValue = 0;

    metricBars.forEach(metricBar => {
        const salesValue = metricBar.getAttribute('data-value');

        if (salesValue >= 0 && salesValue <= 5000) {
            maxSalesValue = 5000;
        } else if (salesValue > 5000 && salesValue <= 10000) {
            maxSalesValue = 10000;
        } else if (salesValue > 10000 && salesValue <= 25000) {
            maxSalesValue = 25000;
        } else if (salesValue > 25000 && salesValue <= 50000) {
            maxSalesValue = 50000;
        } else if (salesValue > 50000 && salesValue <= 100000) {
            maxSalesValue = 100000;
        } else if (salesValue > 100000 && salesValue <= 250000) {
            maxSalesValue = 250000;
        } else if (salesValue > 250000 && salesValue <= 500000) {
            maxSalesValue = 500000;
        } else if (salesValue > 500000 && salesValue <= 1000000) {
            maxSalesValue = 1000000;
        } else if (salesValue > 1000000 && salesValue <= 5000000) {
            maxSalesValue = 5000000;
        } else if (salesValue > 5000000 && salesValue <= 10000000) {
            maxSalesValue = 10000000;
        } else if (salesValue > 10000000 && salesValue <= 50000000) {
            maxSalesValue = 50000000;
        } else if (salesValue > 50000000 && salesValue <= 100000000) {
            maxSalesValue = 50000000;
        } else {
        }

        let newHeight = 0 + '%';

        if (salesValue > 1000000000) {
            newHeight = 100 + '%';
        } else {
            newHeight = ((salesValue / maxSalesValue) * 100) + '%';
        }

        metricBar.style.height = newHeight;
    });

    return maxSalesValue;
}

const setMetricBarValues = () => {
    const metricBars = document.querySelectorAll('.metric-bar');
    const maxSalesValue = getGraphMetricMaxSalesValue(metricBars);

    //Set new height of bars according to max sales value
    metricBars.forEach(metricBar => {
        const salesValue = metricBar.getAttribute('data-value');
        let newHeight;

        if (salesValue > 1000000000) {
            newHeight = 100 + '%';
        } else {
            newHeight = ((salesValue / maxSalesValue) * 100) + '%';
        }

        metricBar.style.height = newHeight;
    });

    updateMetricYAxis(maxSalesValue);
}

const updateMetricYAxis = (maxSalesValue) => {
    const yAxisItems = document.querySelectorAll('.metric-bar-y-axis-item');
    let incrementValue = 1;

    yAxisItems.forEach(item => {
        let newSalesValue = Math.floor(maxSalesValue * incrementValue);
        incrementValue -= 0.2;

        item.innerHTML = newSalesValue.toLocaleString('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 0 });
    });
}

document.addEventListener('DOMContentLoaded', setMetricBarValues);


$(document).ready(() => {
    $("#passwordDisplayToggle").click(() => {
        togglePasswordVisiblity("password");
    });

    $("#newPasswordDisplayToggle").click(() => {
        togglePasswordVisiblity("newPassword");
    });

    $("#editPasswordDisplayToggle").click(() => {
        togglePasswordVisiblity("editPassword");
    });

    $("#passwordMatchProfile").on("input", () => {
        saveProfileChangesButtonState();
    });

    $("#confirmPasswordMatchProfile").on("input", () => {
        saveProfileChangesButtonState();
    });

    $("#newPassword").on("input", () => {
        checkPasswordMatch();
    });

    $("#confirmPassword").on("input", () => {
        checkPasswordMatch();
    });

    $("#confirmPasswordDisplayToggle").click(() => {
        togglePasswordVisiblity("confirmPassword");
    });

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

    const checkPasswordMatch = () => {
        let passwordInput = document.getElementById('newPassword');
        let confirmPasswordInput = document.getElementById('confirmPassword');
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
    if ($('#editCategoryModal').hasClass('show')) {
        $('#editCategoryModal').modal('show');
    }
    if ($('#addCategoryModal').hasClass('show')) {
        $('#addCategoryModal').modal('show');
    }
});