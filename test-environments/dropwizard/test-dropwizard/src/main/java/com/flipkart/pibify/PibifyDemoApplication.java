package com.flipkart.pibify;

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
import pibify.generated.pibify.PibifyHandlerCache;

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

        environment.jersey().register(new JakartaPibifyMessageBodyWriter(PibifyHandlerCache.getInstance(), new PibifySampler()));
        environment.jersey().register(new SampleResource());
        environment.jersey().register(new JakartaParityCheckerResource(PibifyHandlerCache.getInstance()));
        IParityChecker parityChecker = new PibifyParityChecker(PibifyHandlerCache.getInstance(), new ParityCheckerListener(), Optional.of(() -> null));
        environment.jersey().register(new JakartaJsonResponseFilter(parityChecker, new PibifySampler()));
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
