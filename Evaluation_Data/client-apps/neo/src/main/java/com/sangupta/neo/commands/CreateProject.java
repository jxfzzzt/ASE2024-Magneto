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

package com.sangupta.neo.commands;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.neo.NeoGenerator;
import com.sangupta.neo.cache.CacheManager;
import com.sangupta.neo.cache.CacheUtils;
import com.sangupta.neo.cache.ProjectTemplate;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.Option;

/**
 * Allows creation of a new project using the given template name.
 * 
 * @author sangupta
 *
 */
@Command(name = "create", description = "Create a new project")
public class CreateProject implements Runnable {
    
    @Option(name = { "-t", "--template"}, description = "Template to be used for project creation", required = true)
    private String templateName;
    
    @Arguments(description = "the folder where new project will be created", required = true, title = "folder")
    private List<String> folder;

    @Override
    public void run() {
        if(folder.size() > 1) {
            System.out.println("A project can be created only in one folder at a time.");
            return;
        }
        
        ProjectTemplate projectTemplate = CacheUtils.resovleTemplatePath(templateName);
        
        execute(projectTemplate, folder.get(0));
    }

    private static void execute(ProjectTemplate projectTemplate, String projectDir) {
        if (projectTemplate == null) {
            System.out.println("Project template is required.");
            return;
        }
        
        File templateFolder = CacheManager.getTemplate(projectTemplate);
        if (templateFolder == null) {
            System.out.println("Project template is required.");
            return;
        }

        if (AssertUtils.isEmpty(projectDir)) {
            System.out.println("Project folder is required.");
            return;
        }

        // TODO: remove this - instead throw an error
        FileUtils.deleteQuietly(new File(projectDir));

        // start generation
        long start = 0;
        try {
            NeoGenerator generator = NeoGenerator.createInstance(templateFolder, new File(projectDir));
            generator.initialize();

            start = System.currentTimeMillis();
            generator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            if (start > 0) {
                System.out.println("\n\nGeneration complete in " + (end - start) + " milliseconds.");
            }
        }

    }

    public List<String> getFolder() {
        return folder;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setFolder(List<String> folder) {
        this.folder = folder;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}