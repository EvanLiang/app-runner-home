package samples;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws Exception {

        // When run from app-runner, you must use the port set in the environment variable APP_PORT
        int port = Integer.parseInt(firstNonNull(System.getenv("APP_PORT"), "8080"));
        // All URLs must be prefixed with the app name, which is got via the APP_NAME env var.
        String appName = firstNonNull(System.getenv("APP_NAME"), "my-app");

        Server jettyServer = new Server(new InetSocketAddress("localhost", port));
        jettyServer.setStopAtShutdown(true);

        HandlerList handlers = new HandlerList();
        // TODO: set your own handlers
        handlers.addHandler(resourceHandler());

        // you must serve everything from a directory named after your app
        ContextHandler ch = new ContextHandler();
        ch.setContextPath("/" + appName);
        ch.setHandler(handlers);
        jettyServer.setHandler(ch);

        jettyServer.start();

        log.info("Started " + appName + " at http://localhost:" + port + ch.getContextPath());

        jettyServer.join();
    }

    private static Handler resourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newClassPathResource("/web", false, false));
        resourceHandler.setWelcomeFiles(new String[] {"index.html"});
        resourceHandler.setMinMemoryMappedContentLength(-1);
        return resourceHandler;
    }

}