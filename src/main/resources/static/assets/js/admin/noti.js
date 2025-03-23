setTimeout(() => {
    document.querySelectorAll(".alert-fixed").forEach(el => {
        el.style.transition = "opacity 0.5s";
        el.style.opacity = "0";
        setTimeout(() => el.remove(), 500);
    });
}, 3000);
