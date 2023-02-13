package com.prairiefarms.billing;

import com.prairiefarms.billing.utils.FolderMaintenance;
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
                    FolderMaintenance.clean();

                    final BillingService billingService = new BillingService();

                    billingService.init();
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Exception in Billing.main()", exception);
        }
    }
}
