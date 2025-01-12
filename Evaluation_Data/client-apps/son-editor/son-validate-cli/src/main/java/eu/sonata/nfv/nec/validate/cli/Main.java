/*
 * Copyright (C) 2015, SONATA-NFV, NEC
 * ALL RIGHTS RESERVED
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 * Neither the name of the SONATA-NFV and NEC nor the names of 
 * its contributors may be used to endorse or promote products 
 * derived from this software without specific prior written 
 * permission.
 * 
 * This work has been performed in the framework of the SONATA project,
 * funded by the European Commission under Grant number 671517 through 
 * the Horizon 2020 and 5G-PPP programmes. The authors would like to 
 * acknowledge the contributions of their colleagues of the SONATA 
 * partner consortium (www.sonata-nfv.eu).
 */
package eu.sonata.nfv.nec.validate.cli;

import ch.qos.logback.classic.Level;
import com.google.inject.Guice;
import com.google.inject.Injector;
import eu.sonata.nfv.nec.convert.ConversionService;
import eu.sonata.nfv.nec.validate.ValidationService;
import eu.sonata.nfv.nec.convert.modules.ConversionModule;
import eu.sonata.nfv.nec.validate.modules.ValidationModule;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A CLI implementation to validate JSON/YAML files against a given schema.
 *
 * The official JSON schema files to test for valid JSON can be
 * found at: https://github.com/json-schema/json-schema
 *
 * @author Michael Bredel
 */
public class Main {
    /** The exit code if the procedure succeeded. */
    public static final int EXIT_CODE_SUCCESS = 0;
    /** The exit code of the procedure failed. */
    public static final int EXIT_CODE_ERROR = 1;
    /** The help message. */
    public static final String HELP_MSG = "son-validate [OPTIONS] FILE";
    /** The path to the default schemas on the file system. */
    public static final String JSON_DRAFT_PATH = "./src/resources/";
    /** The file name to the default JSON draft 0.4 schema. */
    public static final String JSON_DRAFT_04 = "json-schema-draft-04.json";
    /** The key in a file that points to the corresponding schema. */
    public static final String DEFAULT_SCHEMA_KEY = "$schema";
    /** The ANSI code for the default color. */
    public static final String COLOR_DEFAULT = "\033[39m";
    /** The ANSI code for red. */
    public static final String COLOR_RED = "\033[31m";
    /** The ANSI code for green. */
    public static final String COLOR_GREEN = "\033[32m";
    /** The logger. */
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(Main.class);

    /** The validation service. */
    private static ValidationService validator;
    /** The conversion service. */
    private static ConversionService converter;
    /** The path to the schema file. */
    private static String schemaFile;
    /** The path to the JSON/YAML file to verify or convert. */
    private static String jsonFile;
    /** The key that is used to locate schema URI's within a given JSON/YAML file to check. */
    private static String schemaKey;
    /** Should the given schema file be checked against the schema draft? */
    private static boolean checkSchema = true;
    /** Should we have colored output to the CLI? */
    private static boolean coloredOutput = false;

