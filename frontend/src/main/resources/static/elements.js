const initHeader = () => {
    let menu = document.createElement("div");
    menu.id = "menu";

    let menuElements = [];
    for (let i = 0; i < 3; i++) {
        let menuElement = document.createElement("div");
        menuElement.classList.add("menuElement");

        let a = document.createElement("a");
        menuElement.append(a);

        menu.append(menuElement)
        menuElements.push(menuElement);
    }

    menuElements[0].getElementsByTagName("a")[0].innerText = "Home";
    menuElements[0].getElementsByTagName("a")[0].href="/";

    menuElements[1].getElementsByTagName("a")[0].innerText = "MFC";
    menuElements[1].getElementsByTagName("a")[0].href="/s/";

    menuElements[2].getElementsByTagName("a")[0].innerText = "About";
    menuElements[2].getElementsByTagName("a")[0].href="/about";

    let header = document.getElementById("header");
    header.append(menu);
};

const initFooter = () => {
    let p = document.createElement("p");
    p.innerText = "© All rights reserved";

    let footer = document.getElementById("footer");
    footer.append(p);
}

const initMainStyle = () => {
    document.body.classList.add("background");

    let link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = "main.css";
    document.head.append(link);
}

const initBackground = () => {
    document.body.classList.add("background");
    for (let i = 0; i < 50; i++) document.body.append(document.createElement("span"));

    let link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = "background.css";
    document.head.append(link);
}