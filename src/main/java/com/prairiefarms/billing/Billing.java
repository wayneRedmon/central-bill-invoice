package com.prairiefarms.billing;

import com.prairiefarms.billing.utils.FolderMaintenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Billing {

    private static final Logger LOGGER = LoggerFactory.getLogger("siftingAppender");

    public static void main(String[] args) {
        final LaunchParams launchParams = new LaunchParams(args);

        if (launchParams.init()) {
            try {
                if (BillingEnvironment.getInstance().init(launchParams.getCommandLine())) {
                    new FolderMaintenance().clean();

                    final BillingService billingService = new BillingService();

                    billingService.init();
                }
            } catch (Exception exception) {
                LOGGER.error("Exception in Billing.main()", exception);
            }
        }
    }
}
