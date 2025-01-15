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

import com.flipkart.pibify.codegen.stub.AbstractPibifyHandlerCache;
import com.flipkart.pibify.dropwizard.JakartaPibifyMessageBodyWriter;
import com.flipkart.pibify.paritychecker.IParityChecker;
import com.flipkart.pibify.paritychecker.IParityCheckerListener;
import com.flipkart.pibify.paritychecker.PibifyParityChecker;
import com.flipkart.pibify.paritychecker.filter.JakartaJsonResponseFilter;
import com.flipkart.pibify.paritychecker.resource.JakartaParityCheckerResource;
import com.flipkart.pibify.resources.SampleResource;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.util.Optional;

public class PibifyDemoApplication extends Application<PibifyDemoConfiguration> {

    // TODO add integration tests

    public static void main(final String[] args) throws Exception {
        new PibifyDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "PibifyDemo";
    }

    @Override
    public void initialize(final Bootstrap<PibifyDemoConfiguration> bootstrap) {

    }

    @Override
    public void run(final PibifyDemoConfiguration configuration,
                    final Environment environment) {

        AbstractPibifyHandlerCache handlerCache = AbstractPibifyHandlerCache.getConcreteInstance("com.pibify.pibify.generated.test.dropwizard.PibifyHandlerCache");

        environment.jersey().register(new JakartaPibifyMessageBodyWriter(handlerCache, new PibifySampler()));
        environment.jersey().register(new SampleResource());
        environment.jersey().register(new JakartaParityCheckerResource(handlerCache));
        IParityChecker parityChecker = new PibifyParityChecker(handlerCache, new ParityCheckerListener(),
                Optional.of(() -> null), new PibifySampler());
        environment.jersey().register(new JakartaJsonResponseFilter(parityChecker));
    }

    private static class PibifySampler extends AbstractPibifySampler {
        @Override
        public int getSamplePercentage() {
            return 500;
        }
    }

    private static class ParityCheckerListener implements IParityCheckerListener {
        @Override
        public void parityCheckSucceeded(Object primary, Object pibified, Object requestContext) {
            System.out.println("Parity check succeeded");
        }

        @Override
        public void parityCheckFailed(Object primary, Object pibified, Object requestContext, AssertionError ae) {
            System.out.println("Parity check failed");
        }

        @Override
        public void parityCheckError(Object primary, Object pibified, Object requestContext, Throwable e) {
            System.out.println("Parity check error");
        }
    }
}
