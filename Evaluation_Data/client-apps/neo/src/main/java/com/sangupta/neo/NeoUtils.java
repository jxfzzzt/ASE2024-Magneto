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

import org.zeroturnaround.zip.ZipUtil;

import com.sangupta.jerry.constants.SystemPropertyNames;

public class NeoUtils {

    public static File extractToFolderIfNeeded(File zip) {
        if(zip.isDirectory()) {
            return zip;
        }
        
        if(!zip.getName().endsWith(".zip")) {
            // not a zip file
            return null;
        }
        
        File folder = NeoUtils.createTempFolder();
        
        // extract
        ZipUtil.unpack(zip, folder);
        
        // return the main folder
        return folder;
    }

    public static File createTempFolder() {
        // create a temporary directory
        File tempFolder = new File(System.getProperty(SystemPropertyNames.JAVA_TMPDIR));
        File folder = new File(tempFolder, "neo-" + System.currentTimeMillis());
        folder.mkdirs();
        
        return folder;
    }
}