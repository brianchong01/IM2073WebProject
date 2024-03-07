$(document).ready(function () {
    $('.carousel').carousel({
        interval: 2000
    });

    $('#usernameInp').keypress(function (e){
        validate(e);
    });

    $('#passwordInp').keypress(function (e){
        validate(e);
    });

    function validate(e) {
        var regex = new RegExp("^[a-zA-Z0-9_]+$");
        var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
        if (regex.test(str)) {
            return true;
        }
        e.preventDefault();
        return false;
    }
});
