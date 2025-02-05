const toastAction = () => {
    const toastLiveExample = document.getElementById('liveToast');
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    const toastMessage = document.getElementById("toast-message");

    let action = document.body.getAttribute('data-action');

    if (action !== null) {
        switch (action) {
            case 'addOrder':
                toastMessage.innerText = "Added new order.";
                break;
            case 'cancelOrder':
                toastMessage.innerText = "Cancelled order.";
                break;
            case 'cancelOrderFailed':
                toastMessage.innerText = "Failed to cancel order.";
                break;
            case 'noItemsInOrder':
                toastMessage.innerText = "Order failed, no items selected.";
                break;
            case 'editOrder':
                toastMessage.innerText = "Saved order changes.";
                break;
            case 'deleteOrder':
                toastMessage.innerText = "Deleted order.";
                break;
            case 'existingOrder':
                toastMessage.innerText = "Order already exists.";
                break;
            default:
                break;
        }
        toastBootstrap.show()
    }
}

const getOrderId = (id) => {
    document.getElementById("deleteOrderId").value = id;
}

const getCancelOrderId = (id) => {
    document.getElementById("cancelOrderId").value = id;
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


const showModals = () => {
    let editOrderModal = document.getElementById("editOrderModal");
    let addOrderModal = document.getElementById("addOrderModal");

    if (editOrderModal.classList.contains('show')) {
        new bootstrap.Modal(editOrderModal).show();
    }

    if (addOrderModal.classList.contains('show')) {
        new bootstrap.Modal(addOrderModal).show();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    toastAction();
    showModals();
});