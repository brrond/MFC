let buttons = document.getElementsByClassName("btn-send-request");
for (let i = 0; i < buttons.length; i++) {
    buttons[i].addEventListener("click", (e) => {
        let btn = e.target;
        let form = undefined;
        let prev = btn.parentElement;
        while (prev !== document.body) {
            if (prev.tagName === "FORM") {
                form = prev;
                break;
            }
            prev = prev.parentElement;
        }

        if (form === undefined) return;

        if (!form.checkValidity()) {
            form.classList.add('was-validated')
            return;
        }

        $.ajax({
            type: form.method,
            url: form.action,
            data: $(form).serialize(),
            success: function(data) {
                let parser = new DOMParser();
                let response = parser.parseFromString(data, "text/html");
                let title = response.getElementById("title");
                let msg = response.getElementById("msg");
                notificationManager.notificationQueue.push(title.innerText + "." + msg.innerText);
                notificationManager.reloadAfterLast = true;
            }
        });

    });
}