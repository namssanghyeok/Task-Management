package sparta.task.store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sparta.task.domain.model.UploadFile;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.filestore.LocalFileStore;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LocalFileStoreTest {
    @InjectMocks
    private LocalFileStore fileStore;

    @Test
    @DisplayName("파일 저장 - 성공")
    void save() {
        String originalFilename = "text.txt";
        String contentType = "text/plain";
        byte[] content = "Hello, World!".getBytes();
        MockMultipartFile file = new MockMultipartFile(originalFilename, originalFilename, contentType, content);
        // doNothing을 해야함. 지금은 파일이 진짜 생성되어버림

        // when
        UploadFile uploadFile = fileStore.save(file);

        // then
        assertThat(uploadFile.getOriginalFilename()).isEqualTo(originalFilename);
    }

    @Test
    @DisplayName("파일 저장 - transferTo 실패")
    void save_fail() throws IOException {
        // given
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.doThrow(IOException.class).when(file).transferTo(Mockito.any(File.class));

        // when
        assertThrows(HttpStatusException.class, () -> fileStore.save(file));

        try {
            fileStore.save(file);
        } catch (HttpStatusException e) {
            // then
            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}