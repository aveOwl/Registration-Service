$(function () {

    // Switch between Home and Form sections
    $("#homeButton").click(function (e) {
        e.preventDefault();

        $("#home").addClass("hidden");
        $("#register").removeClass("hidden");
    });

    var form = $("#registerForm");

    form.submit(function (event) {
        event.preventDefault();

        $.ajax({
            url: form.attr("action"),
            type: "POST",
            data: form.serialize(),
            dataType: "json",
            timeout: 10000,
            success: function (response) {
                if (response.success) {
                    // Switch between Form and Success sections
                    $("#register").addClass("hidden");
                    $("#confirm").removeClass("hidden");
                } else {
                    // Email Violation
                    if (response.invalidEmail) {
                        $("#email").addClass("field-error");
                        $("span#emailError").text(response.emailViolationMessage);
                    } else {
                        $("#email").removeClass("field-error");
                        $("#email").addClass("field-valid");
                        $("span#emailError").empty();
                    }

                    // Password Violation
                    if (response.invalidPassword) {
                        $("#password").addClass("field-error");
                        $("span#passwordError").text(response.passwordViolationMessage);
                    } else {
                        $("#password").removeClass("field-error");
                        $("#password").addClass("field-valid");
                        $("span#passwordError").empty();
                    }
                }
            },
            error: function (e) {
                $("#serverError").text("Server not responding! Please, try again later." + e)
            }
        })
    });
});