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
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.FieldNamingPolicy;
import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.GsonUtils;
import com.sangupta.neo.NeoUtils;
import com.sangupta.neo.cache.ProjectTemplate;
import com.sangupta.neo.download.DownloadManager;

/**
 * Class that helps rip github repositories from internet to local disk
 * so that templates can be used from there.
 * 
 * @author sangupta
 *
 */
public class GithubRipper {
    
    /**
     * Download a github project that hosts a {@link ProjectTemplate}.
     * 
     * @param project
     * @return
     */
    public static File downloadGithubTemplate(ProjectTemplate project) {
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
        String url = "https://github.com/" + project.user + "/" + project.repository + "/archive/master.zip";
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
            File sub = new File(folder, project.repository + "-master");
            if(sub.exists() && sub.isDirectory()) {
                return sub;
            }

            // don't know what to do
            return folder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Helps rip a specific path on github.
     * 
     * @param project
     * @return
     * @throws IOException
     */
    private static File ripSpecificPath(ProjectTemplate project) throws IOException {
        // we need to crawl the site and download the folder - this happens using the API
        final String base = "https://api.github.com/repos/" + project.user + "/" + project.repository + "/contents/";
        String uri = base + project.path;
        Queue<String> apiUrls = new LinkedBlockingQueue<>();
        apiUrls.add(uri);
        
        System.out.println("Retrieving a list of all files in the folder...");
        List<GithubEntry> filesToDownload = new ArrayList<>();
        do {
            if(apiUrls.isEmpty()) {
                break;
            }
            
            String url = apiUrls.poll();
            WebResponse response = WebInvoker.getResponse(url);
            if(response == null || !response.isSuccess()) {
                System.out.println("Unable to fetch valid response from github: " + url);
                continue;
            }
            
            parseResponse(response.getContent(), filesToDownload, apiUrls, base);
        } while(true);
        
        if(AssertUtils.isEmpty(filesToDownload)) {
            System.out.println("Nothing in the specified folder on github. Make sure the path is correct!");
            return null;
        }
        
        // create a temp folder
        File folder = NeoUtils.createTempFolder();
        System.out.println("Github path will be downloaded to folder: " + folder.getAbsolutePath());
        
        // start downloading all the files
        for(GithubEntry e : filesToDownload) {
            if(AssertUtils.isEmpty(e.downloadUrl)) {
                // this is a folder - let's create one in the local disk
                File downloadPath = new File(folder, getDownloadPath(project.path, e.path));
                downloadPath.mkdirs();
                continue;
            }
            

            System.out.println("Downloading file: " + e.downloadUrl);
            String contents = WebInvoker.fetchResponse(e.downloadUrl);
            
            // write these back to disk
            File downloadPath = new File(folder, getDownloadPath(project.path, e.path));
            downloadPath.getParentFile().mkdirs();
            org.apache.commons.io.FileUtils.writeStringToFile(downloadPath, contents);
        }
        
        System.out.println("Done downloading.");
        return folder;
    }
    
    /**
     * Extract the path where we should save downloaded file to local disk.
     * 
     * @param path
     * @param fullPath
     * @return
     */
    private static String getDownloadPath(String path, String fullPath) {
        if(!fullPath.startsWith(path)) {
            return fullPath;
        }
        
        return fullPath.substring(path.length());
    }

    /**
     * Parse the repsonse from github API url.
     * 
     * @param json
     * @param filesToDownload
     * @param apiUrls
     * @param base
     */
    private static void parseResponse(String json, List<GithubEntry> filesToDownload, Queue<String> apiUrls, String base) {
        if(AssertUtils.isEmpty(json)) {
            return;
        }
        
        GithubEntry[] entries = GsonUtils.getGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).fromJson(json, GithubEntry[].class);
        if(AssertUtils.isEmpty(entries)) {
            return;
        }
        
        for(GithubEntry e : entries) {
            if(AssertUtils.isEmpty(e.downloadUrl)) {
                // this is a folder
                apiUrls.add(base + e.path);
                continue;
            }
            
            filesToDownload.add(e);
        }
    }

    /**
     * Two fields that we are interested in from github around this repository.
     * 
     * @author sangupta
     *
     */
    private static class GithubEntry {
        
        public String path;
        
        public String downloadUrl;
        
        @Override
        public String toString() {
            return this.path;
        }

    }

}