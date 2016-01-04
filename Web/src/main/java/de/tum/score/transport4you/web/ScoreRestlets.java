package de.tum.score.transport4you.web;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ScoreRestlets extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

       	router.attach("/users",UsersResource.class);
       	router.attach("/user/{email}/{password}",UserResource.class);

        return router;
    }
}
