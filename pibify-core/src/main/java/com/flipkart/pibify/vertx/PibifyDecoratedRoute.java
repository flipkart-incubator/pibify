package com.flipkart.pibify.vertx;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.ParsableMIMEValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * This class is the decorated for vertx Route
 * Author bageshwar.pn
 * Date 04/10/24
 */
public class PibifyDecoratedRoute implements Route {

    private final static MIMEHeader HEADER_JSON = new ParsableMIMEValue("application/json");
    private final static MIMEHeader HEADER_PIBIFY = new ParsableMIMEValue("application/proto");

    private final Route underlying;
    private final AbstractPibifyHandlerCache handlerCache;

    private PibifyDecoratedRoute(Route underlying, AbstractPibifyHandlerCache handlerCache) {
        this.underlying = underlying;
        this.handlerCache = handlerCache;
    }

    public static Route decorate(Route underlying, AbstractPibifyHandlerCache handlerCache) {
        return new PibifyDecoratedRoute(underlying, handlerCache);
    }

    @Override
    public Route putMetadata(String key, Object value) {
        return underlying.putMetadata(key, value);
    }

    @Override
    public Route method(HttpMethod method) {
        return underlying.method(method);
    }

    @Override
    public Route path(String path) {
        return underlying.path(path);
    }

    @Override
    public Route pathRegex(String path) {
        return underlying.pathRegex(path);
    }

    @Override
    public Route produces(String contentType) {
        return underlying.produces(contentType);
    }

    @Override
    public Route consumes(String contentType) {
        return underlying.consumes(contentType);
    }

    @Override
    public Route virtualHost(String hostnamePattern) {
        return underlying.virtualHost(hostnamePattern);
    }

    @Override
    public Route order(int order) {
        return underlying.order(order);
    }

    @Override
    public Route last() {
        return underlying.last();
    }

    @Override
    public Route handler(Handler<RoutingContext> requestHandler) {
        return underlying.handler(requestHandler);
    }

    @Override
    public Route blockingHandler(Handler<RoutingContext> requestHandler) {
        return underlying.blockingHandler(requestHandler);
    }

    @Override
    public Route subRouter(Router subRouter) {
        return underlying.subRouter(subRouter);
    }

    @Override
    public Route blockingHandler(Handler<RoutingContext> requestHandler, boolean ordered) {
        return underlying.blockingHandler(requestHandler, ordered);
    }

    @Override
    public Route failureHandler(Handler<RoutingContext> failureHandler) {
        return underlying.failureHandler(failureHandler);
    }

    @Override
    public Route remove() {
        return underlying.remove();
    }

    @Override
    public Route disable() {
        return underlying.disable();
    }

    @Override
    public Route enable() {
        return underlying.enable();
    }

    @Deprecated
    @Override
    public Route useNormalisedPath(boolean useNormalizedPath) {
        return underlying.useNormalisedPath(useNormalizedPath);
    }

    @Override
    public Route useNormalizedPath(boolean useNormalizedPath) {
        return underlying.useNormalizedPath(useNormalizedPath);
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
    public String getPath() {
        return underlying.getPath();
    }

    @Override
    public boolean isRegexPath() {
        return underlying.isRegexPath();
    }

    @Override
    public boolean isExactPath() {
        return underlying.isExactPath();
    }

    @Override
    public Set<HttpMethod> methods() {
        return underlying.methods();
    }

    @Override
    public Route setRegexGroupsNames(List<String> groups) {
        return underlying.setRegexGroupsNames(groups);
    }

    @Override
    public Route setName(String name) {
        return underlying.setName(name);
    }

    @Override
    public String getName() {
        return underlying.getName();
    }

    @Override
    public <T> Route respond(Function<RoutingContext, Future<T>> function) {
        // add pibify handler and then add default handler
        handler(ctx -> {
            try {
                function.apply(ctx)
                        .onFailure(ctx::fail)
                        .onSuccess(body -> {
                            if (ctx.parsedHeaders().accept().contains(HEADER_PIBIFY)) {
                                System.out.println("Responding from decorator pibify");
                                ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, HEADER_PIBIFY.value());

                                try {
                                    @SuppressWarnings("rawtypes")
                                    Optional<? extends PibifyGenerated> handler = handlerCache.getHandler(body.getClass());
                                    if (handler.isPresent()) {
                                        //noinspection unchecked
                                        ctx.end(Buffer.buffer(handler.get().serialize(body)));
                                    } else {
                                        throw new PibifyCodeExecException("Handler missing for class " + body.getClass().getName());
                                    }
                                } catch (Exception e) {
                                    ctx.fail(e);
                                }
                            } else {
                                ctx.next();
                            }
                        });
            } catch (RuntimeException e) {
                ctx.fail(e);
            }
        });

        return underlying.respond(function);
    }
}
