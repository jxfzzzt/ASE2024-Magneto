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

import java.util.Collection;

import com.sangupta.jerry.print.ConsoleTable;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.neo.cache.CacheManager;
import com.sangupta.neo.cache.CachedTemplate;

import io.airlift.airline.Command;

@Command(name = "list", description = "List all the templates that exist within the cache")
public class ListTemplates implements Runnable {

    @Override
    public void run() {
        Collection<CachedTemplate> templates = CacheManager.getTemplatesInCache();
        if(AssertUtils.isEmpty(templates)) {
            System.out.println("No templates are currently installed in the cache.");
            return;
        }
        
        ConsoleTable table = new ConsoleTable();
        table.addHeaderRow("Name", "User", "Version");
        for(CachedTemplate template : templates) {
            table.addRow(template.name, template.user, template.version);
        }
        
        table.write(System.out);
    }

}