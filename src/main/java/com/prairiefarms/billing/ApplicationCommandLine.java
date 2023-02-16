package com.prairiefarms.billing;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class ApplicationCommandLine {

    private static final Options CLI_OPTIONS = new Options()
            .addRequiredOption(null, "username", true, "jdbc username")
            .addRequiredOption(null, "password", true, "jdbc password")
            .addRequiredOption(null, "server", true, "server name")
            .addRequiredOption(null, "library", true, "data library")
            .addRequiredOption(null, "dairy", true, "dairy id")
            .addRequiredOption(null, "frequency", true, "frequency")
            .addRequiredOption(null, "date", true, "billing date");

    private final CommandLine commandLine;

    private static StringBuilder cliOptionsAsText;

    public ApplicationCommandLine(String[] args) throws Exception {
        this.commandLine = new DefaultParser().parse(CLI_OPTIONS, args);

        MDC.put("dairyId", commandLine.getOptionValue("dairy"));

        if (ObjectUtils.isNotEmpty(commandLine)) {
            cliOptionsAsText = new StringBuilder()
                    .append("\r\ncentral-billing-invoice started with the following CLI options:")
                    .append("\r\n")
                    .append(StringUtils.repeat("=", 125))
                    .append("\r\n");

            for (Option option : commandLine.getOptions())
                cliOptionsAsText
                        .append("-")
                        .append(option.getLongOpt())
                        .append(" ")
                        .append(option.getLongOpt().equalsIgnoreCase("password") ? "<********> " : option.getValue() + " ");

            cliOptionsAsText
                    .append("\r\n")
                    .append(StringUtils.repeat("-", 125))
                    .append("\r\n");
        }
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public String getCLIOptionsAsText() {
        return cliOptionsAsText.toString();
    }
}
