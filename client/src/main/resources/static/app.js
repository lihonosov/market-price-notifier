function connect() {
    var stompClient = Stomp.over(new SockJS('http://localhost:8888/alerts'));
    stompClient.connect({}, function () {
        $("#connection-status").html('Connected').addClass('text-success').removeClass('text-danger');
        stompClient.subscribe('/alerts', function (event) {
            showTickers(JSON.parse(event.body));
        });
    }, function (message) {
        $("#connection-status").html(message + ". Reconnecting in 10 seconds...").removeClass('text-success').addClass('text-danger');
        setTimeout(connect, 10000);
    });
}

function sendLimit(put) {
    $.ajax({
        url: 'http://localhost:8888/alert?pair=' + $("#pair").val() + "&limit=" + $("#limit").val(),
        method: put ? 'PUT' : 'DELETE'
    }).done(function () {
        $("#errors").hide();
        clearForm();
    }).fail(function (error) {
        console.log(error);
        if (error && error.responseJSON) {
            var message = "";
            if (error.responseJSON.errors) {
                for (var i in error.responseJSON.errors) {
                    message += error.responseJSON.errors[i].defaultMessage + '<br>';
                }
            } else {
                message = error.responseJSON.message;
            }
            showError(message);
        } else {
            showError("Unknown error");
        }
    });
}

function showTickers(event) {
    $("#alerts").append("<tr><td>" + event.pair + "</td><td>" + event.limit + "</td><td>" + event.lastValue + "</td><td>" + event.timestamp + "</td></tr>");
}

function showError(message) {
    $("#errors").html(message);
    $("#errors").show();
}

function clearForm() {
    $("#limit").val('');
    $("#pair").val('').focus();
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#put").click(function () {
        sendLimit(true);
    });
    $("#delete").click(function () {
        sendLimit(false);
    });
    $("#clear").click(function () {
        $("#tickers td").remove();
    });
});
