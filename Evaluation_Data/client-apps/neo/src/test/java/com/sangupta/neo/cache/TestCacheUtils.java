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

import org.junit.Assert;
import org.junit.Test;

public class TestCacheUtils {

    @Test
    public void testResovleTemplatePath() {
        Assert.assertNull(CacheUtils.resovleTemplatePath(null));
        Assert.assertNull(CacheUtils.resovleTemplatePath(""));
        
        ProjectTemplate path = CacheUtils.resovleTemplatePath("aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.CACHE, path.provider);
        Assert.assertNull(path.user);
        Assert.assertNotNull(path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("c:/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.LOCAL, path.provider);
        Assert.assertNull(path.user);
        Assert.assertNotNull(path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.LOCAL, path.provider);
        Assert.assertNull(path.user);
        Assert.assertNotNull(path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("~/.m2");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.LOCAL, path.provider);
        Assert.assertNull(path.user);
        Assert.assertNotNull(path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("./src");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.LOCAL, path.provider);
        Assert.assertNull(path.user);
        Assert.assertNotNull(path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("github:sangupta/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.GITHUB, path.provider);
        Assert.assertEquals("sangupta", path.user);
        Assert.assertNotNull("aem", path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("github.com:sangupta/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.GITHUB, path.provider);
        Assert.assertEquals("sangupta", path.user);
        Assert.assertNotNull("aem", path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("github.com/sangupta/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.GITHUB, path.provider);
        Assert.assertEquals("sangupta", path.user);
        Assert.assertNotNull("aem", path.repository);
        Assert.assertNull(path.path);
        
        path = CacheUtils.resovleTemplatePath("http://github.com/sangupta/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.GITHUB, path.provider);
        Assert.assertEquals("sangupta", path.user);
        Assert.assertNotNull("aem", path.repository);
        Assert.assertNull(path.path);

        path = CacheUtils.resovleTemplatePath("https://github.com/sangupta/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.GITHUB, path.provider);
        Assert.assertEquals("sangupta", path.user);
        Assert.assertNotNull("aem", path.repository);
        Assert.assertNull(path.path);

        path = CacheUtils.resovleTemplatePath("git@github.com/sangupta/aem");
        Assert.assertNotNull(path);
        Assert.assertEquals(TemplateProvider.GITHUB, path.provider);
        Assert.assertEquals("sangupta", path.user);
        Assert.assertNotNull("aem", path.repository);
        Assert.assertNull(path.path);
    }
}