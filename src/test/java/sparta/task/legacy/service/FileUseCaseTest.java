package sparta.task.legacy.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class FileUseCaseTest {
//    @Mock
//    private UploadFileJpaRepository uploadFileJpaRepository;
//    @Mock
//    private UploadFileMapper uploadFileMapper;
//
//    @Mock
//    private LocalFileStore fileStore;
//
//    @InjectMocks
//    private FileService fileService;
//
//    @Test
//    @DisplayName("파일 업로드 - 성공")
//    void fileUploadTo_success() {
//        // given
//        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//        Mockito.when(mockFile.isEmpty()).thenReturn(false);
//        Mockito.when(mockFile.getOriginalFilename()).thenReturn("hello");
//        UploadFile uploadFile = UploadFile.builder().originalFilename("hello").filename("asdf").type("image/png").url("url").build();
//
//        Mockito.when(this.fileStore.save(Mockito.any(MultipartFile.class)))
//                .thenReturn(uploadFile);
//
//        Mockito.when(this.uploadFileMapper.toUploadFileResponseDto(Mockito.any(UploadFile.class)))
//                .thenReturn(UploadFileResponseDto.builder().originalFilename("hello").build());
//
//        Mockito.when(this.uploadFileJpaRepository.save(Mockito.any(UploadFile.class)))
//                .thenReturn(Mockito.mock(UploadFile.class));
//
//        // when
//        //UploadFileResponseDto res = this.fileService.fileUploadTo(1L, mockFile, currentUser);
//
//        // then
////        assertThat(res.getOriginalFilename()).isEqualTo("hello");
//    }
//
//    @Test
//    @DisplayName("파일 업로드 - 파일이 null이면 null을 리턴")
//    void fileUploadTo_null() {
//        // given
//        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//        Mockito.when(mockFile.isEmpty()).thenReturn(true);
//
//        // when & then
////        assertThat(fileService.fileUploadTo(1L, mockFile, currentUser)).isNull();
//    }
//
//    @Test
//    @DisplayName("파일 업로드 - 파일의 originalFilename이 null이면 400 예외")
//    void fileUploadTo_originalFilename_null() {
//        // given
//        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//        Mockito.when(mockFile.isEmpty()).thenReturn(false);
//        Mockito.when(mockFile.getOriginalFilename()).thenReturn(null);
//
//        try {
//            // when
////            assertThrows(HttpStatusException.class, () -> this.fileService.fileUploadTo(1L, mockFile, currentUser));
////            this.fileService.fileUploadTo(1L, mockFile, currentUser);
//        } catch (HttpStatusException e) {
//            // then assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @Test
//    @DisplayName("파일 업로드 - save 실패")
//    void fileUploadTo_saveFailed() {
//        // given
//        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//        Mockito.when(mockFile.isEmpty()).thenReturn(false);
//        Mockito.when(mockFile.getOriginalFilename()).thenReturn("hello");
//        Mockito.when(this.fileStore.save(Mockito.any(MultipartFile.class)))
//                .thenReturn(UploadFile.builder().originalFilename("hello").build());
//        Mockito.when(this.uploadFileJpaRepository.save(Mockito.any(UploadFile.class)))
//                .thenThrow(RuntimeException.class);
//
//        // when & then
//        try {
////            assertThrows(HttpStatusException.class, () -> this.fileService.fileUploadTo(1L, mockFile, currentUser));
////            this.fileService.fileUploadTo(1L, mockFile, currentUser);
//        } catch (HttpStatusException e) {
//            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Test
//    @DisplayName("파일 이름으로 찾기")
//    void getByFilename_success() {
//        // given
//        Mockito.when(this.uploadFileJpaRepository.findByFilename(Mockito.any(String.class)))
//                .thenReturn(Optional.of(Mockito.mock(UploadFile.class)));
//        // when
//        UploadFile foundFile = this.fileService.getByFilename("hello");
//
//        // then
//        assertThat(foundFile).isNotNull();
//    }
//
//    @Test
//    @DisplayName("파일 이름으로 찾기 - 없는 경우 404 예외")
//    void getByFilename_notFound() {
//        // given
//        Mockito.when(this.uploadFileJpaRepository.findByFilename(Mockito.any(String.class)))
//                .thenReturn(Optional.empty());
//
//        // when & then
//        try {
//            assertThrows(HttpStatusException.class, () -> this.fileService.getByFilename("hello"));
//            this.fileService.getByFilename("hello");
//        } catch (HttpStatusException e) {
//            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Test
//    @DisplayName("Resource로 변환 - 성공")
//    void getResource_success() throws MalformedURLException {
//        // given
//        String url = "https://hello.com/world.txt";
//        UploadFile uploadFile = UploadFile.builder().originalFilename("hello").url(url).build();
//
//        // when
//        Resource resource = this.fileService.getResource(uploadFile);
//
//        // then
//        assertThat(resource).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Resource 변환 - url 형식이 맞지 않은 경우 500 예외 발생")
//    void getResource_failed() {
//        String url = "aa";
//        UploadFile uploadFile = UploadFile.builder().originalFilename("hello").url(url).build();
//
//        // when & then
//        try {
//            assertThrows(HttpStatusException.class, () -> this.fileService.getResource(uploadFile));
//            this.fileService.getResource(uploadFile);
//        } catch (HttpStatusException e) {
//            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Test
//    @DisplayName("ZIP 다운로드 - 성공")
//    void getByteArrayResource_success() {
//        // given
//        List<UploadFile> attachments = new ArrayList<>();
//        // when
//        ByteArrayResource byteArrayResource = this.fileService.getByteArrayResource(attachments);
//        // then
//        assertThat(byteArrayResource).isNotNull();
//    }
//
//    @Test
//    @DisplayName("ZIP 다운로드 - 실패")
//    void getByteArrayResource_failed() {
//        // given
//        List<UploadFile> attachments = new ArrayList<>();
//        attachments.add(UploadFile.builder().originalFilename("hello").url("file://hahahoho").build());
//        // when & then
//        assertThrows(HttpStatusException.class, () -> this.fileService.getByteArrayResource(attachments));
//        // then
//        try {
//            this.fileService.getByteArrayResource(attachments);
//        } catch (HttpStatusException e) {
//            assertThat(e.getErrorCode().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Test
//    @DisplayName("파일 삭제")
//    void deleteById_success() {
//        // given
//        // when
//        // this.fileService.deleteById(1L);
//        // then
//        // Mockito.verify(this.uploadFileRepository).deleteById(1L);
//    }
}