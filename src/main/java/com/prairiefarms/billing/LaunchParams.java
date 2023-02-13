package com.prairiefarms.billing;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LaunchParams {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchParams.class);

    private static final Options CLI_OPTIONS = new Options()
            .addRequiredOption(null, "username", true, "jdbc username")
            .addRequiredOption(null, "password", true, "jdbc password")
            .addRequiredOption(null, "server", true, "server name")
            .addRequiredOption(null, "library", true, "data library")
            .addRequiredOption(null, "dairy", true, "dairy id")
            .addRequiredOption(null, "frequency", true, "frequency")
            .addRequiredOption(null, "date", true, "billing date");

    private final String[] args;

    private CommandLine commandLine;

    public LaunchParams(String[] args) {
        this.args = args;
        this.setCommandLine();
    }

    private void setCommandLine() {
        try {
            commandLine = new DefaultParser().parse(CLI_OPTIONS, args);

            MDC.put("dairyId", commandLine.getOptionValue("dairy"));

            this.listOptions();
        } catch (Exception exception) {
            LOGGER.error("Exception in LaunchParams.setCommandLine()", exception);
        }
    }

    private void listOptions() {
        if (ObjectUtils.isNotEmpty(commandLine)) {
            StringBuilder cliOptions = new StringBuilder()
                    .append("\r\ncentral-billing-invoice started with the following CLI options:")
                    .append("\r\n")
                    .append(StringUtils.repeat("=", 125))
                    .append("\r\n");

            for (Option option : commandLine.getOptions())
                cliOptions.append("-")
                        .append(option.getLongOpt())
                        .append(" ")
                        .append(option.getLongOpt().equalsIgnoreCase("password") ? "<********> " : option.getValue() + " ");

            cliOptions.append("\r\n")
                    .append(StringUtils.repeat("-", 125))
                    .append("\r\n");

            LOGGER.info(cliOptions.toString());
        }
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public boolean isValid() {
        return ObjectUtils.isNotEmpty(commandLine);
    }
}