    /**
     * Go, go, go, ... move it!
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Set the log level to ERROR.
        setLogLevel(1);

        // Parse the command line.
        parseCliOptions(args);

        // Initialize the Guice modules.
        initGuice();

        // Read the JSON schema specifications.
        String jsonSchemaDraft04 = null;
        try {
            jsonSchemaDraft04 = loadJsonSchema(JSON_DRAFT_04);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Read the actual file.
        String fileContent = loadAndSanitizeFile(jsonFile);
        if (fileContent == null) {
            System.err.println(getFAILURE() + " Could not read file from path: " + jsonFile);
            System.exit(EXIT_CODE_ERROR);
        }

        String schemaContent;
        // If a schema file was provided, read the schema file. With a lot of error handling.
        if (schemaFile != null) {
            schemaContent = loadAndSanitizeFile(schemaFile);
            if (schemaContent == null) {
                System.err.println(getFAILURE() + " Could not read schema file from path: " + schemaFile);
                System.exit(EXIT_CODE_ERROR);
            }
            if (checkSchema && jsonSchemaDraft04 == null) {
                System.err.println(getFAILURE() + " The default schema file '" + JSON_DRAFT_04 + "' is null.");
                System.exit(EXIT_CODE_ERROR);
            }
            if (checkSchema && !validator.isValid(schemaContent, jsonSchemaDraft04)) {
                System.err.println(getFAILURE() + " The schema file is no valid JSON.");
                System.exit(EXIT_CODE_ERROR);
            }
        // If no schema was provided, but the 's' option was used, just use the default schema.
        // We re-use the checkSchema here to state whether to check the default or not.
        } else if (!checkSchema) {
            if (jsonSchemaDraft04 == null) {
                System.err.println(getFAILURE() + " The default schema file '" + JSON_DRAFT_04 + "' is null.");
                System.exit(EXIT_CODE_ERROR);
            }
            schemaContent = jsonSchemaDraft04;
        // Try to get the schema file from a $schema URL in the json file.
        } else {
            JSONObject jSONObject = new JSONObject(fileContent);
            // If there was an schema key provided explicitly, check ...
            if (!schemaKey.equals(DEFAULT_SCHEMA_KEY) && !jSONObject.has(schemaKey)) {
                System.err.println(getFAILURE() + " The file does not contain the given schema key: " + schemaKey);
                System.exit(EXIT_CODE_ERROR);
            }
            // We re-use schemaFile here to store the URI of the schema.
            schemaFile = jSONObject.get(schemaKey).toString();
            schemaContent = loadAndSanitizeFile(schemaFile);
            if (schemaContent == null) {
                System.err.println(getFAILURE() + " Could not read schema from path: " + schemaFile);
                System.exit(EXIT_CODE_ERROR);
            }
        }

        // Check and output.
        if (validator.isValid(fileContent, schemaContent)) {
            if (schemaFile != null) {
                System.out.println(getSUCCESS() + " The file '" + jsonFile + "' is valid according to the given schema '" + schemaFile + "'.");
            } else {
                System.out.println(getSUCCESS() + " The file '" + jsonFile + "' is valid according to the default schema '" + JSON_DRAFT_04 + "'.");
            }
            System.exit(EXIT_CODE_SUCCESS);
        } else {
            if (schemaFile != null) {
                System.out.println(getFAILURE() + " The file '" + jsonFile + "' does NOT meet the schema '" + schemaFile + "'.");
            } else {
                System.out.println(getFAILURE() + " The file '" + jsonFile + "' does NOT meet the schema '" + JSON_DRAFT_04 + "'.");
            }
            System.exit(EXIT_CODE_ERROR);
        }
    }

    /**
     * Creates the command line options for the
     * program.
     *
     * @return An Options object containing all the command line options of the program.
     */
    private static Options createCliOptions() {
        // A helper option
        Option help = Option.builder("h")
                .longOpt("help")
                .desc("Give this help list.")
                .build();
        // The schema option to provide the JSON/YAML schema file.
        Option schema = Option.builder("s")
                .longOpt("schema")
                .desc("A schema file used to check FILE.")
                .hasArg()
                .argName("SCHEMA_FILE")
                .optionalArg(true)
                .build();
        // The schema option to provide the JSON/YAML schema file.
        Option verbose = Option.builder("v")
                .longOpt("verbose")
                .desc("Increase the verbose level.")
                .build();
        // The schema option to provide the JSON/YAML schema file.
        Option skipSchemaCheck = Option.builder("d")
                .longOpt("skip-schema-check")
                .desc("Skip the schema check. Default is 'false'.")
                .build();
        // The schema option to provide the JSON/YAML schema file.
        Option schemaKey = Option.builder("k")
                .longOpt("schema-key")
                .desc("The key that is used to identify the schema URI in FILE. Default is '$schema'.")
                .hasArg()
                .argName("SCHEMA_KEY")
                .build();
        // The option to have colored output.
        Option coloredOutput = Option.builder("c")
                .longOpt("colored-output")
                .desc("Have colored output, i.e. green for success, and red for errors.")
                .build();

        // Create options.
        Options options = new Options();
        options.addOption(help);
        options.addOption(schema);
        options.addOption(verbose);
        options.addOption(skipSchemaCheck);
        options.addOption(schemaKey);
        options.addOption(coloredOutput);

        // Return options.
        return options;
    }

