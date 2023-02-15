package com.prairiefarms.billing;

import com.prairiefarms.billing.utils.Credentials;
import com.prairiefarms.billing.utils.Frequency;
import com.prairiefarms.utils.database.HostConnection;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Environment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Environment.class);

    private static final String APPLICATION_PROPERTIES = "./prairiefarmsApplication.properties";
    private static final String BILLING_TYPE = "invoice";
    private static final String PATH_TO_DAIRY_BUSINESS_FILE = "./DairyBusiness.csv";
    private static final String XLSX_TEMPLATE_PATH = "./templates/DistributorInvoice.xlsx";

    private static CommandLine commandLine;
    private static Credentials credentials;
    private static String server;
    private static String library;
    private static String corporateName;
    private static int dairyId;
    private static String dairyLogoPath;
    private static Frequency frequency;
    private static LocalDate billingDate;
    private static String emailServer;
    private static List<String> emailCarbonCopy;
    private static String xlsxDocumentPassword;
    private static InputStream xlsxTemplate;

    private static class SingletonHelper {
        private static final Environment INSTANCE = new Environment();
    }

    Environment() {
    }

    public static Environment getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean init(CommandLine commandLine) {
        Environment.commandLine = commandLine;

        boolean ready = false;

        if (setCredentials()) {
            if (setServer()) {
                try {
                    if (setLibrary() && setDairy() && setFrequency() && setBillingDate() && setXlsxTemplate()) {
                        if (setEmailServer()) ready = setEmailCarbonCopy();
                    }
                } catch (Exception exception) {
                    LOGGER.error("Exception in Environment.init()", exception);
                }
            }
        }

        return ready;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getServer() {
        return server;
    }

    public String getLibrary() {
        return library;
    }

    public int getDairyId() {
        return dairyId;
    }

    public String getDairyLogoPath() {
        return dairyLogoPath;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public String frequencyAsText() {
        return frequency.type;
    }

    public String billingDateAsUSA() {
        return billingDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public String billingDateAsYYMMD() {
        return billingDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String getEmailServer() {
        return emailServer;
    }

    public String getXlsxDocumentPassword() { return xlsxDocumentPassword; }

    public List<String> emailCarbonCopy() {
        return emailCarbonCopy;
    }

    public String emailOutBox() {
        return "mail/" + library + "/" + BILLING_TYPE + "/" + frequency.type + "/";
    }

    public String emailSentBox() {
        return "mail/" + library + "/" + BILLING_TYPE + "/" + frequency.type + "/sent/";
    }

    public int getPageCount(int linesPerPage, int lines) {
        linesPerPage = linesPerPage <= 0 ? 1 : linesPerPage;
        lines = lines <= 0 ? 1 : lines;

        return lines % linesPerPage != 0 ? lines / linesPerPage + 1 : lines / linesPerPage;
    }

    public InputStream getXlsxTemplate() { return xlsxTemplate; }

    private static boolean setCredentials() {
        credentials = null;

        if (StringUtils.isNotBlank(commandLine.getOptionValue("username")) && StringUtils.isNotBlank(commandLine.getOptionValue("password")))
            credentials = new Credentials(
                    StringUtils.normalizeSpace(commandLine.getOptionValue("username")),
                    StringUtils.normalizeSpace(commandLine.getOptionValue("password"))
            );

        return ObjectUtils.isNotEmpty(credentials);
    }

    private static boolean setServer() {
        server = "";

        if (StringUtils.isNotBlank(commandLine.getOptionValue("server"))) {
            server = StringUtils.normalizeSpace(commandLine.getOptionValue("server"));
        }

        return StringUtils.isNotBlank(server);
    }

    private static boolean setLibrary() {
        library = "";

        if (StringUtils.isNotBlank(commandLine.getOptionValue("library")))
            library = StringUtils.normalizeSpace(commandLine.getOptionValue("library"));

        return StringUtils.isNotBlank(library);
    }

    private static boolean setDairy() {
        final String sql = "select hdgName from ds_Hdg where hdg#=1";

        dairyId = 0;
        corporateName = "";
        dairyLogoPath = "";

        if (StringUtils.isNotBlank(commandLine.getOptionValue("dairy")) && NumberUtils.isDigits(commandLine.getOptionValue("dairy"))) {
            String dairyHeadingName = "";

            try (HostConnection hostConnection = new HostConnection(server, credentials, library)) {
                try (Statement statement = hostConnection.get().createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(sql)) {
                        if (resultSet.next()) {
                            dairyHeadingName = resultSet.getString("hdgName");
                        }
                    }
                }
            } catch (SQLException exception) {
                LOGGER.error("Exception in Environment.setDairy()", exception);
            }

            if (StringUtils.isNotBlank(dairyHeadingName)) {
                try {
                    Scanner scanner = new Scanner(new File(PATH_TO_DAIRY_BUSINESS_FILE));

                    while (scanner.hasNextLine()) {
                        String[] stringArray = scanner.nextLine().split("\",\"");

                        if (StringUtils.normalizeSpace(dairyHeadingName.toLowerCase(Locale.ROOT))
                                .contains(StringUtils.normalizeSpace(stringArray[2].replace("\"", "").toLowerCase(Locale.ROOT)))) {
                            dairyId = NumberUtils.toInt(commandLine.getOptionValue("dairy"));
                            corporateName = stringArray[0].replace("\"", "");
                            dairyLogoPath = stringArray[1].replace("\"", "");

                            break;
                        }
                    }
                } catch (FileNotFoundException exception) {
                    LOGGER.error("Exception in Environment.setDairy()", exception);
                }
            }
        }

        return StringUtils.isNotBlank(corporateName) &&
                (ObjectUtils.isNotEmpty(dairyId) && dairyId > 0) &&
                StringUtils.isNotBlank(dairyLogoPath);
    }

    private static boolean setFrequency() {
        frequency = null;

        if (StringUtils.isNotBlank(commandLine.getOptionValue("frequency")))
            frequency = Frequency.valueOf(commandLine.getOptionValue("frequency").toUpperCase(Locale.ROOT));

        return ObjectUtils.isNotEmpty(frequency);
    }

    private static boolean setBillingDate() {
        billingDate = null;

        if (StringUtils.isNotBlank(commandLine.getOptionValue("date")))
            billingDate = LocalDate.parse(commandLine.getOptionValue("date"), DateTimeFormatter.ofPattern("MMddyy"));

        return ObjectUtils.isNotEmpty(billingDate);
    }

    private static boolean setEmailServer() {
        emailServer = "";

        try (InputStream inputStream = Files.newInputStream(Paths.get(APPLICATION_PROPERTIES))) {
            final Properties properties = new Properties();

            properties.load(inputStream);

            emailServer = properties.getProperty("emailServer");
            xlsxDocumentPassword = properties.getProperty("xlsxPassword");
        } catch (IOException exception) {
            LOGGER.error("Exception in Environment.setEmailServer()", exception);
        }

        return StringUtils.isNotBlank(emailServer);
    }

    private static boolean setEmailCarbonCopy() {
        final String sql = "select email from ds_EmailCC";

        emailCarbonCopy = new ArrayList<>();

        try (HostConnection hostConnection = new HostConnection(server, credentials, library)) {
            try (Statement statement = hostConnection.get().createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        if (StringUtils.isNotBlank(resultSet.getString("email"))) {
                            emailCarbonCopy.add(resultSet.getString("email"));
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            LOGGER.error("Exception in Environment.setEmailCarbonCopy()", exception);
        }

        return ObjectUtils.isNotEmpty(emailCarbonCopy);
    }

    private static boolean setXlsxTemplate() {
        boolean templateFound = false;

        try {
            xlsxTemplate = new FileInputStream(XLSX_TEMPLATE_PATH);

            templateFound = true;
        } catch (FileNotFoundException exception) {
            LOGGER.error("Exception in Environment.setXlsxTemplate()", exception);
        }

        return templateFound;
    }
}
