package fpt.com.universitymanagement.service;

import java.io.IOException;
import java.io.InputStream;

public interface AzureStorageService {
    String uploadFile(InputStream fileStream, String fileName) throws IOException;
}
