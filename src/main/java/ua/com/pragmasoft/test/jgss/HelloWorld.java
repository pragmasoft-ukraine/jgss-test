package ua.com.pragmasoft.test.jgss;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


public class HelloWorld {

    public static void main(String... args) throws LoginException {
        Logger logger = Logger.getLogger(HelloWorld.class.getName());
        logger.info("This is a module-using Hello World!");
        CallbackHandler callbackHandler = new ConsoleCallbackHandler();
        var loginContext = new LoginContext("jgss-test", callbackHandler);
        loginContext.login();
        var subj = loginContext.getSubject();
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Subject's principals:");
            for (var p: subj.getPrincipals()) {
                logger.info(p.getClass().getName() + " > " + p.toString());
            }
        }
    }
}
