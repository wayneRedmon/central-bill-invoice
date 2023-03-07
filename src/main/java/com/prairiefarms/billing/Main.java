package com.prairiefarms.billing;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final Options CLI_OPTIONS = new Options()
            .addRequiredOption(null, "username", true, "jdbc username")
            .addRequiredOption(null, "password", true, "jdbc password")
            .addRequiredOption(null, "server", true, "server name")
            .addRequiredOption(null, "library", true, "data library")
            .addRequiredOption(null, "dairy", true, "dairy id")
            .addRequiredOption(null, "frequency", true, "frequency")
            .addRequiredOption(null, "date", true, "billing date");

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new DefaultParser().parse(CLI_OPTIONS, args);

            MDC.put("dairy", commandLine.getOptionValue("dairy"));

            LOGGER.info("");  //??? prime the logger ???
            LOGGER.info("\r\ncentral-bill-invoice for dairy #" + commandLine.getOptionValue("dairy") + " started with the following:" +
                    "\r\n" +
                    StringUtils.repeat("=", 125) +
                    "\r\n" +
                    Arrays.stream(commandLine.getOptions()).map(option -> "-" + option.getLongOpt() + " " + (option.getLongOpt().equalsIgnoreCase("password") ? "<********> " : option.getValue() + " ")).collect(Collectors.joining("", "", "\r\n" + StringUtils.repeat("-", 125))));

            if (Environment.getInstance().init(commandLine)) new Service().init();
        } catch (Exception exception) {
            LOGGER.error("Exception in Main.main()", exception);
        }
    }
}
