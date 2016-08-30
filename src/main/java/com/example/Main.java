/*
 * File: Main.java
 *
 * Copyright (c) 2015, 2016 Oracle and/or its affiliates.
 *
 * You may not use this file except in compliance with the Universal Permissive
 * License (UPL), Version 1.0 (the "License.")
 *
 * You may obtain a copy of the License at https://opensource.org/licenses/UPL.
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class - Starts Jersey JAX-RS in a Grizzly HTTP server
 * 
 * @author Phil Chung
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
	public static String BASE_URI;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server
     */
    public static HttpServer startServer() throws UnknownHostException {
    	// Base URI the Grizzly HTTP server will listen on
    	final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
    	final Optional<String> hostName = Optional.ofNullable(System.getenv("HOSTNAME"));
		BASE_URI = "http://" + hostName.orElse("localhost") + ":" + port.orElse("8080") + "/";

        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig().packages("com.example");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        
        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
/*            This commented code would be written prior to Java 8 java.time.* API    */	
//            final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//            
//            @Override
//            public void service(Request request, Response response) throws Exception {
//                final Date now = new Date();
//                final String formattedTime;
//                synchronized (formatter) {
//                    formattedTime = formatter.format(now);
//                }
//                        
//                response.setContentType("text/plain");
//                response.getWriter().write("The time using the old Java Date object is: " + formattedTime);
//            }
            @Override
            public void service(Request request, Response response) throws Exception {
                response.setContentType("text/plain");
                response.getWriter().write("The time using the new java.time API in Java 8 is: " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            }
        }, "/time");
        
     // Add the StaticHttpHandler to serve static resources from the static folder
//        server.getServerConfiguration().addHttpHandler(
//                new StaticHttpHandler("src/main/resources/static/"), "static/");

        // Add the CLStaticHttpHandler to serve static resources located at
        // the static folder from the jar file jersey-example-1.0-SNAPSHOT.jar
        server.getServerConfiguration().addHttpHandler(
                new CLStaticHttpHandler(new URLClassLoader(new URL[] {
                    new File("target/repo/com/example/1.0-SNAPSHOT/jersey-example-1.0-SNAPSHOT.jar").toURI().toURL()}), "static/"), "/jarstatic");
        System.out.println("In order to test the server please try the following urls:");
        System.out.println(String.format("%s to see the default resource", BASE_URI));
        System.out.println(String.format("%sapplication.wadl to see the WADL resource", BASE_URI));
        System.out.println(String.format("%stweets to see the JAX-RS resource", BASE_URI));
        System.out.println(String.format("%stime to see the time", BASE_URI));
        System.out.println(String.format("%sjarstatic/index.html to see the jar static resource", BASE_URI));
        System.out.println();
        System.out.println("Press enter to stop the server...");
        
        System.in.read();
        server.shutdown();
    }
}
