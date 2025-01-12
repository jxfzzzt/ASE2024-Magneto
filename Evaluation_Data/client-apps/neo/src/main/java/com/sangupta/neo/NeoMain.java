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

import com.sangupta.jerry.ApplicationContext;
import com.sangupta.neo.commands.CreateProject;
import com.sangupta.neo.commands.DownloadTemplate;
import com.sangupta.neo.commands.InstallTemplate;
import com.sangupta.neo.commands.ListTemplates;
import com.sangupta.neo.commands.RemoveTemplate;

import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Help;
import io.airlift.airline.ParseArgumentsUnexpectedException;

/**
 * Main entry point for the Neo project creation tool. Parses
 * the command line arguments, and executes the tool as needed.
 * 
 * Also helps display the usage information for the tool.
 * 
 * @author sangupta
 *
 */
public class NeoMain {
    
    public static void main(String[] args) {
//        args = new String[] { "download", "github:sangupta/neo-templates/aem-6.2" };
        
        System.out.println("\nNeo Project Scaffolding Tool, Version " + ApplicationContext.PROJECT_VERSION);
        System.out.println("Copyright (c) 2016, Sandeep Gupta.\n");
        @SuppressWarnings("unchecked")
        CliBuilder<Runnable> builder = Cli.<Runnable>builder("neo")
                .withDescription("Project scaffolding tool")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, CreateProject.class, InstallTemplate.class, ListTemplates.class,
                                DownloadTemplate.class, RemoveTemplate.class);
        
        Cli<Runnable> cliParser = builder.build();
        
        try {
            cliParser.parse(args).run();
        } catch(ParseArgumentsUnexpectedException e) {
            System.out.println("Invalid command, use '$ neo help' for usage instructions!");
        }
    }
    
}