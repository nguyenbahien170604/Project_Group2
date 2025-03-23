document.getElementById('usernameManager').addEventListener('input', function () {
    sessionStorage.setItem('usernameManager', this.value);
});

document.getElementById('passwordManager').addEventListener('input', function () {
    sessionStorage.setItem('passwordManager', this.value);
});

document.getElementById('phoneManager').addEventListener('input', function () {
    sessionStorage.setItem('phoneManager', this.value);
});

document.getElementById('emailManager').addEventListener('input', function () {
    sessionStorage.setItem('emailManager', this.value);
});

window.onload = function () {
    if (sessionStorage.getItem('usernameManager')) {
        document.getElementById('usernameManager').value = sessionStorage.getItem('usernameManager');
    }
    if (sessionStorage.getItem('passwordManager')) {
        document.getElementById('passwordManager').value = sessionStorage.getItem('passwordManager');
    }
    if (sessionStorage.getItem('phoneManager')) {
        document.getElementById('phoneManager').value = sessionStorage.getItem('phoneManager');
    }
    if (sessionStorage.getItem('emailManager')) {
        document.getElementById('emailManager').value = sessionStorage.getItem('emailManager');
    }
};
