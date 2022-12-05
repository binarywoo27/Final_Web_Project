package com.ssafy.home.controller;

//import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Log4j2
@RestController
public class FileController {

    private final static String imageDirectory = Paths.get("").toAbsolutePath() + "/images/";

    public FileController() {
        File file = new File(imageDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String getExtension(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        int index = fileName.indexOf(".");
        if (index > -1) {
            return fileName.substring(index);
        }
        return "";
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> image(@PathVariable String fileName) throws FileNotFoundException {
        String filePath = imageDirectory + fileName;
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(filePath));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }

    @GetMapping("/files")
    public List<String> getFileNames() {
        return Stream.of(new File(imageDirectory).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/files")
    public String uploadFiles(@RequestParam("files") List<MultipartFile> files) {
    	String dbfilename = null;
    	for (MultipartFile multipartFile : files) {
        	dbfilename = UUID.randomUUID() + getExtension(multipartFile);
            String filePath = imageDirectory + dbfilename;
            System.out.println("파일 저장 위치" + filePath);
            try (FileOutputStream writer = new FileOutputStream(filePath)) {
                writer.write(multipartFile.getBytes());
            } catch (Exception e) {
//                log.error(e.getMessage(), e);
                throw new RuntimeException("Fail to upload files.");
            }
        }
        return dbfilename;
    }
}