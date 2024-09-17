$(document).ready(function () {
    $("#btnLogin").on("click", login);
    $('.form-control').keypress(function(e){
        if(e.keyCode == 13)
        $('#btnLogin').click();
    });
    $("#inputUsername").focus();
});

function login() {
    event.preventDefault();
    $("#loginAlert").addClass("d-none");
    var dataSend = {
        username: $("#inputUsername").val().trim(),
        password: $("#inputPassword").val().trim()
    };
    if(dataSend.username == "" && dataSend.password == ""){
        $("#loginAlert").removeClass("d-none");
        $("#txtLoginAlert").text("You need to fill in the fields.");
    }else if(dataSend.username == ""){
        $("#loginAlert").removeClass("d-none");
        $("#txtLoginAlert").text("Username empty.");
    }else if(dataSend.password == ""){
        $("#loginAlert").removeClass("d-none");
        $("#txtLoginAlert").html("Password empty.");
    }else{
        $.ajax({
            url: "/dologin",
            method: "POST",
            data: JSON.stringify(dataSend),
            contentType: "application/json",
            dataType: "json"
        }).done(function (response) {
            if(response && response.result == "OK"){
                sessionStorage.SessionName = "SessionData"
                window.location.href = '/home';
            }
            else if(response && response.message != ""){
                $("#loginAlert").removeClass("d-none");
                $("#txtLoginAlert").text(response.message + ".");
            }else{
                $("#loginAlert").removeClass("d-none");
                $("#txtLoginAlert").text("There was an error.");
            }
        }).fail(function (error) {
            $("#loginAlert").removeClass("d-none");
            $("#txtLoginAlert").text(error + ".");
        })
    }
}