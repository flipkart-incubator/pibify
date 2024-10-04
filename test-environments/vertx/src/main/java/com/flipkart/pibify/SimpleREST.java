/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package com.flipkart.pibify;

import com.flipkart.pibify.vertx.PibifyDecoratedRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleREST extends AbstractVerticle {

    private Map<String, JsonObject> products = new HashMap<>();

    public static void main(String[] args) {
        Launcher.executeCommand("run", SimpleREST.class.getName());
    }

    @Override
    public void start() {

        setUpInitialData();

        Router router = PibifyDecoratedRouter.decorate(Router.router(vertx), PibifyHandlerCacheImpl.getInstance());

        router.route().handler(BodyHandler.create());
        router.get("/products/:productID").handler(this::handleGetProduct);
        //router.get("/products").handler(this::handleListProducts);
        router.get("/products").respond(getProducts());

        /*
        Strategy
        1. Create a new Router instead of Router.router()
        2. return a Decorator instance of Route for each of the 33 methods
        3. This Decorator takes care of the `respond` method by adding another handler based on the incoming request type
         */

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    private Function<RoutingContext, Future<List<Product>>> getProducts() {

        return context -> {
            List<Product> productList = products.values().stream().map(p -> new Product(p.getString("id"),
                    p.getString("name"), p.getDouble("price"), p.getInteger("weight"))).collect(Collectors.toList());
            Promise<List<Product>> promise = Promise.promise();
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1000);
                    promise.complete(productList);
                    //promise.complete(Buffer.buffer(new byte[5]));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            return promise.future();
        };
    }

    private void handleGetProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = products.get(productID);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void handleListProducts(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        products.forEach((k, v) -> arr.add(v));
        routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void setUpInitialData() {
        addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
        addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
        addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
    }

    private void addProduct(JsonObject product) {
        products.put(product.getString("id"), product);
    }
}