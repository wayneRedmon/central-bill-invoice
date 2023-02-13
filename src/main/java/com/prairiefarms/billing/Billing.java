package com.prairiefarms.billing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Billing {

    private static final Logger LOGGER = LoggerFactory.getLogger(Billing.class);

    public static void main(String[] args) {
        LOGGER.info("");  //prime the logger

        final LaunchParams launchParams = new LaunchParams(args);

        try {
            if (launchParams.init()) {
                LOGGER.info(launchParams.getCLIOptions());

                if (BillingEnvironment.getInstance().init(launchParams.getCommandLine())) {
                    new BillingService().init();
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Exception in Billing.main()", exception);
        }
    }
}
