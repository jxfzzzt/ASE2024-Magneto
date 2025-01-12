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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ConsoleUtils;
import com.sangupta.jerry.util.StringUtils;
import com.sangupta.neo.domain.NeoInputParam;

public class NeoInputManager {
    
    private Map<String, Object> properties = new HashMap<>();
    
    public NeoInputManager(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    /**
     * Read user input for all parameters that are defined.
     * 
     * @param params
     * @return
     */
    public void getUserInput(NeoInputParam[] params) {
        if(AssertUtils.isEmpty(params)) {
            // nothing to do
            return;
        }
        
        for(NeoInputParam param : params) {
            Object paramValue = readParam(param);
            if(paramValue != null) {
                this.properties.put(param.name, paramValue);
            }
        }
        
        return;
    }

    /**
     * Return the value of the user-input converting it to proper type into an {@link Object}.
     * 
     * @param param
     * @return
     */
    private Object readParam(NeoInputParam param) {
        if(!paramShouldBeRead(param)) {
            return null;
        }
        
        String prompt = param.prompt;
        if(AssertUtils.isEmpty(prompt)) {
            prompt = param.name;
        }
        
        String displayPrompt = prompt;
        if(AssertUtils.isNotEmpty(param.value)) {
            displayPrompt = prompt + " [" + param.value + "]: ";
        }
        
        // process with velocity only if it has a velocity variable in it
        if(displayPrompt.contains("$")) {
            displayPrompt = VelocityUtils.processWithVelocity(displayPrompt);
        }
        
        String input = ConsoleUtils.readLine(displayPrompt, !param.required);
        if(!param.required) {
            if(AssertUtils.isBlank(input)) {
                input = VelocityUtils.processWithVelocity(param.value);
            }
        }
        
        return convertInputToType(param, input);
    }

    /**
     * Convert the given user-input to the type defined in template.
     * 
     * @param param
     * @param input
     * @return
     */
    private Object convertInputToType(NeoInputParam param, String input) {
        // convert the value to proper type
        switch(param.type) {
            case "string":
                return input;
        
            case "int":
            case "integer":
                return StringUtils.getIntValue(input, 0);
                
            case "bool":
            case "boolean":
                return StringUtils.getBoolean(input, false);
                
            case "array":
                return splitToArray(input);
                
            case "list":
                return splitToList(input);
                
            default:
                throw new NeoRuntimeException("Unknown param type detected as: " + param.type);
        }
    }
    
    /**
     * Convert user input into a {@link List} of {@link String} considering values
     * are comma-separated.
     *  
     * @param input
     * @return
     */
    private static List<String> splitToList(String input) {
        List<String> list = new ArrayList<>();
        
        if(AssertUtils.isEmpty(input)) {
            return list;
        }
        
        String[] tokens = input.split(",");
        for(int index = 0; index < tokens.length; index++) {
            list.add(tokens[index].trim());
        }
        
        return list;
    }

    /**
     * Convert user input into a {@link String}-array considering values
     * are comma-separated.
     * 
     * @param input
     * @return
     */
    private static String[] splitToArray(String input) {
        if(AssertUtils.isEmpty(input)) {
            return StringUtils.EMPTY_STRING_LIST;
        }
        
        String[] tokens = input.split(",");
        for(int index = 0; index < tokens.length; index++) {
            tokens[index] = tokens[index].trim();
        }
        
        return tokens;
    }

    /**
     * Decide if the parameter should be read from user or not, checking if the
     * conditions specified in the param are met.
     *  
     * @param param
     * @return
     */
    private boolean paramShouldBeRead(NeoInputParam param) {
        if(AssertUtils.areEmpty(param.ifTrue, param.ifFalse)) {
            return true;
        }
        
        // by default all variables should be read
        boolean result = true;
        
        // if true
        result = result & evaluateCondition(param.ifTrue);
        
        // if false
        result = result & evaluateCondition(param.ifFalse);
        
        return result;
    }
    
    private boolean evaluateCondition(String condition) {
        if(AssertUtils.isEmpty(condition)) {
            return true;
        }
        
        condition = condition.trim();
        
        // check for expression
        if(condition.contains(" ")) {
            String content = VelocityUtils.processWithVelocity("#if (" + condition + ") evaluate #end");
            if(content != null && content.contains("evaluate")) {
                return true;
            }
            
            return false;
        }
        
        if(condition.startsWith("$")) {
            condition = condition.substring(1);
        }
        
        // this is just a variable
        return getBoolean(condition, false);
    }

    /**
     * Convert the value to a {@link Boolean} value.
     * 
     * @param property
     * @param defaultValue
     * @return
     */
    private boolean getBoolean(String property, boolean defaultValue) {
        Object bool = this.properties.get(property);
        if(bool == null) {
            return defaultValue;
        }
        
        if(bool instanceof Boolean) {
            return (Boolean) bool;
        }
        
        return StringUtils.getBoolean(bool.toString(), defaultValue);
    }

}