function loadScript(url, callback) {
    let head = document.head;
    let script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;

    script.onreadystatechange = callback;
    script.onload = callback;

    head.appendChild(script);
}

loadScript("/elements.js", () => {
    initHeader();
    initFooter();
    initMainStyle();
    initBackground();
});