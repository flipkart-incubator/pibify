package com.flipkart.pibify;

import com.flipkart.pibify.resources.SampleResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class PibifyDemoApplication extends Application<PibifyDemoConfiguration> {

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

        environment.jersey().register(new SampleResource());
    }

}
