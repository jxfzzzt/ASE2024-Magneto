package zing.client.file;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;

public class ZFile {

    private String name;

    private String password;

    private String zingAddress;

    private PrivateKey key;

    private ZipFile file;

    public ZFile(File file, String password) throws IOException {
        this.file = new ZipFile(file);
        this.password = password;
        if (!this.file.isValidZipFile()) {
            throw new ZipException("文件无法读取,可能已经被损坏.");
        }
        if (this.file.isEncrypted()) {
            this.file.setPassword(password.toCharArray());
        }
        FileHeader ad = this.file.getFileHeader("address.txt");
        FileHeader key = this.file.getFileHeader("private_key");
        this.zingAddress = IOUtils.toString(this.file.getInputStream(ad), "utf8");
        
    }

    public static void create(String name, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(name + ".zing", password.toCharArray());
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.FAST);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        zipFile.addFile(new File(name + "/address.txt"), parameters);
        zipFile.addFile(new File(name + "/private_key"), parameters);
    }


    public String getZingAddress() {
        return zingAddress;
    }

    public PrivateKey getKey() {
        return key;
    }
}
