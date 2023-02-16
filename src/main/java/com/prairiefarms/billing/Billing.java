package com.prairiefarms.billing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Billing {

    private static final Logger LOGGER = LoggerFactory.getLogger(Billing.class);

    public static void main(String[] args) {
        try {
            ApplicationCommandLine applicationCommandLine = new ApplicationCommandLine(args);

            LOGGER.info(applicationCommandLine.getCLIOptionsAsText());

            if (Environment.getInstance().init(applicationCommandLine.getCommandLine()))
                new Service().init();
        } catch (Exception exception) {
            LOGGER.error("Exception in Billing.main()", exception);
        }
    }
}
