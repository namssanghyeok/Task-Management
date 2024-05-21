package sparta.task.validation.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class FileValidatorTest {
    private FileValidator fileValidator;

    @BeforeAll
    void beforeAll() {
        this.fileValidator = new FileValidator();
    }

    @Test
    @DisplayName("validation - 통과")
    void isValid_success() {
        // given
        String contentType = MediaType.IMAGE_PNG_VALUE;
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", contentType, "Hello World".getBytes());
        // when
        boolean valid = fileValidator.isValid(file, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    @DisplayName("validation - 이미지 타입이 아니면 false")
    void isValid_notImage() {
        // given
        String contentType = MediaType.TEXT_HTML_VALUE;
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", contentType, "Hello World".getBytes());
        // when
        boolean valid = fileValidator.isValid(file, null);
        // then
        assertThat(valid).isFalse();
    }

    @Test
    @DisplayName("validation - 이미지 타입이 null 이면 false")
    void isValid_null() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", null, "Hello World".getBytes());
        // when
        boolean valid = fileValidator.isValid(file, null);
        // then
        assertThat(valid).isFalse();
    }
}