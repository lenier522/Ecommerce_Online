$(function(){

    // Validación de registro de usuario
    var $userRegister = $("#userRegister");

    $userRegister.validate({
        rules: {
            name: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNumber: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            password: {
                required: true,
                space: true
            },
            confirmpassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true
               // space: true
            },
            state: {
                required: true
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true
            },
            img: {
                required: true
            }
        },
        messages: {
            name: {
                required: 'Nombre requerido',
                lettersonly: 'Nombre inválido'
            },
            email: {
                required: 'Correo requerido',
                space: 'No se permiten espacios',
                email: 'Correo inválido'
            },
            mobileNumber: {
                required: 'Número de móvil requerido',
                space: 'No se permiten espacios',
                numericOnly: 'Número de móvil inválido',
                minlength: 'Mínimo 10 dígitos',
                maxlength: 'Máximo 12 dígitos'
            },
            password: {
                required: 'Contraseña requerida',
                space: 'No se permiten espacios'
            },
            confirmpassword: {
                required: 'Confirmar contraseña requerida',
                space: 'No se permiten espacios',
                equalTo: 'Las contraseñas no coinciden'
            },
            address: {
                required: 'Dirección requerida',
                all: 'Dirección inválida'
            },
            city: {
                required: 'Municipio Requerido'
               // space: 'No se permiten espacios'
            },
            state: {
                required: 'Estado requerido'
            },
            pincode: {
                required: 'Código postal requerido',
                space: 'No se permiten espacios',
                numericOnly: 'Código postal inválido'
            },
            img: {
                required: 'Imagen requerida'
            }
        }
    });

    // Validación de pedidos
    var $orders = $("#orders");

    $orders.validate({
        rules: {
            firstName: {
                required: true,
                lettersonly: true
            },
            lastName: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNo: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                space: true
            },
            state: {
                required: true
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true
            },
            paymentType: {
                required: true
            }
        },
        messages: {
            firstName: {
                required: 'Nombre requerido',
                lettersonly: 'Nombre inválido'
            },
            lastName: {
                required: 'Apellido requerido',
                lettersonly: 'Apellido inválido'
            },
            email: {
                required: 'Correo requerido',
                space: 'No se permiten espacios',
                email: 'Correo inválido'
            },
            mobileNo: {
                required: 'Número de móvil requerido',
                space: 'No se permiten espacios',
                numericOnly: 'Número de móvil inválido',
                minlength: 'Mínimo 10 dígitos',
                maxlength: 'Máximo 12 dígitos'
            },
            address: {
                required: 'Dirección requerida',
                all: 'Dirección inválida'
            },
            city: {
                required: 'Municipio Requerido',
                space: 'No se permiten espacios'
            },
            state: {
                required: 'Estado requerido'
            },
            pincode: {
                required: 'Código postal requerido',
                space: 'No se permiten espacios',
                numericOnly: 'Código postal inválido'
            },
            paymentType: {
                required: 'Seleccione tipo de pago'
            }
        }
    });

    // Validación de restablecimiento de contraseña
    var $resetPassword = $("#resetPassword");

    $resetPassword.validate({
        rules: {
            password: {
                required: true,
                space: true
            },
            confirmPassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            }
        },
        messages: {
            password: {
                required: 'Contraseña requerida',
                space: 'No se permiten espacios'
            },
            confirmpassword: {
                required: 'Confirmar contraseña requerida',
                space: 'No se permiten espacios',
                equalTo: 'Las contraseñas no coinciden'
            }
        }
    });

});

jQuery.validator.addMethod('lettersonly', function(value, element) {
    return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
});

jQuery.validator.addMethod('space', function(value, element) {
    return /^[^-\s]+$/.test(value);
});

jQuery.validator.addMethod('all', function(value, element) {
    return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
});

jQuery.validator.addMethod('numericOnly', function(value, element) {
    return /^[0-9]+$/.test(value);
});