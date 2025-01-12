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
package eu.sonata.nfv.nec.convert.internal;

import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import java.io.*;

/**
 * The include constructor registers a new YAML tag/key/constructor
 * that allows referencing to other YAML files within a given YAML
 * file that are then included on the fly. This allows splitting
 * content in different files.
 *
 * @author Michael Bredel
 */
public class IncludeConstructor extends Constructor {
    /** The YAML tag that identifies other YAML files to include. */
    public static final String INCLUDE_TAG = "!include";

    /** The logger. */
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(IncludeConstructor.class);

    /**
     * Default constructor.
     */
    public IncludeConstructor() {
        this.yamlConstructors.put(new Tag(INCLUDE_TAG), new ConstructInclude());
    }

    /**
     * Constructor that also sets a base path to include the files from.
     *
     * @param path The base path where the include files are located.
     */
    public IncludeConstructor(String path) {
        this.yamlConstructors.put(new Tag(INCLUDE_TAG), new ConstructInclude(path));
    }

    /**
     * The construct include class handles the '!include' YAML tag/key/construct
     * and reads the next
     */
    private class ConstructInclude extends AbstractConstruct {
        /** The base path to include files from. */
        public String basePath;

        /**
         * Default constructor that sets the base path to
         * the current directory, i.e. './'.
         */
        public ConstructInclude() {
            this.basePath = "./";
        }

        /**
         * Constructor that sets the base path.
         *
         * @param path The base path to include files from.
         */
        public ConstructInclude(String path) {
            if (path == null) {
                this.basePath = "./";
            } else if (!path.endsWith("/")) {
                this.basePath = path + "/";
            } else {
                this.basePath = path;
            }
        }

        @Override
        public Object construct(Node node) {
            if (!(node instanceof ScalarNode)) {
                throw new IllegalArgumentException("Non-scalar " + INCLUDE_TAG + ": " + node.toString());
            }

            ScalarNode scalarNode = (ScalarNode) node;
            String value = scalarNode.getValue();

            // Remove a trailing '/' to create the full path, and to avoid path traversal attacks.
            if (value.startsWith("/")) {
                value = this.basePath + value.substring(1);
            } else {
                value = this.basePath + value;
            }

            File file = new File(value);
            if (!file.exists()) {
                if (Logger.isWarnEnabled()) {
                    Logger.warn("The file " + value + " does not exist.");
                }
                return INCLUDE_TAG + " \"" + scalarNode.getValue() + "\"";
            }

            try {
                final InputStream input = new FileInputStream(file);
                final Yaml yaml = new Yaml(new IncludeConstructor());
                return yaml.load(input);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}
