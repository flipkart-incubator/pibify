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

package com.flipkart.pibify.vertx;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.paritychecker.IParityChecker;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
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
    private final AbstractPibifySampler goLiveSampler;
    private final IParityChecker parityChecker;

    private PibifyDecoratedRoute(Route underlying, AbstractPibifyHandlerCache handlerCache,
                                 AbstractPibifySampler goLiveSampler,
                                 IParityChecker parityChecker) {
        this.underlying = underlying;
        this.handlerCache = handlerCache;
        this.goLiveSampler = goLiveSampler;
        this.parityChecker = parityChecker;
    }

    public static Route decorate(Route underlying, AbstractPibifyHandlerCache handlerCache,
                                 AbstractPibifySampler goLiveSampler, IParityChecker parityChecker) {
        return new PibifyDecoratedRoute(underlying, handlerCache, goLiveSampler, parityChecker);
    }

//    public static Route decorate(Route underlying, AbstractPibifyHandlerCache handlerCache) {
//        return decorate(underlying, handlerCache, AbstractPibifySampler.DEFAULT_SAMPLER, AbstractPibifySampler.DEFAULT_SAMPLER);
//    }

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
        handler(ctx -> {
            try {
                if (goLiveSampler.shouldSample() && ctx.parsedHeaders().accept().contains(HEADER_PIBIFY)) {
                    // if goLive is enabled and client is accepting proto
                    function.apply(ctx).onFailure(ctx::fail)
                            .onSuccess(body -> handleSuccess(ctx, body));
                } else {
                    // for non-pibify requests, if sampling is enabled
                    if (parityChecker.shouldSample()) {
                        ctx.response().endHandler(end -> {
                            function.apply(ctx).onSuccess(object -> parityChecker.checkParity(object, true));
                        });
                    }
                    ctx.next();
                }
            } catch (RuntimeException e) {
                ctx.fail(e);
            }
        });

        return underlying.respond(function);
    }

    private <T> void handleSuccess(RoutingContext ctx, T body) {
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
    }
}
