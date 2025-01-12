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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.neo.NeoGenerator;

public class PrintContextDirective extends Directive {

	@Override
	public String getName() {
		return "printContext";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
	    Map<String, Object> map = NeoGenerator.getInstance().getProperties();
	    if(AssertUtils.isEmpty(map)) {
	        return true;
	    }
	    
	    for(Entry<String, Object> entry : map.entrySet()) {
	        System.out.println("Property: " + entry.getKey() + " = " + entry.getValue());
	    }
	    
		return true;
	}

}