/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.flipkart.pibify;

import com.flipkart.pibify.paritychecker.IParityCheckerListener;
import com.flipkart.pibify.paritychecker.PibifyParityChecker;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
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

        Router router = PibifyDecoratedRouter.decorate(Router.router(vertx), PibifyHandlerCacheImpl.getInstance(),
                new PibifySampler(), new PibifyParityChecker(PibifyHandlerCacheImpl.getInstance(),
                        new PibifyParityCheckListener(),
                        null,
                        new PibifySampler()));

        router.route().handler(BodyHandler.create());
        router.get("/products/:productID").handler(this::handleGetProduct);
        //router.get("/products").handler(this::handleListProducts);
        router.get("/products").respond(getProducts());

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    private Function<RoutingContext, Future<List<Product>>> getProducts() {

        return context -> {
            List<Product> productList = products.values().stream().map(p -> new Product(p.getString("id"),
                    p.getString("name"), p.getDouble("price"), p.getInteger("weight"))).collect(Collectors.toList());
            Promise<List<Product>> promise = Promise.promise();
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1);
                    promise.complete(productList);
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

    private static class PibifySampler extends AbstractPibifySampler {
        @Override
        public int getSamplePercentage() {
            return 500;
        }
    }

    private static class PibifyParityCheckListener implements IParityCheckerListener {

        @Override
        public void parityCheckSucceeded(Object primary, Object pibified, Object requestContext) {
            System.out.println(Thread.currentThread() + "Parity check succeeded");
        }

        @Override
        public void parityCheckFailed(Object primary, Object pibified, Object requestContext, AssertionError ae) {
            System.out.println(Thread.currentThread() + "Parity check failed");
        }

        @Override
        public void parityCheckError(Object primary, Object pibified, Object requestContext, Throwable e) {
            System.out.println(Thread.currentThread() + "Parity check error");
        }
    }
}