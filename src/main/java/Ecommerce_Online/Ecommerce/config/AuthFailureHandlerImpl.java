package Ecommerce_Online.Ecommerce.config;

import Ecommerce_Online.Ecommerce.model.UserDtls;
import Ecommerce_Online.Ecommerce.repository.UserRepository;
import Ecommerce_Online.Ecommerce.service.UserService;
import Ecommerce_Online.Ecommerce.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");

        UserDtls userDtls = userRepository.findByEmail(email);

        if (userDtls != null) {

            if (userDtls.getIsEnable()) {

                if (userDtls.getAccountNonLocked()) {

                    if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                        userService.increaseFailedAttempt(userDtls);
                    } else {
                        userService.userAccountLock(userDtls);
                        exception = new LockedException("Su cuenta ha sido bloqueada !! lo ha intentado 3 veces");
                    }
                } else {

                    if (userService.unlockAccountTimeExpired(userDtls)) {
                        exception = new LockedException("Su cuenta está desbloqueada Inicie Sessión");
                    } else {
                        exception = new LockedException("Su cuenta está bloqueada !! intentalo en un tiempo");
                    }
                }

            } else {
                exception = new LockedException("Su cuenta está inactiva");
            }
        } else {
            exception = new LockedException("Email o Contraseña incorrectos");
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
