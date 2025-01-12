package com.magneto.dependency;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MavenDependency {

    private String groupId;

    private String artifactId;

    private String version;

    private String scope;

    private Set<MavenDependency> dependencies;

    private File jarFile;

    public MavenDependency() {
        this.dependencies = new HashSet<>();
    }

    public MavenDependency(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
        this.dependencies = new HashSet<>();
    }

    public MavenDependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.dependencies = new HashSet<>();
    }

    public MavenDependency(String groupId, String artifactId, String version, String scope, Set<MavenDependency> dependencies) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
        this.dependencies = dependencies;
    }

    public void appendDependencyNode(MavenDependency node) {
        if (node != null)
            this.dependencies.add(node);
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }

    public File getJarFile() {
        return jarFile;
    }

    public Set<MavenDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(HashSet<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public String getDescriptor() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.groupId);
        sb.append(':');
        sb.append(this.artifactId);
        sb.append(':');
        sb.append(this.version);
        if (this.scope != null) {
            sb.append(':');
            sb.append(this.scope);
        }

        return sb.toString();
    }

    public String getDependencyKey() {

        assert (this.groupId != null && this.artifactId != null && this.version != null);

        String sb = this.groupId +
                ':' +
                this.artifactId +
                ':' +
                this.version;
        return sb;
    }

    public boolean containsDependency(MavenDependency dep) {
        for (MavenDependency dependency : dependencies) {
            if (dependency.equals(dep)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MavenDependency that = (MavenDependency) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(artifactId, that.artifactId) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return "<" + getDescriptor() + ">";
    }
}
