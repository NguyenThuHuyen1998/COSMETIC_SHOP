package com.example.crud.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.crud.repository.ImageRepository;
import com.example.crud.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  private final Path root = Paths.get("uploads");
  private ImageRepository imageRepository;

  @Autowired
  public FilesStorageServiceImpl(ImageRepository imageRepository){
    this.imageRepository= imageRepository;
  }

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }


  @Override
  public void save(MultipartFile file) {
    try {
      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }


  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

//  static AWSCredentials credentials = new BasicAWSCredentials(
//          "AKIAVP7CK36VWMYYTRIS",
//          "pqZPQabKlJ6gUMAPrQywyCEut5Bgs+fe7gFe76KO"
//  );
//  public static final AmazonS3 s3 = AmazonS3ClientBuilder
//          .standard()
//          .withCredentials(new AWSStaticCredentialsProvider(credentials))
//          .withRegion(Regions.US_EAST_1)
//          .build();
//
//  static final String bucket_name = "cosmetic-shop";
//
//  static File convertMultiPartToFile(MultipartFile file) throws IOException {
//    File convFile = new File(file.getOriginalFilename());
//    FileOutputStream fos = new FileOutputStream(convFile);
//    fos.write(file.getBytes());
//    fos.close();
//    return convFile;
//  }
//
//  static void uploadFileTos3bucket(String fileName, File file) {
//    s3.putObject(new PutObjectRequest(bucket_name, fileName, file)
//            .withCannedAcl(CannedAccessControlList.PublicRead));
//  }
//
//  public static List<String> listObjects() {
//    List<String> res = new ArrayList<>();
//    ListObjectsV2Result result = s3.listObjectsV2(bucket_name);
//    List<S3ObjectSummary> objects = result.getObjectSummaries();
//    for (S3ObjectSummary os : objects) {
//      res.add(os.getKey());
//    }
//    return res;
//  }
//
//  public static void uploadObject(String key_name, String file_path) {
//    s3.putObject(bucket_name, key_name, new File(file_path));
//  }
//
//  public static String uploadFile(MultipartFile multipartFile, long productId, long vendorId) {
//
//    String fileUrl = "";
//    try {
//      File file = convertMultiPartToFile(multipartFile);
//      String fileName = generateFileName(multipartFile);
//      fileUrl = bucket_name + ".s3.amazonaws.com/" + fileName;
//      uploadFileTos3bucket(fileName, file);
//      file.delete();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    JSONObject response = new JSONObject();
//    response.put("url",fileUrl);
//    return fileUrl;
//  }
//
//  static String generateFileName(MultipartFile multiPart) {
//    return ""+ new Date().getTime();
//  }
//
//  public static String deleteFileFromS3Bucket(String fileUrl) {
//    String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
//    System.out.println(fileName);
//    try {
//      s3.deleteObject(new DeleteObjectRequest(bucket_name, fileName));
//    } catch (Exception e) {
//      System.out.println(e);
//    }
//    ;
//    return "Successfully deleted";
//  }
//
//  public static void downloadObject(String key_name) throws IOException {
//    S3Object o = s3.getObject(bucket_name, key_name);
//    S3ObjectInputStream s3is = o.getObjectContent();
//    FileOutputStream fos = new FileOutputStream(new File(key_name));
//    byte[] read_buf = new byte[1024];
//    int read_len;
//    while ((read_len = s3is.read(read_buf)) > 0) {
//      fos.write(read_buf, 0, read_len);
//    }
//    s3is.close();
//    fos.close();
//  }
//
//  /*
//   * public static AmazonS3 getS3() { return s3; }
//   */
//
//  public static String getBucketName() {
//    return bucket_name;
//  }


}
