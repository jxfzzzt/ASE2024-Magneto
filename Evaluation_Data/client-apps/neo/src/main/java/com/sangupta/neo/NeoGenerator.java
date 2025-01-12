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

package com.sangupta.neo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.GsonUtils;
import com.sangupta.jerry.util.StringUtils;
import com.sangupta.neo.domain.NeoProcess;
import com.sangupta.neo.domain.NeoTemplate;

/**
 * {@link NeoGenerator} takes care of reading the project template and
 * creation of project artifacts as per the user's choices.
 * 
 * @author sangupta
 *
 */
public class NeoGenerator {
    
    /**
     * My private logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NeoGenerator.class);
    
    /**
     * Singleton instance of {@link NeoGenerator}
     */
    private static NeoGenerator INSTANCE = null;
    
    /**
     * The folder where the template is contained - all files for templates
     * are resolved from this folder as base.
     */
    private final File templateDir;
    
    /**
     * The folder where the project is to be created
     */
    private final File projectFolder;
    
    /**
     * The {@link NeoTemplate} data as read and needs to be processed 
     */
    private final NeoTemplate template;

    /**
     * The properties which will be used when processing the template
     */
    private final Map<String, Object> properties = new HashMap<>();
    
    /**
     * Get the singleton instance of {@link NeoGenerator}.
     * 
     * @return
     */
    public static NeoGenerator getInstance() {
        return INSTANCE;
    }
    
    /**
     * Create a singleton instance of {@link NeoGenerator} - if the instance is
     * already created that will be returned, else the first instance is created
     * and returned.
     * 
     * @param templateDir
     * @param projectFolder
     * @return
     */
    public static synchronized NeoGenerator createInstance(File templateDir, File projectFolder) {
        if(INSTANCE != null) {
            return INSTANCE;
        }
        
        INSTANCE = new NeoGenerator(templateDir, projectFolder);
        return INSTANCE;
    }
    
    /**
     * Constructor
     * 
     * @param templateDir
     *            the folder from where the project template has to be read.
     *            Eventually we will be able to pass the url to a repository
     *            like github or someplace so that we can read templates
     *            directly from there.
     */
    private NeoGenerator(File templateDir, File projectFolder) {
        this.templateDir = templateDir;
        this.projectFolder = projectFolder;
        this.template = readNeoProjectTemplate();
    }

    /**
     * Read the project template from the template folder. Templates
     * are usually named <code>neo.json</code>
     * 
     */
    private NeoTemplate readNeoProjectTemplate() {
        String json = this.getResourceContents("neo.json");
        if(AssertUtils.isEmpty(json)) {
            throw new NeoRuntimeException("Cannot read template contents from neo.json");
        }
        
        NeoTemplate template = GsonUtils.getGson().fromJson(json, NeoTemplate.class);
        if(template == null) {
            throw new NeoRuntimeException("Unable to parse template contents from neo.json");
        }
        
        // run integrity check on template
        template.validate();
        
        // return the template
        return template;
    }

    /**
     * Generate the project in the specified folder.
     * 
     * @param projectFolder
     */
    public void generate() {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Properties from user input are as under:");
            for(Entry<String, Object> entry : this.properties.entrySet()) {
                LOGGER.debug(entry.getKey() + ": " + entry.getValue());
            }
        }
        
        projectFolder.mkdirs();
        
        // start copying each file from data directory to the project folder
        File dataFolder = this.getResource("data");
        if(dataFolder != null && dataFolder.exists() && dataFolder.isDirectory()) {
            try {
                LOGGER.info("Processing data folder");
                processDataFolder(dataFolder, projectFolder);
            } catch (IOException e) {
                throw new NeoRuntimeException("Unable to copy file from data folder to project folder", e);
            }
        } else {
            LOGGER.info("Skipping data folder as it is non-existent");
        }
        
