package ua.com.pragmasoft.test.jgss;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.sun.security.auth.callback.TextCallbackHandler; // NOSONAR 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KrbClient implements Runnable {

    final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        try {
            log.info("This is a module based Kerberos Client Server prototype");
            CallbackHandler callbackHandler = new TextCallbackHandler();
            var loginContext = new LoginContext("jgss-test", callbackHandler);
            loginContext.login();
            var clientSubject = loginContext.getSubject();
            // logSubject(clientSubject);
        } catch (LoginException e) {
            throw new IllegalStateException(e);
        }
    }
    
}