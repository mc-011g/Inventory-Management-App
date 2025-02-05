const toastAction = () => {
    const action = document.body.getAttribute('data-action');
    const toastLiveExample = document.getElementById('liveToast');
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    const toastMessage = document.getElementById("toast-message");

    if (action !== null) {
        switch (action) {
            case 'addProduct':
                toastMessage.innerText = "Added new product.";
                break;
            case 'editProduct':
                toastMessage.innerText = "Saved product changes.";
                break;
            case 'deleteProduct':
                toastMessage.innerText = "Deleted product.";
                break;
            case 'existingProduct':
                toastMessage.innerText = "Product already exists.";
                break;
            default:
                break;
        }
        toastBootstrap.show()
    }
}

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

const getProductId = (id) => {
    document.getElementById("deleteProductId").value = id;
}

const showModals = () => {
    let editProductModal = document.getElementById('editProductModal');
    let addProductModal = document.getElementById('addProductModal');

    if (editProductModal.classList.contains('show')) {
        new bootstrap.Modal(editProductModal).show();
    }

    if (addProductModal.classList.contains('show')) {
        new bootstrap.Modal(addProductModal).show();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    toastAction();
    showModals();
});