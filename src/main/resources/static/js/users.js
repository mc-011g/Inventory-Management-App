const toastAction = () => {
    const showEditModal = document.body.getAttribute('showEditModal');
    const showAddModal = document.body.getAttribute('showAddModal');
    const selectedUserId = document.body.getAttribute('selectedUserId');
    const action = document.body.getAttribute('data-action');

    const toastLiveExample = document.getElementById('liveToast');
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    const toastMessage = document.getElementById("toast-message");

    if (selectedUserId !== null) {
        document.getElementById("selectedUserId").value = selectedUserId;
    }

    if (showEditModal) {
        new bootstrap.Modal(editProductModal).show();
    }

    if (showAddModal) {
        new bootstrap.Modal(editProductModal).show();
    }

    if (action !== null) {
        switch (action) {
            case 'addUser':
                toastMessage.innerText = "Added new user.";
                break;
            case 'editUser':
                toastMessage.innerText = "Saved user changes.";
                break;
            case 'deleteUser':
                toastMessage.innerText = "Deleted user.";
                break;
            case 'existingUser':
                toastMessage.innerText = "User already exists.";
                break;
            default:
                break;
        }
        toastBootstrap.show()
    }
}

const getUser = (selectedUser, selectedUserId) => {
    document.getElementById("editUserRole").value = selectedUser.role;
    document.getElementById("editUserEmail").value = selectedUser.email;
    document.getElementById("selectedUserId").value = selectedUserId;
}

const getUserId = (id) => {
    document.getElementById("deleteUserId").value = id;
}

document.addEventListener('DOMContentLoaded', () => {
    toastAction();
    resetProfileChanges();
});