package com.prairiefarms.billing;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class LaunchParams {

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

    private StringBuilder cliOptions;

    public LaunchParams(String[] args) {
        this.args = args;
    }

    public boolean init() throws ParseException {
        boolean success = false;

        commandLine = new DefaultParser().parse(CLI_OPTIONS, args);

        if (ObjectUtils.isNotEmpty(commandLine)) {
            MDC.put("dairyId", commandLine.getOptionValue("dairy"));

            cliOptions = new StringBuilder()
                    .append("\r\nCentral Bill RemitToTable started with the following CLI options:")
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

            success = true;
        }

        return success;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public String getCLIOptions() {
        return cliOptions.toString();
    }
}
