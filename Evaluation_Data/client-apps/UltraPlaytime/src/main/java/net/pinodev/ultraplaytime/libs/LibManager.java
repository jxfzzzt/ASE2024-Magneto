package net.pinodev.ultraplaytime.libs;

import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

import static net.pinodev.ultraplaytime.UltraPlaytime.logger;
import static net.pinodev.ultraplaytime.UltraPlaytime.MainInstance;

public class LibManager {
    BukkitLibraryManager bukkitLibraryManager;

    public LibManager(){
        bukkitLibraryManager = new BukkitLibraryManager(MainInstance);
        loadLibraries();
    }


    public void loadLibraries(){
        logger.info("Loading dependencies....");
        for(Dependency dependency : Dependency.values()){
            Library lib;
                lib = Library.builder()
                        .groupId(dependency.dependencyMavenRepository)
                        .artifactId(dependency.dependencyArtifactID)
                        .version(dependency.dependencyVersion)
                        .checksum(dependency.dependencyChecksum)
                        .build();
                bukkitLibraryManager.addMavenCentral();
                bukkitLibraryManager.loadLibrary(lib);

        }
    }
}
