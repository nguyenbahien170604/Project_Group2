function redirectToPageProduct(button) {
    let page = button.getAttribute("data-page");
    let search = button.getAttribute("data-search") || "";
    let url = "/productList?page=" + page + (search ? "&search=" + encodeURIComponent(search) : "");
    window.location.href = url;
}