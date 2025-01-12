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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.FileUtils;
import com.sangupta.jerry.util.GsonUtils;
import com.sangupta.neo.domain.NeoTemplate;

/**
 * Service that manages the template cache on the user's machine. Templates are
 * usually downloaded from the internet and kept in local machine's cache so they
 * can be reused.
 * 
 * @author sangupta
 *
 */
public class CacheManager {
    
    /**
     * Our cache directory where all templates are stored
     */
    private static final File CACHE_DIR;
    
    /**
     * Directory inside cache store where all templates are stored
     */
    private static final File TEMPLATE_CACHE_DIR;
    
    /**
     * Initialize the directories
     */
    static {
        File file = new File(FileUtils.getUsersHomeDirectory(), ".neo");
        if(!file.exists()) {
            file.mkdirs();
        }
        
        if(!file.isDirectory()) {
            file.mkdirs();
        }
        
        CACHE_DIR = file;
        
        TEMPLATE_CACHE_DIR = new File(CACHE_DIR, "templates");
        TEMPLATE_CACHE_DIR.mkdirs();
    }
    
    /**
     * Return the size of the cache.
     * 
     * @return
     */
    public long size() {
        return 0;
    }
    
    /**
     * Check if the template exists with the given name in our local
     * cache or not.
     * 
     * @param name
     * @return
     */
    public boolean exists(String name) {
        if(AssertUtils.isEmpty(name)) {
            return false;
        }
        
        File repo = new File(TEMPLATE_CACHE_DIR, name);
        if(repo.exists() && repo.isDirectory()) {
            return true;
        }
        
        return false;
    }

    /**
     * Install a new template in the cache.
     * 
     * @param templateDir
     * @return
     * @throws IOException
     */
    public static boolean installTemplate(File templateDir) throws IOException {
        File neo = new File(templateDir, "neo.json");
        if(!neo.exists()) {
            System.out.println("No template found in folder (no neo.json found): " + templateDir.getAbsolutePath());
            return false;
        }
        
        if(!neo.isFile()) {
            System.out.println("No template found in folder (no neo.json found): " + templateDir.getAbsolutePath());
            return false;
        }
        
        String contents = org.apache.commons.io.FileUtils.readFileToString(neo);
        if(AssertUtils.isEmpty(contents)) {
            System.out.println("Template configuration neo.json is empty in folder: " + templateDir.getAbsolutePath());
            return false;
        }
        
        NeoTemplate template = GsonUtils.getGson().fromJson(contents, NeoTemplate.class);
        template.validate();
        
        // start installing the template
        File repo = new File(TEMPLATE_CACHE_DIR, template.name);
        repo.mkdirs();
        
        // copy the entire folder contents to this repo folder
        org.apache.commons.io.FileUtils.copyDirectory(templateDir, repo);
        return true;
    }

    public static File getTemplate(ProjectTemplate projectTemplate) {
        File[] files = TEMPLATE_CACHE_DIR.listFiles();
        
        for(File file : files) {
            if(file.isDirectory() && file.getName().equals(projectTemplate.repository)) {
                return file;
            }
        }
        
        return null;
    }
    
    public static List<CachedTemplate> getTemplatesInCache() {
        List<CachedTemplate> templates = new ArrayList<>();
        
        File[] files = TEMPLATE_CACHE_DIR.listFiles();
        
        for(File file : files) {
            if(file.isDirectory()) {
                templates.add(new CachedTemplate(file.getName()));
            }
        }
        
        return templates;
    }

    public static boolean removeTemplate(String templateName) {
        File repo = new File(TEMPLATE_CACHE_DIR, templateName);
        if(!repo.exists()) {
            System.out.println("Template does not exists: " + templateName);
            return false;
        }

        if(!repo.isDirectory()) {
            System.out.println("Template name is invalid: " + templateName);
            return false;
        }
        
        
        return org.apache.commons.io.FileUtils.deleteQuietly(repo);
    }

}
