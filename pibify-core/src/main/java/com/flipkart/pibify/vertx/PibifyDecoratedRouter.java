package com.flipkart.pibify.vertx;

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.AllowForwardHeaders;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

/**
 * This class is the decorated for vertx Route
 * Author bageshwar.pn
 * Date 02/10/24
 */
public class PibifyDecoratedRouter implements Router {

    private final Router underlying;
    private final AbstractPibifyHandlerCache handlerCache;
    private final AbstractPibifySampler sampler;

    public PibifyDecoratedRouter(Router underlying, AbstractPibifyHandlerCache handlerCache, AbstractPibifySampler sampler) {
        this.underlying = underlying;
        this.handlerCache = handlerCache;
        this.sampler = sampler;
    }

    public static Router decorate(Router underlying, AbstractPibifyHandlerCache handlerCache, AbstractPibifySampler sampler) {
        return new PibifyDecoratedRouter(underlying, handlerCache, sampler);
    }

    public static Router decorate(Router underlying, AbstractPibifyHandlerCache handlerCache) {
        return decorate(underlying, handlerCache, AbstractPibifySampler.DEFAULT_SAMPLER);
    }

    public static Router router(Vertx vertx) {
        return io.vertx.ext.web.Router.router(vertx);
    }

    @Override
    public Router putMetadata(String key, Object value) {
        return underlying.putMetadata(key, value);
    }

    @Override
    public Map<String, Object> metadata() {
        return underlying.metadata();
    }

    @Override
    public <T> T getMetadata(String key) {
        return underlying.getMetadata(key);
    }

    @Override
    public Route route() {
        return PibifyDecoratedRoute.decorate(underlying.route(), handlerCache, sampler);
    }

    @Override
    public Route route(HttpMethod method, String path) {
        return PibifyDecoratedRoute.decorate(underlying.route(method, path), handlerCache, sampler);
    }

    @Override
    public Route route(String path) {
        return PibifyDecoratedRoute.decorate(underlying.route(path), handlerCache, sampler);
    }

    @Override
    public Route routeWithRegex(HttpMethod method, String regex) {
        return PibifyDecoratedRoute.decorate(underlying.routeWithRegex(method, regex), handlerCache, sampler);
    }

    @Override
    public Route routeWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.routeWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route get() {
        return PibifyDecoratedRoute.decorate(underlying.get(), handlerCache, sampler);
    }

    @Override
    public Route get(String path) {
        return PibifyDecoratedRoute.decorate(underlying.get(path), handlerCache, sampler);
    }

    @Override
    public Route getWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.getWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route head() {
        return PibifyDecoratedRoute.decorate(underlying.head(), handlerCache, sampler);
    }

    @Override
    public Route head(String path) {
        return PibifyDecoratedRoute.decorate(underlying.head(path), handlerCache, sampler);
    }

    @Override
    public Route headWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.headWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route options() {
        return PibifyDecoratedRoute.decorate(underlying.options(), handlerCache, sampler);
    }

    @Override
    public Route options(String path) {
        return PibifyDecoratedRoute.decorate(underlying.options(path), handlerCache, sampler);
    }

    @Override
    public Route optionsWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.optionsWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route put() {
        return PibifyDecoratedRoute.decorate(underlying.put(), handlerCache, sampler);
    }

    @Override
    public Route put(String path) {
        return PibifyDecoratedRoute.decorate(underlying.put(path), handlerCache, sampler);
    }

    @Override
    public Route putWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.putWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route post() {
        return PibifyDecoratedRoute.decorate(underlying.post(), handlerCache, sampler);
    }

    @Override
    public Route post(String path) {
        return PibifyDecoratedRoute.decorate(underlying.post(path), handlerCache, sampler);
    }

    @Override
    public Route postWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.postWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route delete() {
        return PibifyDecoratedRoute.decorate(underlying.delete(), handlerCache, sampler);
    }

    @Override
    public Route delete(String path) {
        return PibifyDecoratedRoute.decorate(underlying.delete(path), handlerCache, sampler);
    }

    @Override
    public Route deleteWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.deleteWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route trace() {
        return PibifyDecoratedRoute.decorate(underlying.trace(), handlerCache, sampler);
    }

    @Override
    public Route trace(String path) {
        return PibifyDecoratedRoute.decorate(underlying.trace(path), handlerCache, sampler);
    }

    @Override
    public Route traceWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.traceWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route connect() {
        return PibifyDecoratedRoute.decorate(underlying.connect(), handlerCache, sampler);
    }

    @Override
    public Route connect(String path) {
        return PibifyDecoratedRoute.decorate(underlying.connect(path), handlerCache, sampler);
    }

    @Override
    public Route connectWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.connectWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public Route patch() {
        return PibifyDecoratedRoute.decorate(underlying.patch(), handlerCache, sampler);
    }

    @Override
    public Route patch(String path) {
        return PibifyDecoratedRoute.decorate(underlying.patch(path), handlerCache, sampler);
    }

    @Override
    public Route patchWithRegex(String regex) {
        return PibifyDecoratedRoute.decorate(underlying.patchWithRegex(regex), handlerCache, sampler);
    }

    @Override
    public List<Route> getRoutes() {
        return underlying.getRoutes();
    }

    @Override
    public Router clear() {
        return underlying.clear();
    }

    @Deprecated
    @Override
    public Route mountSubRouter(String mountPoint, Router subRouter) {
        return PibifyDecoratedRoute.decorate(underlying.mountSubRouter(mountPoint, subRouter), handlerCache, sampler);
    }

    @Override
    public Router errorHandler(int statusCode, Handler<RoutingContext> errorHandler) {
        return underlying.errorHandler(statusCode, errorHandler);
    }

    @Override
    public void handleContext(RoutingContext context) {
        underlying.handleContext(context);
    }

    @Override
    public void handleFailure(RoutingContext context) {
        underlying.handleFailure(context);
    }

    @Override
    public Router modifiedHandler(Handler<Router> handler) {
        return underlying.modifiedHandler(handler);
    }

    @Override
    public Router allowForward(AllowForwardHeaders allowForwardHeaders) {
        return underlying.allowForward(allowForwardHeaders);
    }

    @Override
    public void handle(HttpServerRequest event) {
        underlying.handle(event);
    }
}
