package sparta.task.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.validation.annotation.File;

import java.util.List;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {
    private static final List<String> VALID_FILE_TYPES = List.of(
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE
    );

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return false;
        }
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        for (String validFileType : VALID_FILE_TYPES) {
            if (contentType.contains(validFileType)) {
                return true;
            }
        }
        return false;
    }
}
