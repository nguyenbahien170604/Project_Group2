document.getElementById('nameProduct').addEventListener('input', function () {
    sessionStorage.setItem('nameProduct', this.value);
});

document.getElementById('descriptionProduct').addEventListener('input', function () {
    sessionStorage.setItem('descriptionProduct', this.value);
});

document.getElementById('shortDescriptionProduct').addEventListener('input', function () {
    sessionStorage.setItem('shortDescriptionProduct', this.value);
});

document.getElementById('discountProduct').addEventListener('input', function () {
    sessionStorage.setItem('discountProduct', this.value);
});
document.getElementById('priceProduct').addEventListener('input', function () {
    sessionStorage.setItem('priceProduct', this.value);
});

document.getElementById('brandIdProduct').addEventListener('input', function () {
    sessionStorage.setItem('brandIdProduct', this.value);
});

document.getElementById('categoryIdProduct').addEventListener('input', function () {
    sessionStorage.setItem('categoryIdProduct', this.value);
});

document.getElementById('discountProduct').addEventListener('input', function () {
    sessionStorage.setItem('discountProduct', this.value);
});

window.onload = function () {
    if (sessionStorage.getItem('nameProduct')) {
        document.getElementById('nameProduct').value = sessionStorage.getItem('nameProduct');
    }
    if (sessionStorage.getItem('descriptionProduct')) {
        document.getElementById('descriptionProduct').value = sessionStorage.getItem('descriptionProduct');
    }
    if (sessionStorage.getItem('shortDescriptionProduct')) {
        document.getElementById('shortDescriptionProduct').value = sessionStorage.getItem('shortDescriptionProduct');
    }
    if (sessionStorage.getItem('discountProduct')) {
        document.getElementById('discountProduct').value = sessionStorage.getItem('discountProduct');
    }
    if (sessionStorage.getItem('priceProduct')) {
        document.getElementById('priceProduct').value = sessionStorage.getItem('priceProduct');
    }
    if (sessionStorage.getItem('brandIdProduct')) {
        document.getElementById('brandIdProduct').value = sessionStorage.getItem('brandIdProduct');
    }
    if (sessionStorage.getItem('categoryIdProduct')) {
        document.getElementById('categoryIdProduct').value = sessionStorage.getItem('categoryIdProduct');
    }
    if (sessionStorage.getItem('discountProduct')) {
        document.getElementById('discountProduct').value = sessionStorage.getItem('discountProduct');
    }
};
