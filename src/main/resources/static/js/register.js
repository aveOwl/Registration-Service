$(function () {

    var home = $('#home');

    if (localStorage.getItem('isClicked')) {
        $(home).addClass("hidden");
        $('#register').removeClass("hidden");
    }

    $(home).click(function (e) {
        e.preventDefault();
        localStorage.setItem('isClicked', true);
        $('#home').addClass("hidden");
        $('#register').removeClass("hidden");
    });

    // LocalStorage auto clean
    var timeout = 2;
    var now = new Date().getTime();

    var setupTime = localStorage.getItem('setupTime');
    if (setupTime === null) {
        localStorage.setItem('setupTime', now);
    } else {
        if (now - setupTime > timeout*60*1000) {
            localStorage.clear();
            localStorage.setItem('setupTime', now);
        }
    }
});