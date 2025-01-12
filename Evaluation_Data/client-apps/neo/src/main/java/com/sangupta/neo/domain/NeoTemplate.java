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

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.neo.NeoRuntimeException;

/**
 * Defines the overall structure of a Neo template that is used
 * to create projects.
 * 
 * @author sangupta
 *
 */
public class NeoTemplate {
    
    /**
     * The name of this template
     */
    public String name;
    
    /**
     * The version of this template - should be semantic versioning
     */
    public String version;

    /**
     * The description of this template
     */
    public String description;
    
    /**
     * The URL that points to neo.json file so that we can check if there are updates available or not.
     */
    public String updateUrl;
    
    /**
     * The author information for this template, in case of a single author
     */
    public NeoTemplateAuthor author;
    
    /**
     * The author information for this template, in case of multiple authors
     */
    public NeoTemplateAuthor[] authors;
    
    /**
     * The input parameters that need to be read from user
     */
    public NeoInputParam[] params;
    
    /**
     * Process actions that create the project for us
     */
    public NeoProcess[] process;

    /**
     * Run integrity check for this template.
     * 
     */
    public void validate() {
        if(AssertUtils.isEmpty(name) || AssertUtils.isEmpty(version)) {
            throw new NeoRuntimeException("Template name is required");
        }
        
        for(NeoInputParam param : this.params) {
            if(AssertUtils.isEmpty(param.name)) {
                throw new NeoRuntimeException("Param name is empty in template");
            }
            
            param.name = param.name.trim();
            
            if(AssertUtils.isEmpty(param.type)) {
                param.type = "string";
            } else {
                param.type = param.type.toLowerCase();
            }
            
            // TODO: convert first letter to uppercase of param.description
        }
    }
    
}