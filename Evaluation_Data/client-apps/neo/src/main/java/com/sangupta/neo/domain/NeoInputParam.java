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
 * An input parameter that will be requested from the user after
 * the template is read, and generation process is initiated. User
 * parameters define how the project will be setup.
 * 
 * @author sangupta
 *
 */
public class NeoInputParam {
    
    public String name;
    
    public String type = "string";
    
    public String prompt;
    
    public String value = "";
    
    public boolean required = false;
    
    public String ifTrue;
    
    public String ifFalse;

    @Override
    public String toString() {
        return this.name + " (" + this.type + ")";
    }
}