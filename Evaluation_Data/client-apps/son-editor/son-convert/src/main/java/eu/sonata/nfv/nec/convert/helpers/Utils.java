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
package eu.sonata.nfv.nec.convert.helpers;

/**
 * A utils class the just collects some useful
 * functions.
 *
 * @author Michael Bredel
 */
public class Utils {

    /**
     * Strip the beginning of a path string.
     *
     * @param path The path string to strip.
     * @return A stripped path string.
     */
    public static synchronized String stripPath(String path) {
        if (path.startsWith("./"))
            return path.substring(2);
        if (path.startsWith("/"))
            return path.substring(1);
        return path;
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private Utils() {
        // Do nothing.
    }
}
