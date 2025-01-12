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

package com.sangupta.neo.domain;

/**
 * A process step that determines the actions we need to perform
 * other than just copying and processing the <code>data</code> folder.
 * 
 * For many of the projects it may be OK to have just <code>data</code>
 * folder processed, but many a times, projects will need to perform
 * many crucial steps based on user input, such as creating specific
 * files and folders as needed.
 * 
 * {@link NeoProcess} is essentially meant to execute such customized
 * process steps.
 * 
 * @author sangupta
 *
 */
public class NeoProcess {

    /**
     * The message to display before executing all commands in this process step
     */
    public String message;
    
    /**
     * List of commands to execute as part of this process step
     */
    public String[] commands;
    
    /**
     * The condition under which to bound all these steps
     */
    public String condition;
    
}