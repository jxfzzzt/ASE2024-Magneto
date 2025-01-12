package net.pinodev.ultraplaytime.libs;

import java.util.Base64;

public enum Dependency {


    H2_DRIVER(
            "com.h2database",
            "h2",
            "2.1.214",
            "1iPNwPYdIYz1SajQnxw5H/kQlhFrIuJHVHX85PvnK9A="
    ),
    MARIADB_DRIVER(
            "org.mariadb.jdbc",
            "mariadb-java-client",
            "3.1.3",
            "ESl+5lYkJsScgTh8hgFTy8ExxMPQQkktT20tl6s6HKU="
    ),
    HIKARI_CP(
            "com.zaxxer",
            "HikariCP",
            "4.0.3",
            "fAJK7/HBBjV210RTUT+d5kR9jmJNF/jifzCi6XaIxsk="
    );
//   SNAPPY_JAVA("org{}xerial{}snappy",
//        "snappy-java",
//        "1.1.9.1",
//        "tpa8kQXR6GnePlsrsWW9Ckgr8IQVkX4l0px0YeWxSW4=");

    public final String dependencyMavenRepository;


    public final String dependencyArtifactID;
    public final String dependencyVersion;
    public final byte[] dependencyChecksum;

    Dependency(String groupId, String artifactId, String version, String checksum) {
        this.dependencyMavenRepository = groupId;
        this.dependencyArtifactID = artifactId;
        this.dependencyVersion = version;
        this.dependencyChecksum = Base64.getDecoder().decode(checksum);
    }


}
