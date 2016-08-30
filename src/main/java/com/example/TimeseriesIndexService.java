/*
 * File: TimeseriesIndexService.java
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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 * A JAX-RS REST service that returns sample static time series index JSON data
 * 
 * @author Phil Chung
 */
@Path("timeseriesIndex")
public class TimeseriesIndexService {
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/json" media type.
     *
     * @return String that will be returned as an application/json response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {
        JsonObject jolist = Json.createObjectBuilder()
        		.add("data", Json.createArrayBuilder()
        		.add(Json.createObjectBuilder()
                        .add("value", "1")
                        .add("label", "Dataset 1"))
        		.add(Json.createObjectBuilder()
                        .add("value", "2")
                        .add("label", "Dataset 2"))
        		.add(Json.createObjectBuilder()
                        .add("value", "3")
                        .add("label", "Dataset 3"))).build();
            Map<String, Boolean> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory jwf = Json.createWriterFactory(config);
            StringWriter sw = new StringWriter();
            try (JsonWriter jsonWriter = jwf.createWriter(sw)) {
                jsonWriter.writeObject(jolist);
            }
            catch (Exception e) {
            	e.printStackTrace();
            }
        	return Response.ok(sw.toString())
        			.header("Access-Control-Allow-Origin", "*")
        			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        			.build();
    }
}
