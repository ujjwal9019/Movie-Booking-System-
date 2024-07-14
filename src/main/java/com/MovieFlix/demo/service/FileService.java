package com.MovieFlix.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadFile(String path , MultipartFile file) throws IOException;

//    This method is to fetch the image/file from server to client
    InputStream getResourceFile(String path , String fileName) throws FileNotFoundException;


}
