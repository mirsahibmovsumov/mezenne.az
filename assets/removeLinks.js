window.onload = function () {
    for (var i = 0; i < document.querySelectorAll("a[target='_blank']").length; i++) {
        document.querySelectorAll("a[target='_blank']")[i].removeAttribute("href");
    }
};