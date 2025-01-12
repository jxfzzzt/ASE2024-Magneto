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

package com.sangupta.neo.download;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.util.UriUtils;

public class DownloadManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    public static File downloadToTempFile(String url) throws IOException {
        String name = UriUtils.extractFileName(url);
        String extension = UriUtils.extractExtension(url);

        File tempFile = File.createTempFile(name + "-", "." + extension);
        
        LOGGER.debug("Downloading {} to {}", url, tempFile.getAbsolutePath());
        
        try {
            WebRequest.get(url).execute().writeToFile(tempFile);
            return tempFile;
        } catch(HttpResponseException e) {
            LOGGER.error("HTTP response did not yield an OK status", e);
        } catch(IOException e) {
            LOGGER.error("Unable to download url to temp file", e);
        }
        
        return null;
    }
}