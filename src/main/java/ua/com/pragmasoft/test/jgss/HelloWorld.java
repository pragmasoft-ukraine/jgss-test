
package ua.com.pragmasoft.test.jgss;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;

import com.sun.security.auth.callback.TextCallbackHandler; // NOSONAR 

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.Oid;

public class HelloWorld {
    static final Logger logger = Logger.getLogger(HelloWorld.class.getName());

    public static void main(String... args) throws Exception {
        logger.info("This is a module-using Hello World!");
        CallbackHandler callbackHandler = new TextCallbackHandler();
        var loginContext = new LoginContext("jgss-test", callbackHandler);
        loginContext.login();
        var clientSubject = loginContext.getSubject();
        logSubject(clientSubject);

        loginContext = new LoginContext("fna");
        loginContext.login();
        var serviceSubject = loginContext.getSubject();
        logSubject(serviceSubject);
        logContextSubject();

        final GSSManager gssManager = GSSManager.getInstance();
        final Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");
        final var serverName = gssManager.createName("fna/zdv-office-ubuntu", null);

        var doGetClientContext = new PrivilegedExceptionAction<GSSContext>() {

            @Override
            public GSSContext run() throws Exception {
                return gssManager.createContext(serverName, krb5Oid, null, GSSContext.DEFAULT_LIFETIME);
            }

        };

        var clientCtx = Subject.doAs(clientSubject, doGetClientContext);
        byte[] token = new byte[0];
        token = clientCtx.initSecContext(token, 0, token.length);
        logger.info("gss context " + clientCtx.getSrcName() + " > " + clientCtx.getTargName() + " is "
                + (clientCtx.isEstablished() ? "" : "not") + " established");
        clientCtx.dispose();

    }

    private static void logSubject(Subject subject) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Subject's principals:");
            for (var p : subject.getPrincipals()) {
                logger.info(p.getClass().getName() + " > " + p.toString());
            }
            for (var p : subject.getPrivateCredentials()) {
                logger.info(p.getClass().getName() + " > " + p.toString());
            }
            for (var p : subject.getPublicCredentials()) {
                logger.info(p.getClass().getName() + " > " + p.toString());
            }
        }
    }

    private static void logContextSubject() {
        var acc = AccessController.getContext();
        var ctxSubject = Subject.getSubject(acc);
        logger.info("context subject");

        if (ctxSubject != null) {
            logSubject(ctxSubject);
        } else {
            logger.info("Context subject is not set");
        }
    }

}
