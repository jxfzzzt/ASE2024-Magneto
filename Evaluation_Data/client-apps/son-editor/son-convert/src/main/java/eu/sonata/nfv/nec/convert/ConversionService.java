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
package eu.sonata.nfv.nec.convert;

import java.util.Map;

/**
 * The conversion service converts JSON strings to YAML
 * or vice versa. Moreover it converts strings to a
 * general map representation and allows the comparison
 * of two JSON/YAML strings by its contained information,
 * i.e. regardless of the actual string structure.
 *
 * @author Michael Bredel
 */
public interface ConversionService {
    /**
     * Convert a YAML string to a JSON string.
     *
     * @param yamlString The YAML string to convert.
     * @return A JSON string, or null.
     */
    String convertToJson(String yamlString);

    /**
     * Convert a map of key-value pairs to a JSON string.
     *
     * @param map The map of key-value pairs to convert.
     * @return A JSON string, or null.
     */
    String convertToJson(Map<String, Object> map);

    /**
     * Convert a JSON string to a YAML string.
     *
     * @param jsonString The JSON string to covert.
     * @return A YAML string, or null.
     */
    String convertToYaml(String jsonString);

    /**
     * Convert a map of key-value pairs to a YAML string.
     *
     * @param map The map of key-value pairs to convert.
     * @return A YAML string, or null.
     */
    String convertToYaml(Map<String, Object> map);

    /**
     * Convert a JSON or YAML string to a map.
     *
     * @param string The string to convert.
     * @return A map representation of the key-value pairs, or null.
     */
    Map<String, Object> convertToMap(String string);

    /**
     * Checks if the two strings represent the same information.
     *
     * @param a The first string, either JSON or YAML.
     * @param b The second string, either JSON or YAML.
     * @return True if the two strings represent the same information, false otherwise.
     */
    boolean compare(String a, String b);
}
