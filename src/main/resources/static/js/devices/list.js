var table;
var wolModal;

$(document).ready(function () {
    getDevices();
    const isMobile = 'ontouchstart' in window || navigator.maxTouchPoints > 0;
    if(isMobile){
        $(".sidebar-mini").addClass("sidebar-collapse");
    }else{
        $(".sidebar-mini").removeClass("sidebar-collapse");
    }
    threadGetDevices();
});

function getDevices() {
    table = $('#devices').DataTable({
        scrollY: '550px',
        dom: 'lrtip',
        order: [[1, 'asc']],
        language: {
          url: "/json/English.json"
        },
        initComplete: onInitComplete,
        paging: false,
        searching: false,
        ajax: {
              url: '/getDevices',
              type: 'GET',
              dataSrc: function (response) {
                var d = {'data':response};
                return d.data;
              }
        },
        columns: [
            {
               data: 'id',
               visible: false
            },
            {
                data: 'name',
                searchable: false
            },
            {
                data: 'description',
                searchable: false,
                orderable: false
            },
            {
                data: 'ip',
                visible: false
            },
            {
                data: 'mac',
                visible: false
            },
            {
                data: 'null',
                render: function(data, type, data) {
                    return data.status ? '<img id="img' + data.id + '" src="/img/online.png" class="img-connection">' : '<img id="img' + data.id + '" src="/img/offline.png" class="img-connection">';
                },
                className: 'text-center',
                searchable: false,
                orderable: false
            },
            {
                data: null,
                render: function(data)
                {
                    return '<div class="btn-group">'
                    + '          <button class="btn btn-default btn-wol" name="' + data.name + '" mac="' + data.mac + '" + ip="' + data.ip + '" title="Send WoL"><i class="fas fa-power-off"></i></button>'
                    + '     </div>';
                },
                className: 'text-center',
                searchable: false,
                orderable: false
            }
        ],
        drawCallback: function() {
            $(".btn-wol").unbind("click");
            $(".btn-wol").click(function(){
                var name = $(this).attr("name");
                var mac = $(this).attr("mac");
                var ip = $(this).attr("ip");
                wolModal = $.confirm({
                    title: 'Wake Confirmation',
                    content: 'Do you want to wake <b>' + name + '</b> up?',
                    type: 'green',
                    typeAnimated: true,
                    buttons: {
                        "Wake up!": function () {
                            sendWoL(name, mac, ip);
                            wolModal.close();
                        },
                        cancel: function () {
                            wolModal.close();
                        }
                    }
                });
            });
        },
    });
}

function sendWoL(name, mac, ip) {
    notifyWoL(name);
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "./wol/" + mac + "&" + ip, true);
    xhttp.send();
}

function notifyWoL(name){
	toastr.options = {
	  "closeButton": false,
	  "debug": false,
	  "newestOnTop": false,
	  "progressBar": false,
	  "positionClass": "toast-top-center",
	  "preventDuplicates": false,
	  "onclick": null,
	  "showDuration": "300",
	  "hideDuration": "1000",
	  "timeOut": "5000",
	  "extendedTimeOut": "1000",
	  "showEasing": "swing",
	  "hideEasing": "linear",
	  "showMethod": "fadeIn",
	  "hideMethod": "fadeOut"
	}
	toastr["success"]("Turning " + name + " on.", "WoL Sent")
}

function threadGetDevices(){
    window.setInterval(function () {
        httpGetAsync("/getDevices", function (devices) {
            var devices = JSON.parse(devices);
            if(devices != null && devices.length > 0){
                devices.forEach(function callback(device, index, array) {
                    if(device.status){
                        $("#img" + device.id).attr("src", "/img/online.png");
                    }else{
                        $("#img" + device.id).attr("src", "/img/offline.png");
                    }
                });
            }
        });
    }, 2500);

    function httpGetAsync(theUrl, callback) {
        var httpRequest = new XMLHttpRequest();
        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                var data = httpRequest.responseText;
                if (callback) {
                    callback(data);
                }
            }
        };
        httpRequest.open("GET", theUrl, true);
        httpRequest.send(null);
    }
}