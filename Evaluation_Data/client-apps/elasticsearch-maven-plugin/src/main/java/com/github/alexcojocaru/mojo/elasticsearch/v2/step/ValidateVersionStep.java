package com.github.alexcojocaru.mojo.elasticsearch.v2.step;

import org.apache.commons.lang3.StringUtils;

import com.github.alexcojocaru.mojo.elasticsearch.v2.ClusterConfiguration;
import com.github.alexcojocaru.mojo.elasticsearch.v2.ElasticsearchSetupException;
import com.github.alexcojocaru.mojo.elasticsearch.v2.util.VersionUtil;

/**
 * Validate that the provided Elasticsearch version is valid (ie. 5.0.0+).
 * 
 * @author Alex Cojocaru
 */
public class ValidateVersionStep
        implements ClusterStep
{

    @Override
    public void execute(ClusterConfiguration config)
    {
        String version = config.getVersion();
        
        if (StringUtils.isBlank(version))
        {
            throw new ElasticsearchSetupException(String.format(
                    "Please provide a valid Elasticsearch version."));
        }
        
        if (VersionUtil.isUnder_5_0_0(version))
        {
            throw new ElasticsearchSetupException(String.format(
                    "elasticsearch-maven-plugin supports only versions 5+ of Elasticsearch. You configured: %s.",
                    version));
        }
    }

}
