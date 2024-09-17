jQuery.validator.addMethod("integer", function(value, element) {
    return this.optional(element) || /^[1-9][0-9]*$/.test(value);
}, 'Escribe un n&uacute;mero entero v&aacute;lido.');

jQuery.extend(jQuery.validator.messages, {
      required: "Este campo es obligatorio.",
      remote: "Rellena este campo.",
      email: "Escribe una direcci&oacute;n de correo v&aacute;lida.",
      url: "Escribe una URL v&aacute;lida.",
      date: "Escribe una fecha v&aacute;lida.",
      dateISO: "Escribe una fecha (ISO) v&aacute;lida.",
      number: "Escribe un n&uacute;mero entero v&aacute;lido.",
      digits: "Escribe solo d&iacute;gitos.",
      creditcard: "Escribe un n&uacute;mero de tarjeta v&aacute;lido.",
      equalTo: "Escribe el mismo valor de nuevo.",
      accept: "Escribe un valor con una extensi&oacute;n aceptada.",
      maxlength: jQuery.validator.format("No escribas m&aacute;s de {0} caracteres."),
      minlength: jQuery.validator.format("No escribas menos de {0} caracteres."),
      rangelength: jQuery.validator.format("Escribe un valor entre {0} y {1} caracteres."),
      range: jQuery.validator.format("Escribe un valor entre {0} y {1}."),
      max: jQuery.validator.format("Escribe un valor menor o igual a {0}."),
      min: jQuery.validator.format("Escribe un valor mayor o igual a {0}.")
});

jQuery.extend(jQuery.validator.setDefaults({
    highlight: function(element) {
        $(element).addClass("is-invalid").removeClass("is-valid");
    },
    unhighlight: function(element) {
        $(element).addClass("is-valid").removeClass("is-invalid");
    },
    errorClass: "text-danger"
}));

