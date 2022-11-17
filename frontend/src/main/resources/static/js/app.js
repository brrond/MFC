function loadScript(src, callback) {
    let head = document.head;
    let script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = src;

    script.onreadystatechange = callback;
    script.onload = callback;

    head.appendChild(script);
}

function loadScripts(srcs, callback) {
    let loadedScripts = 0;
    let load = () => {
        loadedScripts++;
        if (srcs.length === loadedScripts) callback();
    };

    for (let i = 0; i < srcs.length; i++) {
        loadScript(srcs[i], load);
    }
}

function loadLink(href, rel, callback) {
    let head = document.head;
    let link = document.createElement('link');
    link.rel = rel;
    link.href = href;

    link.onreadystatechange = callback;
    link.onload = callback;

    head.appendChild(link);
}

function loadHead() {
    loadLink("/img/favicon.png", "icon");
    loadLink("https://fonts.gstatic.com", "preconnect");
    loadLink("https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i", "stylesheet");
    loadLink("https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css", "stylesheet");
    loadLink("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css", "stylesheet");
    loadLink("/css/style.css", "stylesheet");
    loadLink("/css/notificationManagerStyle.css", "stylesheet");
}

function loadScriptsOnPage() {
    loadScripts(["https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js",
        "https://cdn.tiny.cloud/1/no-api-key/tinymce/6/tinymce.min.js",
        "https://code.jquery.com/jquery-3.6.1.min.js"], onPageLoad);
}

function loadHeader() {
    let header = document.getElementsByTagName("header")[0];
    header.id = "header";
    header.classList.add("header","fixed-top","d-flex","align-items-center");

    let div = document.createElement("div");
    div.classList.add("d-flex","align-items-center","justify-content-between");
    header.append(div);

    let a = document.createElement("a");
    a.href = "/";
    a.classList.add("logo","d-flex","align-items-center");
    div.append(a);

    let img = document.createElement("img");
    img.src = "/img/logo.png";
    a.append(img);

    let span = document.createElement("span");
    span.classList.add()
    span.innerText = 'MFC';
    span.classList.add("d-none","d-lg-block");
    a.append(span);

    let menu = document.createElement("i");
    menu.classList.add("bi", "bi-list", "toggle-sidebar-btn")
    div.append(menu);
}

function loadFooter() {
    let footer = document.getElementsByTagName("footer")[0];
    footer.id = "footer";
    footer.classList.add("footer");

    let copyright = document.createElement("div");
    copyright.innerHTML = "&copy; Copyright <strong><span>MFC</span></strong>. All Rights Reserved";
    copyright.classList.add("copyright");
    footer.append(copyright);
}

function loadBackToTop() {
    let a = document.createElement("a");
    a.href = "#";
    a.classList.add("back-to-top","d-flex","align-items-center","justify-content-center");

    let i = document.createElement("i");
    i.classList.add("bi", "bi-arrow-up-short");
    a.append(i);

    document.body.append(a);
}

loadScriptsOnPage();
loadHead();

function onPageLoad() {
    loadScript("/js/main.js");
    loadScript("/js/notificationManager.js");
    loadScript("/js/sendRequest.js");
    if (typeof (LOAD_NOTHING) === "undefined") {
        if (typeof (LOAD_NO_HEADER) === "undefined") loadHeader();
        if (typeof (LOAD_NO_FOOTER) === "undefined") loadFooter();
        loadBackToTop();
    }

    if (typeof (LoadData) !== "undefined") loadData();
}
