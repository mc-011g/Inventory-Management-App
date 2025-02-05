const toastAction = () => {
    const action = document.body.getAttribute('data-action');
    const toastLiveExample = document.getElementById('liveToast');
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    const toastMessage = document.getElementById("toast-message");

    if (action !== null) {
        if (action === 'saveProfile') {
            toastMessage.innerText = "Saved profile.";
        }
        toastBootstrap.show()
    }
}

const resetProfileChanges = () => {
    document.getElementById('cancelButton').addEventListener('click', function () {
        document.getElementById('profileForm').reset();
        document.getElementById('submitButton').disabled = false;
        document.getElementById('passwordMatchMessage').hidden = true;
    });
}

document.addEventListener('DOMContentLoaded', () => {
    toastAction();
    resetProfileChanges();
});