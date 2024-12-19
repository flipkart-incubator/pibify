package com.flipkart.pibify;

import com.flipkart.pibify.dropwizard.JakartaPibifyMessageBodyWriter;
import com.flipkart.pibify.paritychecker.resource.JakartaParityCheckerResource;
import com.flipkart.pibify.resources.SampleResource;
import com.flipkart.pibify.sampler.AbstractPibifySampler;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import pibify.generated.pibify.PibifyHandlerCache;

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

        environment.jersey().register(new JakartaPibifyMessageBodyWriter(PibifyHandlerCache.getInstance(), new PibifySampler()));
        environment.jersey().register(new SampleResource());
        environment.jersey().register(new JakartaParityCheckerResource(PibifyHandlerCache.getInstance()));
    }

    private static class PibifySampler extends AbstractPibifySampler {
        @Override
        public boolean enabled() {
            return true;
        }

        @Override
        public int getSamplePercentage() {
            return 500;
        }
    }
}
