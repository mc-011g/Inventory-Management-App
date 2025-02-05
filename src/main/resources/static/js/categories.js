const toastAction = () => {
    const action = document.body.getAttribute('data-action');
    const toastLiveExample = document.getElementById('liveToast');
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    const toastMessage = document.getElementById("toast-message");

    if (action !== null) {
        switch (action) {
            case 'addCategory':
                toastMessage.innerText = "Added new category.";
                break;
            case 'noItemsInCategory':
                toastMessage.innerText = "Could not create category.";
                break;
            case 'editCategory':
                toastMessage.innerText = "Saved category changes.";
                break;
            case 'deleteCategory':
                toastMessage.innerText = "Deleted category.";
                break;
            case 'existingCategory':
                toastMessage.innerText = "Category already exists.";
                break;
            default:
                break;
        }
        toastBootstrap.show()
    }
}

const getCategory = (selectedCategory) => {
    document.getElementById("editCategoryName").value = selectedCategory.name;
    document.getElementById("editCategoryDescription").value = selectedCategory.description;
    document.getElementById("editCategoryId").value = selectedCategory.id;
}

const getCategoryId = (id) => {
    document.getElementById("deleteCategoryId").value = id;
}

const showModals = () => {
    let editCategoryModal = document.getElementById('editCategoryModal');
    let addCategoryModal = document.getElementById('addCategoryModal');

    if (editCategoryModal.classList.contains('show')) {
        new bootstrap.Modal(editCategoryModal).show();
    }

    if (addCategoryModal.classList.contains('show')) {
        new bootstrap.Modal(addCategoryModal).show();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    toastAction();
    showModals();
});