        runProcesses();
    }

    /**
     * Run all processes that are mentioned in the {@link NeoTemplate}.
     * 
     */
    private void runProcesses() {
        if(AssertUtils.isEmpty(this.template.process)) {
            return;
        }
        
        // run each command as specified in the process section
        for(NeoProcess process : this.template.process) {
            // first preference is of commands
            runProcessCommand(process);
        }
    }

    /**
     * Run one set of commands as specified in the {@link NeoProcess}.
     * 
     * @param process
     */
    private void runProcessCommand(NeoProcess process) {
        if(AssertUtils.isEmpty(process.commands)) {
            return;
        }
        
        if(AssertUtils.isEmpty(process.condition)) {
            // if there is no condition we need not build the script
            if(AssertUtils.isNotEmpty(process.message)) {
                String message = VelocityUtils.processWithVelocity(process.message);
                System.out.println(message);
            }

            // just run each command without worry
            for(String command : process.commands) {
                if(AssertUtils.isEmpty(command)) {
                    continue;
                }

                VelocityUtils.processWithVelocity(command);
            }
            
            return;
        }
        
        StringBuilder script = new StringBuilder();
        script.append("#if (" + process.condition + ")");
        script.append(StringUtils.SYSTEM_NEW_LINE);
        script.append("#prompt (\"" + process.message + "\")");
        script.append(StringUtils.SYSTEM_NEW_LINE);
        for(String command : process.commands) {
            if(AssertUtils.isEmpty(command)) {
                continue;
            }
            script.append(command);
            script.append(StringUtils.SYSTEM_NEW_LINE);
        }
        script.append("#end");
        script.append(StringUtils.SYSTEM_NEW_LINE);
        
        // execute the script
        String contents = script.toString();
        VelocityUtils.processWithVelocity(contents);
    }
    
    /**
     * Copy all files from data folder to the project folder.
     * 
     * @param dataFolder
     *            the <code>data</code> folder where all data for the template
     *            is contained.
     * 
     * @param projectFolder
     *            the folder where the project needs to be created
     * 
     * @throws IOException
     *             if something goes wrong
     */
    private void processDataFolder(File dataFolder, File projectFolder) throws IOException {
        Collection<File> files = FileUtils.listFilesAndDirs(dataFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        
        System.out.println("Processing the data folder ...");
        if(AssertUtils.isEmpty(files)) {
            // nothing to do in data folder
            return;
        }
        
        // let's process each file
        for(File file : files) {
            LOGGER.info("Processing file: {}", file.getAbsolutePath());
            String filePath = getProjectFolderPath(dataFolder, file);
            
            // process the filename itself with velocity as it may contain variables
            if(filePath.contains("$")) {
                filePath = org.apache.commons.lang.StringUtils.replace(filePath, "\\", "/");
                filePath = VelocityUtils.processWithVelocity(filePath);
            }

            // go ahead and create the file
            if(file.isDirectory()) {
                // create a corresponding dir in project folder
                File destDir = new File(projectFolder, filePath);
                destDir.mkdirs();
                continue;
            }
            
            File projectFile = new File(projectFolder, filePath);
            if(file.length() == 0) {
                // create an empty 
                FileUtils.writeStringToFile(projectFile, "");
                continue;
            }
            
            // the file has contents
            String contents = FileUtils.readFileToString(file);
            
            // update contents via velocity
            contents = VelocityUtils.processWithVelocity(contents);
            
            // write back to project folder
            FileUtils.writeStringToFile(projectFile, contents);
        }
    }

    private String getProjectFolderPath(File dataFolder, File file) {
        String basePath = dataFolder.getAbsolutePath();
        String path = file.getAbsolutePath();
        
        if(!path.startsWith(basePath)) {
            return file.getName(); 
        }
        
        path = path.substring(basePath.length());
        if(path.startsWith("/") || path.startsWith("\\")) {
            return path.substring(1);
        }
        
        return path;
    }

    /**
     * Read a resource from the template dir/url for the given relative
     * path.
     * 
     * @param relativePath
     */
    public File getResource(String relativePath) {
        File file = new File(this.templateDir, relativePath);
        if(file.exists() && file.canRead()) {
            return file;
        }
        
        return null;
    }
    
    public String getResourceContents(String relativePath) {
        File file = this.getResource(relativePath);
        if(file == null) {
            return null;
        }
        
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new NeoRuntimeException("Unable to read resource " + relativePath + " from repository", e);
        }
    }

    /**
     * Initialize this generator by reading and processing all contents
     * from the {@link NeoTemplate} that has been read as part of the
     * construction of this object.
     * 
     */
    public void initialize() {
        new NeoInputManager(this.properties).getUserInput(this.template.params);
    }
    
    // Usual accessors follow

    public File getProjectFolder() {
        return projectFolder;
    }

    /**
     * Add a new property to the list of {@link #properties}.
     * 
     * @param property
     *            the name of the property
     * 
     * @param value
     *            the value for the property
     */
    public void addProperty(String property, String value) {
        this.properties.put(property, value);
    }
    
    /**
     * Remove the property from the list of {@link #properties}.
     * 
     * @param property
     *            the name of the property to be removed
     */
    public void removeProperty(String property) {
        this.properties.remove(property);
    }

    /**
     * Return the list of all the {@link #properties} as held by this
     * instance of {@link NeoGenerator}.
     * 
     * @return
     */
    public Map<String, Object> getProperties() {
        return this.properties;
    }
    
}