package com.prairiefarms.billing;

import com.prairiefarms.billing.utils.Credentials;
import com.prairiefarms.billing.utils.Frequency;
import com.prairiefarms.utils.database.HostConnection;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BillingEnvironment {

    private static final String APPLICATION_PROPERTIES = "./prairiefarmsApplication.properties";
    private static final String BILLING_TYPE = "invoice";
    private static final String PATH_TO_DAIRY_BUSINESS_FILE = "./DairyBusiness.csv";

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

    private static class SingletonHelper {
        private static final BillingEnvironment INSTANCE = new BillingEnvironment();
    }

    BillingEnvironment() {
    }

    public static BillingEnvironment getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean init(CommandLine commandLine) throws Exception {
        boolean ready = false;

        BillingEnvironment.commandLine = commandLine;

        if (setCredentials()) {
            if (setServer()) {
                if (setLibrary() && setDairy() && setFrequency() && setBillingDate()) {
                    if (setEmailServer()) ready = setEmailCarbonCopy();
                }
            }
        }

        return ready;
    }

    private static boolean setCredentials() {
        credentials = null;

        if (StringUtils.isNotBlank(commandLine.getOptionValue("username")) && StringUtils.isNotBlank(commandLine.getOptionValue("password")))
            credentials = new Credentials(StringUtils.normalizeSpace(commandLine.getOptionValue("username")), StringUtils.normalizeSpace(commandLine.getOptionValue("password")));

        return ObjectUtils.isNotEmpty(credentials);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    private static boolean setServer() {
        server = "";

        if (StringUtils.isNotBlank(commandLine.getOptionValue("server"))) {
            server = StringUtils.normalizeSpace(commandLine.getOptionValue("server"));
        }

        return StringUtils.isNotBlank(server);
    }

    public String getServer() {
        return server;
    }

    private static boolean setLibrary() {
        library = "";

        if (StringUtils.isNotBlank(commandLine.getOptionValue("library")))
            library = StringUtils.normalizeSpace(commandLine.getOptionValue("library"));

        return StringUtils.isNotBlank(library);
    }

    public String getLibrary() {
        return library;
    }

    private static boolean setDairy() throws SQLException, FileNotFoundException {
        //final String sql = "select subStr(hdgName,1,15) as nameText from ds_Hdg where hdg#=1";
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
            }

            if (StringUtils.isNotBlank(dairyHeadingName)) {
                Scanner scanner = new Scanner(new File(PATH_TO_DAIRY_BUSINESS_FILE));

                while (scanner.hasNextLine()) {
                    String[] stringArray = scanner.nextLine().split("\",\"");

                    if (StringUtils.normalizeSpace(dairyHeadingName.toLowerCase(Locale.ROOT)).contains(StringUtils.normalizeSpace(stringArray[2].replace("\"", "").toLowerCase(Locale.ROOT)))) {
                        dairyId = NumberUtils.toInt(commandLine.getOptionValue("dairy"));
                        corporateName = stringArray[0].replace("\"", "");
                        dairyLogoPath = stringArray[1].replace("\"", "");

                        break;
                    }
                }
            }
        }

        return StringUtils.isNotBlank(corporateName) &&
                (ObjectUtils.isNotEmpty(dairyId) && dairyId > 0) &&
                StringUtils.isNotBlank(dairyLogoPath);
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

    private static boolean setFrequency() {
        frequency = null;

        if (StringUtils.isNotBlank(commandLine.getOptionValue("frequency")))
            frequency = Frequency.valueOf(commandLine.getOptionValue("frequency").toUpperCase(Locale.ROOT));

        return ObjectUtils.isNotEmpty(frequency);
    }

    public Frequency getFrequency() {
        return frequency;
    }

    private static boolean setBillingDate() {
        billingDate = null;

        if (StringUtils.isNotBlank(commandLine.getOptionValue("date")))
            billingDate = LocalDate.parse(commandLine.getOptionValue("date"), DateTimeFormatter.ofPattern("MMddyy"));

        return ObjectUtils.isNotEmpty(billingDate);
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    static boolean setEmailServer() throws IOException {
        emailServer = "";

        try (InputStream inputStream = Files.newInputStream(Paths.get(APPLICATION_PROPERTIES))) {
            final Properties properties = new Properties();

            properties.load(inputStream);

            emailServer = properties.getProperty("emailServer");
        }

        return StringUtils.isNotBlank(emailServer);
    }

    public String getEmailServer() {
        return emailServer;
    }

    private static boolean setEmailCarbonCopy() throws SQLException {
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
        }

        return ObjectUtils.isNotEmpty(emailCarbonCopy);
    }

    public List<String> emailCarbonCopy() {
        return emailCarbonCopy;
    }

    public String emailOutBox() {
        return "mail/" + library + "/" + BILLING_TYPE + "/" + frequency.type + "/";
    }

    public String emailSentBox() {
        return "mail/" + library + "/" + BILLING_TYPE + "/" + frequency.type + "/sent/";
    }
}
