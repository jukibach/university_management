package fpt.com.universitymanagement.common;

import org.apache.commons.io.FilenameUtils;

import java.util.Map;
import java.util.Optional;

public class ContentTypeUtil {
    private static final Map<String, String> CONTENT_TYPES = Map.of(
            "pdf", "application/pdf",
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg"
    );
    
    private ContentTypeUtil() {
    }
    
    public static String getContentType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        return Optional.ofNullable(CONTENT_TYPES.get(extension))
                .orElse("application/octet-stream");
    }
}
