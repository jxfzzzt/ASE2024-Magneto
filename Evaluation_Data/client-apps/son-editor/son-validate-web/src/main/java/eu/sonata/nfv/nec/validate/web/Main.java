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
package eu.sonata.nfv.nec.validate.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import eu.sonata.nfv.nec.convert.ConversionService;
import eu.sonata.nfv.nec.convert.modules.ConversionModule;
import eu.sonata.nfv.nec.validate.ValidationService;
import eu.sonata.nfv.nec.validate.modules.ValidationModule;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import spark.Response;
import spark.Spark;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static spark.Spark.post;

/**
 * A web service implementation to validate JSON/YAML files against a given schema.
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
    public static final String HELP_MSG = "son-validate-web [OPTIONS]";
    /** The default port, the server is listening to. */
    public static final int DEFAULT_PORT = 4567;
    /** The path to the default schemas on the file system. */
    public static final String JSON_DRAFT_PATH = "./src/resources/";
    /** The file name to the default JSON draft 0.4 schema. */
    public static final String JSON_DRAFT_04 = "json-schema-draft-04.json";
    /** The key in a file that points to the corresponding schema. */
    public static final String DEFAULT_SCHEMA_KEY = "$schema";

    /** The logger. */
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(Main.class);

    /** The validation service. */
    private static ValidationService validator;
    /** The conversion service. */
    private static ConversionService converter;
    /** The actual port the server listens to. */
    private static int port = DEFAULT_PORT;
    /** The key that is used to locate schema URI's within a given JSON/YAML file to check. */
    private static String schemaKey;
    /** Should we check the $schema key in the JSON file? */
    private static boolean checkKey = false;
    /** Should the given schema file be checked against the schema draft? */
    private static boolean checkSchema = true;

    /**
     * Go, go, go, ... move it!
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Parse the command line.
        parseCliOptions(args);

        // Initialize the Guice modules.
        initGuice();

        // Set the web-server port
        Spark.port(port);

        // Start the validator web-server.
        post("/validate", "application/json", (request, response) -> {
            // Check content type.
            if (!request.contentType().equalsIgnoreCase("application/json")) {
                response.status(HTTP_BAD_REQUEST);
                response.type("application/json");
                return "{ \"info\": \"Content Type has to be 'application/json'\" }";
            }
            // Validate and return.
            return validate(request.body(), response);
        });
    }

    private static String validate(String dataJsonString, Response response) throws JSONException {
        // Create a JSONObject.
        JSONObject jsonObject;
        String schemaContent = null;
        String fileContent = null;

        // Create the JSON object from the request body.
        try {
            jsonObject = new JSONObject(dataJsonString);
        } catch (JSONException e) {
            response.status(HTTP_BAD_REQUEST);
            response.type("application/json");
            return "{ \"info\": \"Could not parse the given file.\", \"error_message\": \"" + e.getMessage() + "\" }";
        }

        // Extract schema, the options, and the file to validate.
        if (jsonObject.has("schema"))
            try {
                schemaContent = jsonObject.get("schema").toString();
            } catch (Exception e) {
                Logger.info("Could not parse the schema file. Using default instead.", e);
            }
        if (jsonObject.has("file"))
            try {
                fileContent = jsonObject.get("file").toString();
            } catch (Exception e) {
                response.status(HTTP_BAD_REQUEST);
                response.type("application/json");
                return "{ \"info\": \"Could not parse the given file.\", \"error_message\": \"" + e.getMessage() + "\" }";
            }
        if (jsonObject.has("options")) {
            try {
                checkSchema = jsonObject.getJSONObject("options").getBoolean("skip-schema-check");
            } catch (Exception e) {
                Logger.warn("Could not parse 'options.skip-schema-check'. Using default instead.");
            }
            try {
                schemaKey = jsonObject.getJSONObject("options").getString("schema-key");
            } catch (Exception e) {
                Logger.warn("Could not parse 'options.schema-key'. Using default instead.");
            }
            try {
                checkKey = jsonObject.getJSONObject("options").getBoolean("skip-schema-key");
            } catch (Exception e) {
                Logger.warn("Could not parse 'options.skip-schema-key'. Using default instead.");
            }
        }

        // Read the JSON schema specifications.
        String jsonSchemaDraft04 = null;
        try {
            jsonSchemaDraft04 = loadJsonSchema(JSON_DRAFT_04);
        } catch (IOException e) {
            Logger.error("Could not load the default schema." + e.getMessage());
        }

        // Check the default schema.
        if (checkSchema && jsonSchemaDraft04 == null) {
            response.status(HTTP_INTERNAL_ERROR);
            response.type("application/json");
            return "{ \"info\": \"Internal Server Error. The default schema is null.\" }";
        }

        // If no schema is given in the request, try to infer the schema form the schema key.
        if (schemaContent == null && checkKey) {
            JSONObject jSONObject = new JSONObject(fileContent);
            // If there was an schema key provided explicitly, check ...
            if (!schemaKey.equals(DEFAULT_SCHEMA_KEY) && !jSONObject.has(schemaKey)) {
                response.status(HTTP_BAD_REQUEST);
                response.type("application/json");
                return "{ \"info\": \"The file does not contain the given schema key: " + schemaKey + "\" }";
            }
            String schemaUri = jSONObject.get(schemaKey).toString();
            schemaContent = loadAndSanitizeFile(schemaUri);
            if (schemaContent == null) {
                response.status(HTTP_INTERNAL_ERROR);
                response.type("application/json");
                return "{ \"info\": \"Could not retrieve schema from : " + schemaUri + ".\" }";
            }
        }

        // Check the schema against the default schema.
        if (checkSchema && schemaContent != null && !validator.isValid(schemaContent, jsonSchemaDraft04)) {
            response.status(HTTP_BAD_REQUEST);
            response.type("application/json");
            return "{ \"info\": \"The given schema is not a valid schema file.\" }";
        }

        // Check the file against the schema.
        if (schemaContent == null) {
            if (!validator.isValid(fileContent, jsonSchemaDraft04)) {
                response.status(HTTP_BAD_REQUEST);
                response.type("application/json");
                return "{ \"info\": \"The file does NOT meet the default schema.\" }";
            }
        } else {
            if (!validator.isValid(fileContent, schemaContent)) {
                response.status(HTTP_BAD_REQUEST);
                response.type("application/json");
                return "{ \"info\": \"The file does NOT meet the given schema.\" }";
            }
        }

        // Everything is fine.
        response.status(HTTP_OK);
        response.type("application/json");
        return "{ \"info\": \"The file is valid according to the given schema.\" }";
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
                .desc("Give this help list")
                .build();
        // The prt option.
        Option port = Option.builder("p")
                .longOpt("port")
                .desc("The port the server listens to.")
                .hasArg()
                .argName("PORT")
                .build();

        // Create options.
        Options options = new Options();
        options.addOption(help);
        options.addOption(port);

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
            if (line.hasOption("p")) {
                port = Integer.parseInt(line.getOptionValue('p'));
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
           Logger.error("Could not parse the file content of: " + uri);
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
     * Bootstrap the Guice modules.
     */
    private static void initGuice() {
        Injector guiceInjector = Guice.createInjector(new ValidationModule(), new ConversionModule());
        validator = guiceInjector.getInstance(ValidationService.class);
        converter = guiceInjector.getInstance(ConversionService.class);
    }
}
