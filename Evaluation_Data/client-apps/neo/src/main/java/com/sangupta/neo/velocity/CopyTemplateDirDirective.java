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

package com.sangupta.neo.velocity;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.neo.NeoGenerator;
import com.sangupta.neo.VelocityUtils;

public class CopyTemplateDirDirective extends Directive {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyTemplateDirDirective.class);
    
	@Override
	public String getName() {
		return "copyTemplateDir";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		String source = null;
		String destination = null;
		
		if(node.jjtGetChild(0) != null) {
			source = String.valueOf(node.jjtGetChild(0).value(context));
		}
        if(node.jjtGetChild(1) != null) {
            destination = String.valueOf(node.jjtGetChild(1).value(context));
        }
        
        if(AssertUtils.isEmpty(source)) {
            LOGGER.error("Source dir cannot be empty/null");
        }

        if(AssertUtils.isEmpty(destination)) {
            LOGGER.error("Destination dir cannot be empty/null");
        }

		File src = NeoGenerator.getInstance().getResource(source);
        File dest = new File(NeoGenerator.getInstance().getProjectFolder(), destination);
        
        dest.mkdirs();
		
		// TODO: check if we are not going out of parent folder
		Collection<File> files = FileUtils.listFilesAndDirs(src, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		if(AssertUtils.isNotEmpty(files)) {
		    for(File file : files) {
		        String path = file.getAbsolutePath();
		        if(path.startsWith(src.getAbsolutePath())) {
		            path = path.substring(src.getAbsolutePath().length());
		        }
		        
		        if(AssertUtils.isEmpty(path)) {
		            continue;
		        }
		        
		        File newFile = new File(dest, path);
		        
                if(file.isDirectory()) {
                    newFile.mkdirs();
                    continue;
                }
                
		        String contents = FileUtils.readFileToString(file);
		        contents = VelocityUtils.processWithVelocity(contents);
		        
		        FileUtils.writeStringToFile(newFile, contents);
		    }
		}
		
		System.out.println("Directory copied from: " + src.getAbsolutePath() + " to: " + dest.getAbsolutePath());
		return true;
	}

}