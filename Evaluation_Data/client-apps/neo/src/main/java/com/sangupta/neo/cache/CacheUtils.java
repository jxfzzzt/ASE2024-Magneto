/**
 *
 * neo - project scaffolding tool
 * Copyright (c) 2016, Sandeep Gupta
 * 
 * http://sangupta.com/projects/neo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.neo.cache;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.sangupta.jerry.io.AdvancedStringReader;
import com.sangupta.jerry.util.AssertUtils;

public class CacheUtils {

    /**
     * Normalize the template path and name to the one that we understand. For example,
     * 
     * <ul>
     * <li>Path <code>aem</code> resolves to <code>CACHE</code></li>
     * <li>Path <code>c:/templates/aem</code> resolves to <code>LOCAL</code> file system</li>
     * <li>Path <code>/templates/aem</code> resolves to <code>LOCAL</code> file system</li>
     * <li>Path <code>templates/aem</code> resolves to <code>LOCAL</code> file system</li>
     * <li>Path <code>file://c:/templates/aem</code> resolves to <code>LOCAL</code> file system</li>
     * <li>Path <code>file:///c:/templates/aem</code> resolves to <code>LOCAL</code> file system</li>
     * <li>Path <code>github:sangupta/aem</code> resolves to <code>GITHUB</code></li>
     * <li>Path <code>github.com:sangupta/aem</code> resolves to <code>GITHUB</code></li>
     * <li>Path <code>github.com/sangupta/aem</code> resolves to <code>GITHUB</code></li>
     * <li>Path <code>http://github.com/sangupta/aem</code> resolves to <code>GITHUB</code></li>
     * <li>Path <code>https://github.com/sangupta/aem</code> resolves to <code>GITHUB</code></li>
     * <li>Path <code>git@github.com:sangupta/aem</code> resolves to <code>GITHUB</code></li>
     * <li>Path <code>bitbucket:sangupta/aem</code> resolves to <code>BITBUCKET</code></li>
     * </ul>
     * 
     * @param templateName
     * @return
     */
    public static ProjectTemplate resovleTemplatePath(String templateName) {
        if(AssertUtils.isEmpty(templateName)) {
            return null;
        }
        
        // find if we have a separator in it or not
        boolean notFromCache = StringUtils.containsAny(templateName, "/:@");
        
        if(!notFromCache) {
            // resolve this from the cache
            return new ProjectTemplate(TemplateProvider.CACHE, null, templateName, null);
        }
        
        // check if this is from file system
        if(templateName.startsWith("file:")) {
            templateName = templateName.substring(5);
            do {
                if(templateName.startsWith("/")) {
                    templateName = templateName.substring(1);
                    continue;
                }
                
                break;
            } while(true);
        }

        File file = com.sangupta.jerry.util.FileUtils.resolveToFile(templateName);
        if(file.exists()) {
            // this is from file system
            return new ProjectTemplate(TemplateProvider.LOCAL, null, file.getAbsolutePath(), null);
        }
        
        // this is from a provider
        if(templateName.startsWith("http://")) {
            templateName = templateName.substring(7);
        }
        
        if(templateName.startsWith("https://")) {
            templateName = templateName.substring(8);
        }
        
        // remove anything before '@'
        int at = templateName.indexOf('@');
        if(at > 0) {
            templateName = templateName.substring(at + 1);
        }
        
        // convert colon to slash
        templateName = StringUtils.replaceChars(templateName, ':', '/');
        
        // build a reader
        AdvancedStringReader reader = new AdvancedStringReader(templateName);
        
        String provider = reader.readTillNext('/');
        String user = reader.readTillNext('/');
        String repository = reader.readTillNext('/');
        String path = reader.readRemaining();
        
        return new ProjectTemplate(TemplateProvider.from(provider), user, repository, path);
    }

}
