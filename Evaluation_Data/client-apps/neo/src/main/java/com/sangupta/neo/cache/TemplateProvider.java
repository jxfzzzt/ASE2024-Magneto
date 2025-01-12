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

import com.sangupta.jerry.util.AssertUtils;

public enum TemplateProvider {
    
    LOCAL,
    
    CACHE,
    
    GITHUB,
    
    BITBUCKET,
    
    GITLAB,
    
    UNKNOWN;

    public static TemplateProvider from(String provider) {
        if(AssertUtils.isEmpty(provider)) {
            return null;
        }
        
        provider = provider.toLowerCase();
        
        switch(provider) {
            case "github":
            case "github.com":
                return GITHUB;
                
            case "bitbucket":
            case "bitbucket.org":
                return BITBUCKET;
                
            case "gitlab":
            case "gitlab.com":
                return GITLAB;
            
            default:
                return UNKNOWN;
        }
    }

}