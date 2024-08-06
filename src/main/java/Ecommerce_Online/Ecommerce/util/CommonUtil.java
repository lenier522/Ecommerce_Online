package Ecommerce_Online.Ecommerce.util;

import Ecommerce_Online.Ecommerce.model.ProductOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    public Boolean sendMail(String url, String reciepentEmail) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("leniercruz02@gmail.com", "Lenier.0591");
        helper.setTo(reciepentEmail);

        String content = "<p>Hola,</p>" + "<p>Has solicitado cambiar su contraseña.</p>"
                + "<p>Has click en el link de abajo para cambiar su contraseña:</p>"
                + "<p><a href=\"" + url
                + "\">Cambiar Contraseña</a></p>";

        helper.setSubject("Cambio de Password");
        helper.setText(content, true);

        mailSender.send(message);

        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg = null;

    public Boolean sendEmailForProductOrder(ProductOrder order, String status) throws Exception {

        
        msg = "<p>[[name]]</p><br><p>Su Orden: <b>[[orderStatus]].</b></p>"
            + "<p><b>Detalles del Producto:</b></p>"
            + "<p>Nombre: [[productName]]</p>"
            + "<p>Categoría: [[category]]</p>"
            + "<p>Cantidad: [[quantity]]</p>"
            + "<p>Precio: [[price]]</p>"
            + "<p>Tipo de Pago: [[paymentType]]</p>";
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("leniercruz02@gmail.com", "Lenier.0591");
        helper.setTo(order.getOrderAddress().getEmail());

        msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]", status);
        msg = msg.replace("[[productName]]", order.getProduct().getTitle());
        msg = msg.replace("[[category]]", order.getProduct().getCategory());
        msg = msg.replace("[[quantity]]", order.getQuantity().toString());
        msg = msg.replace("[[price]]", order.getPrice().toString());
        msg = msg.replace("[[paymentType]]", order.getPaymentType());

        helper.setSubject("Estado de la Orden del Producto");
        helper.setText(msg, true);

        mailSender.send(message);

        return true;
    }

}