    /**
     * Parses the command line arguments.
     *
     * @param args The command line arguments.
     */
    private static void parseCliOptions(String[] args) {
        // Command line options.
        Options options = createCliOptions();
        // Command line parser.
        CommandLineParser parser = new DefaultParser();

        try {
            // Parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("h")) {
                printHelp(options);
                System.exit(EXIT_CODE_SUCCESS);
            }
            if (line.hasOption("s")) {
                schemaFile = normalizePath(line.getOptionValue('s'));
                if (schemaFile == null)
                    checkSchema = false;
            }
            if (line.hasOption("v")) {
                setLogLevel(2);
            }
            if (line.hasOption("d")) {
                checkSchema = false;
            }
            if (line.hasOption("c")) {
                coloredOutput = true;
            }
            if (line.hasOption("k")) {
                schemaKey = line.getOptionValue('k');
            } else {
                schemaKey = DEFAULT_SCHEMA_KEY;
            }
            // Get whatever ist left, after the options have been processed.
            if (line.getArgList() == null || line.getArgList().isEmpty()) {
                throw new MissingArgumentException("JSON/YAML file to validate is missing.");
            } else {
                jsonFile = normalizePath(line.getArgList().get(0));
            }
        } catch (MissingOptionException | MissingArgumentException e) {
            System.err.println("ERROR: " + e.getMessage() + "\n");
            printHelp(options);
            System.exit(EXIT_CODE_ERROR);
        } catch (ParseException e) {
            // Oops, something went wrong
            System.err.println("ERROR: Parsing failed. Reason: " + e.getMessage());
        }
    }

    /**
     * Reads a file from the given path with a given charset.
     *
     * @param path The path to the file.
     * @return A string that contains the file content.
     */
    private static String readFile(String path) throws IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }

    /**
     * Reads a file from the given path with a given charset.
     *
     * @param path The path to the file.
     * @param encoding The Charset of the file.
     * @return A string that contains the file content.
     */
    private static String readFile(String path, Charset encoding) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), encoding);
    }

    /**
     * Reads file from a given URL.
     *
     * @param urlString The URL to the file.
     * @return A string that contains the file content.
     */
    private static String loadFileFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        return IOUtils.toString(url);
    }

    /**
     * Reads a file from the given path and parses its content. It tries
     * to convert the file content to a JSON string.
     *
     * @param uri The URI to identify the JSON file.
     * @return The JSON String.
     */
    private static String loadAndSanitizeFile(@Nonnull String uri) {
        String fileContent;
        // This is needed, since there is not http provider for nio.files yet.
        try {
            if (uri.toLowerCase().startsWith("http://") || uri.toLowerCase().startsWith("https://")) {
                fileContent = loadFileFromUrl(uri);
            } else {
                fileContent = readFile(uri);
            }
        } catch (IOException e) {
            return null;
        }

        // Try to convert to JSON (to make sure we have a JSON string).
        String jsonString = null;
        try {
            jsonString = converter.convertToJson(fileContent);
        } catch (Exception e) {
            System.err.println(getFAILURE() + " Could not parse the file content of: " + uri);
            System.exit(EXIT_CODE_ERROR);
        }

        // Return the sanitized JSON string loaded from the file.
        return jsonString;
    }

    /**
     * Reads the default schema file either from the file system (e.g. in
     * case of unit tests) or the executable JAR file.
     *
     * @param schema The name of the schema file.
     * @return A JSON string representing the schema file.
     * @throws IOException If the schema file cannot be read.
     */
    private static String loadJsonSchema(String schema) throws IOException {
        // Try to read from the file system.
        try {
            return readFile(JSON_DRAFT_PATH + schema);
        } catch (IOException e) {
            if (Logger.isDebugEnabled()) {
                Logger.debug("Cannot read draft schema from file system. Is it a JAR file?\n" + e);
            }
        }

        // Try to read from the JAR file.
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream(schema);
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            if (Logger.isDebugEnabled()) {
                Logger.debug("Cannot read draft schema from JAR file.\n" + e);
            }
        }

        throw new IOException("Cannot read the draft file.");
    }

    /**
     * Prints the help of the command.
     *
     * @param options The command's options.
     */
    private static void printHelp(Options options) {
        // A help formatter.
        HelpFormatter formatter = new HelpFormatter();
        // Print help.
        formatter.printHelp(HELP_MSG, options);
    }

    /**
     * Sets the log level.
     *
     * @param level The log level to set.
     */
    private static void setLogLevel(int level) {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        switch (level) {
            case 3:
                rootLogger.setLevel(Level.DEBUG);
                break;
            case 2:
                rootLogger.setLevel(Level.INFO);
                break;
            case 1:
                rootLogger.setLevel(Level.ERROR);
                break;
            default:
                rootLogger.setLevel(Level.OFF);
        }
    }

    /**
     * Bootstrap the Guice modules.
     */
    private static void initGuice() {
        Injector guiceInjector = Guice.createInjector(new ValidationModule(), new ConversionModule());
        validator = guiceInjector.getInstance(ValidationService.class);
        converter = guiceInjector.getInstance(ConversionService.class);
    }

    /**
     * Produces colored outputs for the shell (bash).
     *
     * @param string The String to colorize.
     * @param color The color to use.
     * @return The colorized string.
     */
    private static String getColoredString(String string, String color) {
        if (coloredOutput)
            return color + string + COLOR_DEFAULT;
        else
            return string;
    }

    /**
     * Convenience method to produce a colorized "FAILURE" string.
     * @return The failure string.
     */
    private static String getFAILURE() {
        return getColoredString("[FAILURE]", COLOR_RED);
    }

    /**
     * Convenience method to produce a colorized "SUCCESS" string.
     * @return The success string.
     */
    private static String getSUCCESS() {
        return getColoredString("[SUCCESS]", COLOR_GREEN);
    }

    /**
     * Normalizes a path.
     *
     * @param path The path to normalize.
     * @return A normalized path string.
     */
    private static String normalizePath(String path) {
        if (path != null) {
            path = Paths.get(path).normalize().toString();
            if (!path.startsWith(File.separator))
                path = "." + File.separator + path;
            return path;
        } else {
            return null;
        }
    }
}
