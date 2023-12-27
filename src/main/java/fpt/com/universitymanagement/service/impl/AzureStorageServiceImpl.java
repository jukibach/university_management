package fpt.com.universitymanagement.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import fpt.com.universitymanagement.common.ContentTypeUtil;
import fpt.com.universitymanagement.service.AzureStorageService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class AzureStorageServiceImpl implements AzureStorageService {
    private final Environment environment;
    
    public AzureStorageServiceImpl(Environment environment) {
        this.environment = environment;
    }
    
    @Override
    public String uploadFile(InputStream fileStream, String fileName) throws IOException {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(Objects.requireNonNull(environment.getProperty("azure.storage.connection-string")))
                .containerName(environment.getProperty("azure.storage.container-name"))
                .blobName(fileName)
                .buildClient();
        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentType(ContentTypeUtil.getContentType(fileName));
        blobClient.upload(fileStream, fileStream.available(), true);
        blobClient.setHttpHeaders(headers);
        return blobClient.getBlobUrl();
    }
}
