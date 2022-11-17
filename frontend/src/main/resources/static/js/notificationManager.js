var notificationManager = {
    div: null,
    notificationQueue: [],
    reloadAfterLast: false,

    next: function() {
        if (this.notificationQueue.length !== 0) {
            let str = this.notificationQueue.shift();
            if (str === undefined) {
                setTimeout(function() {this.next();}.bind(this), 1000);
                return;
            }
            this.div.innerText = str;
            this.div.style.display = "block";
            setTimeout(function() {this.hide();}.bind(this), 3 * 1000);
            return;
        } else if (this.reloadAfterLast) {
            location.reload();
        }
        setTimeout(function() {this.next();}.bind(this), 1000);
    },

    hide: function() {
        console.log(this);
        this.div.style.display = "none";
        setTimeout(function() {notificationManager.next();}.bind(this), 1000);
    },

    start: function() {
        this.div = document.createElement("div");
        this.div.id = "notificationManager";
        document.getElementById("sidebar").append(this.div);
        this.next();
    }

};
notificationManager.start();