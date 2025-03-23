function redirectToPageManager(button) {
    let page = button.getAttribute("data-page");
    let search = button.getAttribute("data-search") || "";
    let url = "/managerList?page=" + page + (search ? "&search=" + encodeURIComponent(search) : "");
    window.location.href = url;
}