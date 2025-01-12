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

public class ProjectTemplate {

    /**
     * The provider for this template
     */
    public final TemplateProvider provider;
    
    /**
     * The user who owns the repository
     */
    public final String user;

    /**
     * The repository where the template is stored
     */
    public final String repository;

    /**
     * The sub-path in the repository where template resides
     */
    public final String path;
    
    public ProjectTemplate(TemplateProvider provider, String user, String repository, String path) {
        this.provider = provider;
        this.user = user;
        this.repository = repository;
        this.path = path;
    }
    
    @Override
    public String toString() {
        if(this.user != null) {
            return this.provider.toString().toLowerCase() + ":" + this.user + "/" + this.repository;
        }
        
        return this.provider.toString().toLowerCase() + ":/" + this.repository;
    }
}