## Usage

- Maven

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>llc-kits</artifactId>
    <version>1.0.9.RC1</version>
</dependency>

```

- `Zip`解压缩

```java

// 压缩器
Compress compress =
  Llc.builder().decompressEncodeCharset("GBK").outputSiz(1024 * 4).type(Llc.CompressType.ZIP).build().getCompress();

// 测试压缩
File[] files = new File[] {new File("/xxx/xxx.json")};
compress.compress(files, new File("/xxx/xxx/xxx.zip"), false);

// 显示文件列表
compress.listFiles(new File("/xxx/xxx/xxx.zip"));

// 测试解压
compress.decompress(new File("/xxx/xxx/xxx.zip"), "/xxx/xxx/xxx.json");

```

- `7z`解压缩
```java

// 压缩器
Compress compress =
  Llc.builder().outputSiz(1024 * 4).type(Llc.CompressType.SEVENZ).build().getCompress();

// 测试压缩
File[] files = new File[] {new File("/xxx/xxx.json")};
compress.compress(files, new File("/xxx/xxx/xxx.7z"), false);

// 显示文件列表
compress.listFiles(new File("/xxx/xxx/xxx.7z"));

// 测试解压
compress.decompress(new File("/xxx/xxx/xxx.7z"), "/xxx/xxx/xxx.json");
```


- `image`压缩

```java

// 压缩器
Compress compress =
  Llc.builder().outputSiz(1024 * 4).type(Llc.CompressType.IMAGE).build().getCompress();

/**
* Special for picture compression.
*
* @param originPath the original path
* @param outputPath the output path
* @param scale specifies the image size. The value is between 0 and 1, 1f is the original image
*     size and 0.5 is half the original image size.
* @param outputQuality the quality of the picture is also between 0 and 1. The closer it is to 1,
*     the better the quality is. The closer it is to 0,the worse the quality is.
* @return
*/
boolean compressImage(String originPath, String outputPath, float scale, float outputQuality);

/**
 * Special for picture compression with specified length and width.
 *
 * @param originPath the original path
 * @param outputPath the output path
 * @return
 */
boolean compressImage(String originPath, String outputPath, int length, int width);

```

- `pdf`压缩

```java



// 压缩器
Compress compress =
  Llc.builder().outputSiz(1024 * 4).type(Llc.CompressType.PDF).build().getCompress();

// 测试压缩pdf
File[] files = new File[] {new File("/xxx/xxx.pdf")};
compress.compress(files, new File("/xxx/xxx/xxx.pdf"), false);

```
