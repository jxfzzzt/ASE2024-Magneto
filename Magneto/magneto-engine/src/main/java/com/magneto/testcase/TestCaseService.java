package com.magneto.testcase;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson2.JSON;
import com.magneto.config.ConfigProperty;
import com.magneto.config.GlobalConfiguration;
import com.magneto.dependency.MavenDependency;
import com.magneto.fuzz.FuzzClassLoader;
import com.magneto.testcase.model.MetaInfo;
import com.magneto.testcase.model.TestcaseUnit;
import com.magneto.testcase.runner.RunResult;
import com.magneto.testcase.runner.TestcaseRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@Component
@Slf4j
public class TestCaseService {
    private static final Set<String> BAN_DIR_NAME = new HashSet<>(Arrays.asList("target", ".idea", ".DS_Store", ".vscode"));

    private static final String METAINFO_FILE_NAME = "metainfo.json";

    @Autowired
    private ConfigProperty configProperty;

    private List<File> testcaseFileList = null; // testcase root dir path list

    private List<MetaInfo> metaInfoCache = null; // metainfo file path list

    private Set<MavenDependency> vulDependencySetCache = null; // dependency set

    private Map<String, File> testcaseFileMap = null; // vulnerability name ---> testcase root dir path

    private final Map<Pair<TestcaseUnit, String>, RunResult> testcaseExecResultMap = new HashMap<>(); // (testcaseUnit, version) ---> testcase execute result

    private Map<MavenDependency, List<MetaInfo>> dependencyMetaInfoMap = null;

    public TestCaseService() {

    }

    public synchronized List<TestcaseUnit> getTestcaseUnitsByDependency(MavenDependency dependency) {
        if (dependencyMetaInfoMap == null) {
            dependencyMetaInfoMap = new HashMap<>();
            List<MetaInfo> allMetaInfo = getAllMetaInfo();
            for (MetaInfo metaInfo : allMetaInfo) {
                String groupId = metaInfo.getGroupId();
                String artifactId = metaInfo.getArtifactId();
                List<String> affectedVersionList = metaInfo.getAffectedVersion();

                if (metaInfo.getAffectedVersion() == null) {
                    throw new RuntimeException("the " + metaInfo.getVulName() + "'s affect version of metainfo can not be null");
                }

                for (String version : affectedVersionList) {
                    MavenDependency d = new MavenDependency(groupId, artifactId, version);
                    List<MetaInfo> metaInfos = dependencyMetaInfoMap.getOrDefault(d, new ArrayList<>());
                    metaInfos.add(metaInfo);
                    dependencyMetaInfoMap.put(d, metaInfos);
                }
            }
        }

        List<MetaInfo> metaInfoList = dependencyMetaInfoMap.getOrDefault(dependency, null);
        if (metaInfoList == null) {
            return new ArrayList<>();
        }

        List<TestcaseUnit> testcaseUnitList = new ArrayList<>();
        for (MetaInfo metaInfo : metaInfoList) {
            String vulName = metaInfo.getVulName();
            String groupId = metaInfo.getGroupId();
            String artifactId = metaInfo.getArtifactId();
            File storageDir = metaInfo.getStorageDir();
            List<TestcaseUnit> testcaseUnits = metaInfo.getTestcaseUnitList();
            for (TestcaseUnit testcaseUnit : testcaseUnits) {
                testcaseUnit.setGroupId(groupId);
                testcaseUnit.setArtifactId(artifactId);
                testcaseUnit.setVulName(vulName);
                testcaseUnit.setStorageDir(storageDir);

                testcaseUnitList.add(testcaseUnit);
            }
        }

        return testcaseUnitList;
    }

    public synchronized List<MetaInfo> getAllMetaInfo() {
        if (metaInfoCache != null) return metaInfoCache;
        else {
            List<MetaInfo> metaInfoList = new ArrayList<>();
            List<File> dataDirList = getAllDataDir();
            for (File file : dataDirList) {
                File metainfoFile = FileUtil.file(file.getAbsolutePath(), METAINFO_FILE_NAME);
                if (metainfoFile.exists() && metainfoFile.isFile()) {
                    FileReader reader = new FileReader(metainfoFile);
                    MetaInfo metaInfo = JSON.parseObject(reader.readString(), MetaInfo.class);
                    // if target_vulnerability not null, then only consider target_vulnerability
                    if (GlobalConfiguration.TARGET_VULNERABILITY != null
                            && !metaInfo.getVulName().equals(GlobalConfiguration.TARGET_VULNERABILITY)) {
                        continue;
                    }
                    metaInfo.setStorageDir(file);
                    metaInfoList.add(metaInfo);
                } else {
                    throw new RuntimeException("Metainfo file is not exist: " + file.getAbsolutePath());
                }
            }
            this.metaInfoCache = metaInfoList;
            return metaInfoList;
        }
    }

    public synchronized Set<MavenDependency> getVulDependencySet() {
        if (vulDependencySetCache != null) return vulDependencySetCache;
        else {
            vulDependencySetCache = new HashSet<>();
            List<MetaInfo> allMetaInfo = getAllMetaInfo();
            for (MetaInfo metaInfo : allMetaInfo) {
                if (metaInfo.getAffectedVersion() == null) {
                    throw new RuntimeException("the " + metaInfo.getVulName() + "'s affect version of metainfo can not be null");
                }
                for (String affectedVersion : metaInfo.getAffectedVersion()) {
                    MavenDependency dependency = new MavenDependency(metaInfo.getGroupId(), metaInfo.getArtifactId(), affectedVersion);
                    vulDependencySetCache.add(dependency);
                }
            }
            return vulDependencySetCache;
        }
    }

    private synchronized List<File> getAllDataDir() {
        if (testcaseFileList != null) return testcaseFileList;
        else {
            String groundTruthPath = configProperty.getGroundTruthPath();
            File groundtruthFile = FileUtil.file(groundTruthPath);
            if (groundtruthFile.exists() && groundtruthFile.isDirectory()) {
                File[] files = groundtruthFile.listFiles(File::isDirectory);

                if (files == null) {
                    throw new RuntimeException("the files array is null");
                }

                testcaseFileList = new ArrayList<>();
                for (File file : files) {
                    String fileName = FileNameUtil.mainName(file);
                    if (BAN_DIR_NAME.contains(fileName)) {
                        continue;
                    }
                    testcaseFileList.add(file);
                }
                return testcaseFileList;
            } else {
                throw new RuntimeException("the groundtruth directory is not exist.");
            }
        }
    }

    public synchronized RunResult getTestcaseExecuteResult(FuzzClassLoader fuzzClassLoader,
                                                           TestcaseUnit testcaseUnit,
                                                           String version) throws Exception {
        Pair<TestcaseUnit, String> key = new Pair<>(testcaseUnit, version);
        if (testcaseExecResultMap.containsKey(key)) {
            return testcaseExecResultMap.get(key);
        }

        File storageDir = testcaseUnit.getStorageDir();
        TestcaseRunner runner = new TestcaseRunner(storageDir);
        String testcaseClassName = testcaseUnit.getTestcaseClassName();

        if (StringUtils.isEmpty(testcaseClassName)) {
            throw new RuntimeException("the testcase class name is empty.");
        }

        RunResult runResult = runner.runTestcaseWithVersion(fuzzClassLoader, testcaseClassName, version);
        testcaseExecResultMap.put(key, runResult);

        return runResult;
    }

}
