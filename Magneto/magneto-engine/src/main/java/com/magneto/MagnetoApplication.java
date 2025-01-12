package com.magneto;

import cn.hutool.core.io.FileUtil;
import com.magneto.config.GlobalConfiguration;
import com.magneto.config.ProjectContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

import static com.magneto.Magneto.*;


@Slf4j
@Component
@EnableScheduling
@SpringBootApplication
public class MagnetoApplication {

    private static Magneto magneto;

    @Autowired
    public void setClientDefender(Magneto magneto) {
        MagnetoApplication.magneto = magneto;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MagnetoApplication.class, args);

        log.info("input args: {}", Arrays.toString(args));
        ProjectContext projectContext = parseArgs(args);

        magneto.run(projectContext);
    }

    private static ProjectContext parseArgs(String[] args) {
        Options options = new Options();
        options.addOption(new Option(PROJECT_ROOT_PATH, true, "detect project root path"));
        options.addOption(new Option(SKIP_FUZZING, false, "whether to skip the fuzzing procedure"));
        options.addOption(new Option(OUTPUT_DIR, true, "result output directory"));
        options.addOption(new Option(TARGET_VULNERABILITY, true, "target detect vulnerability"));

        CommandLineParser parser = new DefaultParser();

        ProjectContext projectContext = null;

        try {
            CommandLine line = parser.parse(options, args);
            String projectPath = line.getOptionValue(PROJECT_ROOT_PATH);

            if (projectPath == null) {
                log.warn("project path is null, start checking default demo project");
                projectPath = System.getProperty("user.dir") + File.separator + "test-tool-demo";
            }

            File projectDir = FileUtil.file(projectPath);
            if (projectDir == null || !projectDir.exists() || !projectDir.isDirectory()) {
                throw new RuntimeException("the input project directory must be exist");
            }

            String outputDir = line.getOptionValue(OUTPUT_DIR);
            if (outputDir != null) {
                File outputFile = FileUtil.file(outputDir);

                GlobalConfiguration.OUTPUT_DIR_PATH = outputFile.getAbsolutePath();
                GlobalConfiguration.OUTPUT_DIR = outputFile;
            }

            String targetVulnerability = line.getOptionValue(TARGET_VULNERABILITY);
            if (targetVulnerability != null) {
                GlobalConfiguration.TARGET_VULNERABILITY = targetVulnerability;
            }

            projectContext = new ProjectContext(projectPath, projectDir, line);
        } catch (ParseException e) {
            log.error("ParsingCommandError: ", e);
        }
        return projectContext;
    }

}
