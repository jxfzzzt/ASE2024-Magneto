package com.magneto.dependency;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.magneto.config.GlobalConfiguration;
import com.magneto.util.CommandUtil;
import com.magneto.util.JarUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class MavenDependencyTree {

    private static final String POM_FILE_NAME = "pom.xml";
    private static final String MAVEN_CENTRAL_URL = "https://repo1.maven.org/maven2/";

    private final String projectPath; // project dir path
    private final File projectDir; // project dir
    private final File dependencyDir; // maven dependency dir
    private final File classpathFile; // client project classespath
    private final File jarFile; // client project jar file path
    private final File pomFile; // maven pom file
    private final Model model; // maven model
    private final List<String> repoUrls; // maven download repo urls
    private final Map<String, String> propertyMap; // property in pom.xml
    private final List<String> dependencyJarPathList; // client project's dependency jar file path
    private final Map<MavenDependency, String> dependencyJarPathMap; // mavenDependency ---> jar file path
    private final Map<String, MavenDependency> jarPathDependencyMap; // jar file path ---> mavenDependency
    private MavenDependency rootNode; // the client project mavenDependency node
    private final Set<Character> IGNORED_CHARACTER = new HashSet<>(Arrays.asList('|', '+', '-', ' ', '\\'));

    private final Map<Pair<String, String>, String> versionMap = new HashMap<>(); // (groupId, artifactId) ---> version

    private Model getMavenModel() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        return reader.read(new java.io.FileReader(pomFile));
    }

    private Map<String, String> initProperty() {
        Map<String, String> map = new HashMap<>();
        if (model.getGroupId() != null) {
            map.put("project.groupId", model.getGroupId());
        }

        if (model.getArtifactId() != null) {
            map.put("project.artifactId", model.getArtifactId());
        }

        for (Map.Entry<Object, Object> entry : model.getProperties().entrySet()) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }

        return map;
    }

    private List<String> initRepoUrls() {
        List<String> urls = new ArrayList<>();
        urls.add(MAVEN_CENTRAL_URL);
        List<Repository> repositories = model.getRepositories();

        if (repositories != null) {
            for (Repository repository : repositories) {
                String url = repository.getUrl();
                if (url != null) {
                    urls.add(url);
                }
            }
        }
        return urls;
    }

    private String getWithProperty(String key) {
        String pattern = "\\$\\{(.+?)\\}";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(key);

        if (matcher.find()) {
            String realKey = matcher.group(1);
            return propertyMap.getOrDefault(realKey, key);
        } else {
            return key;
        }
    }

    public MavenDependencyTree(final File projectDir) throws Exception {
        this.projectPath = projectDir.getAbsolutePath();
        this.projectDir = projectDir;

        this.pomFile = FileUtil.file(this.projectPath, POM_FILE_NAME);

        if (!this.pomFile.exists()) {
            throw new RuntimeException("the pom file not exist");
        }

        this.model = getMavenModel();
        this.propertyMap = initProperty();
        this.repoUrls = initRepoUrls();

        File file = null;
        // deal with versionMap
        file = getDependencyTreeFile(false);
        if (file.exists() && file.isFile()) {
            FileReader reader = new FileReader(file, CharsetUtil.defaultCharset());
            String content = reader.readString().trim();
            parseVersion(content);
        } else {
            throw new RuntimeException("dependency tree file is not exist");
        }

        // deal with dependency tree
        file = getDependencyTreeFile(true);
        if (file.exists() && file.isFile()) {
            FileReader reader = new FileReader(file, CharsetUtil.defaultCharset());
            String content = reader.readString().trim();
            parseText(content);
        } else {
            throw new RuntimeException("dependency tree file is not exist");
        }

        assert (rootNode != null);

        // maven package
        String packageCommand = "mvn clean package -DskipTests";
        CommandUtil.execCommand(projectDir, packageCommand);
        this.classpathFile = FileUtil.file(projectPath, "target", "classes");

        String finalName = null;
        if (model.getBuild() != null) {
            finalName = model.getBuild().getFinalName();
        }

        String jarName = null;
        if (finalName != null) {
            jarName = getWithProperty(finalName) + ".jar";
        } else {
            jarName = StrUtil.format("{}-{}.jar", getProjectArtifactId(), getProjectVersion());

        }
        this.jarFile = FileUtil.file(projectPath, "target", jarName);
        if (!classpathFile.exists() || !jarFile.exists()) {
            throw new RuntimeException("client project package fail");
        }

        // todo need to auto
        this.dependencyDir = FileUtil.file(projectPath, "target", "dependency");
        // download dependency jar files
        this.jarPathDependencyMap = new HashMap<>();
        this.dependencyJarPathMap = new HashMap<>();
        downloadJar(rootNode);

        // Walk dependency tree of client project
        this.dependencyJarPathList = Files.walk(Paths.get(GlobalConfiguration.JAR_DIR_PATH))
                .map(Path::toString).filter(s -> "jar".equals(FileNameUtil.extName(s))).collect(Collectors.toList());
    }

    private File getDependencyTreeFile(boolean detail) throws Exception {
        // generate dependency file to cache dir
        File file = FileUtil.file(GlobalConfiguration.CACHE_DIR_PATH, "dependency_tree.txt");
        String command;
        if (detail) {
            command = StrUtil.format("mvn dependency:tree -Dverbose -DoutputFile={}", file.getAbsoluteFile());
        } else {
            command = StrUtil.format("mvn dependency:tree -DoutputFile={}", file.getAbsoluteFile());
        }
        CommandUtil.execCommand(projectDir, command);
        return file;
    }

    private void parseVersion(String content) {
        String[] lines = content.split("\n");
        for (String line : lines) {
            int j = 0;
            while (j < line.length() && IGNORED_CHARACTER.contains(line.charAt(j))) j++;
            line = line.substring(j);
            String[] split = line.split(":");

            if (split.length >= 5) {
                String groupId = split[0];
                String artifactId = split[1];
                String version = split[split.length - 2];
                String scope = split[split.length - 1];

                if ("test".equals(scope)) {
                    continue;
                }

                Pair<String, String> pair = new Pair<>(groupId, artifactId);
                versionMap.put(pair, version);
            } else {
                String groupId = split[0];
                String artifactId = split[1];
                String version = split[split.length - 1];

                Pair<String, String> pair = new Pair<>(groupId, artifactId);
                versionMap.put(pair, version);
            }
        }
    }

    private void parseText(String content) {
        String[] lines = content.split("\n");
        Map<Integer, MavenDependency> depthMap = new HashMap<>();

        for (String line : lines) {
            Integer degree = getDegree(line);
            MavenDependency dependency = parseDependency(line);

            if (dependency == null) {
//                depthMap.remove(degree);
                continue;
            }

            if ("test".equals(dependency.getScope())) {
                continue;
            }

            if (Integer.valueOf(0).equals(degree)) {
                depthMap.put(degree, dependency);
                rootNode = dependency;
            } else {
                MavenDependency parentDependency = depthMap.getOrDefault(degree - 1, null);
                if (parentDependency == null) continue;
                parentDependency.appendDependencyNode(dependency);
                depthMap.put(degree, dependency);
            }
        }
    }

    private Integer getDegree(final String line) {
        int j = 0;
        while (j < line.length() && IGNORED_CHARACTER.contains(line.charAt(j))) j++;
        assert j % 3 == 0;
        return j / 3;
    }

    private MavenDependency parseDependency(String line) {
        int j = 0;
        while (j < line.length() && IGNORED_CHARACTER.contains(line.charAt(j))) j++;
        line = line.substring(j);

        String groupId = null, artifactId = null, scope = null;

        if (line.charAt(0) == '(') {
            line = line.substring(1, line.length() - 2).trim().split(" ")[0].trim();
            String[] split = line.split(":");
            groupId = split[0];
            artifactId = split[1];
            scope = split[split.length - 1];
        } else {
            String[] split = line.split(":");
            groupId = split[0];
            artifactId = split[1];
            if (split.length >= 5) {
                scope = split[split.length - 1];
            }
        }

        Pair<String, String> pair = new Pair<>(groupId, artifactId);
        String version = versionMap.getOrDefault(pair, null);

        if (groupId == null || artifactId == null || version == null) return null;

        return new MavenDependency(groupId, artifactId, version, scope, new HashSet<>());
    }

    public MavenDependency getRootNode() {
        return rootNode;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public File getProjectDir() {
        return projectDir;
    }

    public List<String> getDependencyJarPathList() {
        return dependencyJarPathList;
    }

    public Map<String, MavenDependency> getJarPathDependencyMap() {
        return jarPathDependencyMap;
    }

    public Map<MavenDependency, String> getDependencyJarPathMap() {
        return dependencyJarPathMap;
    }

    public File getClasspathFile() {
        return classpathFile;
    }

    public File getJarFile() {
        return jarFile;
    }

    public Map<Pair<String, String>, String> getVersionMap() {
        return versionMap;
    }

    public String getProjectGroupId() {
        return rootNode.getGroupId();
    }

    public String getProjectArtifactId() {
        return rootNode.getArtifactId();
    }

    public String getProjectVersion() {
        return rootNode.getVersion();
    }


    private File findDependencyInLocal(MavenDependency mavenDependency) {
        String jarName = mavenDependency.getArtifactId() + "-" + mavenDependency.getVersion();

        File[] files = FileUtil.ls(dependencyDir.getAbsolutePath());

        // exact matching
        for (File file : files) {
            if (file.isFile() && file.getName().equals(jarName + "jar")) {
                return file;
            }
        }

        // prefix matching
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith(jarName)) {
                return file;
            }
        }

        return null;
    }

    // download the dependency jar file
    private void downloadJar(MavenDependency mavenDependency) {
        if (!rootNode.equals(mavenDependency)) {
            log.info("begin get [{}]", mavenDependency.getDescriptor());

            File file = JarUtil.downloadJar(mavenDependency, repoUrls);

            if (!file.exists()) {
                if (dependencyDir.exists()) {
                    File localDependency = findDependencyInLocal(mavenDependency);
                    if (localDependency != null && localDependency.exists()) {
                        FileUtil.copy(localDependency, file, false);
                        file = FileUtil.file(file.getAbsolutePath());
                    }
                }
                if (!file.exists()) {
                    throw new RuntimeException("download jar file fail: " + mavenDependency.getDescriptor());
                }
            }

            mavenDependency.setJarFile(file);
            this.jarPathDependencyMap.put(file.getAbsolutePath(), mavenDependency);
            this.dependencyJarPathMap.put(mavenDependency, file.getAbsolutePath());
        } else {
            // root node
            mavenDependency.setJarFile(this.jarFile);
            this.jarPathDependencyMap.put(this.jarFile.getAbsolutePath(), mavenDependency);
            this.dependencyJarPathMap.put(mavenDependency, this.jarFile.getAbsolutePath());
        }

        for (MavenDependency dependency : mavenDependency.getDependencies()) {
            if (dependency != null) {
                downloadJar(dependency);
            }
        }
    }

    private void getString(Integer depth, MavenDependency node, StringBuilder sb) {
        for (int i = 0; i < depth; i++) {
            sb.append("   ");
        }
        sb.append(node.getDescriptor());
        sb.append('\n');
        for (MavenDependency sonNode : node.getDependencies()) {
            if (sonNode != null)
                getString(depth + 1, sonNode, sb);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        getString(0, rootNode, sb);
        return sb.toString().trim();
    }
}
