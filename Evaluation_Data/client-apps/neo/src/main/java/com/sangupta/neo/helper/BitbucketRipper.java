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

package com.sangupta.neo.helper;

import java.io.File;
import java.io.IOException;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.neo.NeoUtils;
import com.sangupta.neo.cache.ProjectTemplate;
import com.sangupta.neo.download.DownloadManager;

/**
 * Class that helps rip bitbucket repositories from internet to local disk
 * so that templates can be used from there.
 * 
 * @author sangupta
 *
 */
public class BitbucketRipper {
    
    /**
     * Download a bitbucket project that hosts a {@link ProjectTemplate}.
     * 
     * @param project
     * @return
     */
    public static File downloadBitbucketTemplate(ProjectTemplate project) {
        if(AssertUtils.isEmpty(project.path)) {
            return ripMasterZip(project);
        }
        
        try {
            return ripSpecificPath(project);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Rip the master branch ZIP and extract it to a local folder.
     * 
     * @param project
     * @return
     */
    private static File ripMasterZip(ProjectTemplate project) {
        // download the entire repository in one shot
        String url = "https://bitbucket.org/" + project.user + "/" + project.repository + "/get/master.zip";
        try {
            File downloaded = DownloadManager.downloadToTempFile(url);
            
            // this is a ZIP file and needs extraction
            System.out.println("Template downloaded to: " + downloaded.getAbsolutePath());
            
            File folder = NeoUtils.extractToFolderIfNeeded(downloaded);
            if(folder == null) {
                System.out.println("Don't know how to process downloaded file at: " + downloaded.getAbsolutePath());
                return null;
            }
            
            // pick up the sub-folder
            File[] files = folder.listFiles();
            if(files == null || files.length == 0 || files.length > 1) {
                System.out.println("Unable to find repository inside extracted ZIP: " + folder.getAbsolutePath());
                return null;
            }
            
            return files[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Helps rip a specific path on bitbucket. Bitbucket currently does not provide a way
     * to rip a specific folder directly, either by an API or by any other means. Thus we will
     * first download the master ZIP, and then pick up the folder from there.
     * 
     * @param project
     * @return
     * @throws IOException
     */
    private static File ripSpecificPath(ProjectTemplate project) throws IOException {
        File folder = ripMasterZip(project);
        
        File subFolder = new File(folder, project.path);
        if(subFolder.exists() && subFolder.isDirectory()) {
            return subFolder;
        }
        
        // choose the specific folder from there
        System.out.println("Couldn't find the path in repository");
        return null;
    }
    
}