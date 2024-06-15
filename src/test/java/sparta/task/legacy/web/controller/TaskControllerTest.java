//package sparta.task.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import sparta.task.dto.request.CreateTaskRequestDto;
//import sparta.task.dto.request.UpdateTaskRequestDto;
//import sparta.task.dto.response.TaskResponseDto;
//import sparta.task.dto.response.UploadFileResponseDto;
//import sparta.task.model.Task;
//import sparta.task.model.UploadFile;
//import sparta.task.service.FileService;
//import sparta.task.service.TaskService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//@WebMvcTest(TaskController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//class TaskControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private TaskService taskService;
//    @MockBean
//    private FileService fileService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Task task;
//    private TaskResponseDto taskResponseDto;
//    private CreateTaskRequestDto createTaskRequestDto;
//    private UpdateTaskRequestDto updateTaskRequestDto;
//
//    @BeforeEach
//    public void beforeEach() {
//        createTaskRequestDto = CreateTaskRequestDto.builder().title("title").content("content").password("1234").build();
//        task = Task.builder().id((long) 1).title("title").content("content").password("1234").build();
//        taskResponseDto = TaskResponseDto.builder().id((long) 1).title("title").content("content").build();
//        updateTaskRequestDto = UpdateTaskRequestDto.builder().title("hello").password("1234").build();
//    }
//
//
//    @Test
//    @DisplayName("task 생성 - 201 status code")
//    void createTask_success() throws Exception {
////        Mockito.when(this.taskService.createTask(Mockito.mock(CreateTaskRequestDto.class)))
////                .thenAnswer(InvocationOnMock::getArguments);
//        Mockito.when(this.taskService.createTask(Mockito.mock(CreateTaskRequestDto.class)))
//                .thenReturn(taskResponseDto);
//
//        ResultActions response = mockMvc.perform(post("/api/task")
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .content(objectMapper.writeValueAsString(createTaskRequestDto)));
//
//        response.andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print());
//        // 이런것도 할 수 있음
//        // .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(createTaskRequestDto.getAssignee())))
//    }
//
//    @Test
//    @DisplayName("선택한 task 조회 - 200 status code")
//    void showTaskById_success() throws Exception {
//        Mockito.when(this.taskService.showTaskById(Mockito.anyLong()))
//                .thenReturn(taskResponseDto);
//        ResultActions response = mockMvc.perform(get("/api/task/11"));
//
//        response.andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(taskResponseDto)))
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("모든 task 조회 - 200 status code")
//    void showAllTasks_success() throws Exception {
//
//        List<TaskResponseDto> list = new ArrayList<>();
//        Mockito.when(this.taskService.showAll())
//                .thenReturn(list);
//        ResultActions response = mockMvc.perform(get("/api/task"));
//
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    @DisplayName("task 수정 - 200 status code")
//    void updateTask_success() throws Exception {
//        Mockito.when(this.taskService.updateTaskBy(Mockito.anyLong(), Mockito.any(UpdateTaskRequestDto.class)))
//                .thenReturn(taskResponseDto);
//
//        ResultActions response = mockMvc.perform(put("/api/task/11")
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .content(objectMapper.writeValueAsString(updateTaskRequestDto))
//        );
//
//        response.andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(taskResponseDto)))
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("파일 첨부")
//    void uploadFile_success() throws Exception {
//        byte[] fileBytes = objectMapper.writeValueAsBytes(task);
//        MockMultipartFile file = new MockMultipartFile("file", "originalName", MediaType.IMAGE_PNG_VALUE, fileBytes);
//        Mockito.when(this.taskService.findByIdAndCheckPassword(Mockito.anyLong(), Mockito.anyString()))
//                .thenReturn(task);
//        Mockito.when(this.fileService.fileUploadTo(task.getId(), file))
//                .thenReturn(Mockito.mock(UploadFileResponseDto.class));
//
//        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/task/{id}/attachment/upload", 1L)
//                .file(file)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .param("password", "1234")
//                .characterEncoding("UTF-8")
//        );
//
//        response.andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @Test
//    @DisplayName("task에 upload 파일 조회")
//    void showFiles_success() throws Exception {
//        List<UploadFileResponseDto> files = new ArrayList<>();
//        Mockito.when(this.taskService.findUploadFilesByTaskId(Mockito.anyLong()))
//                .thenReturn(files);
//        ResultActions response = mockMvc.perform(get("/api/task/{id}/attachment", 1L));
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    @DisplayName("task에 upload 된 파일 다운로드 - 실패")
//    void downloadAllAttachments_failed() throws Exception {
//        Mockito.when(this.taskService.findById(Mockito.anyLong()))
//                .thenReturn(task);
//        ResultActions response = mockMvc.perform(get("/api/task/{id}/attachment/download", 1L));
//        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("task에 upload 된 파일 다운로드 - 성공")
//    void downloadAllAttachments_success() throws Exception {
//        Task mockTask = Mockito.mock(Task.class);
//        List<UploadFile> mockAttachments = Mockito.mock(List.class);
//        mockAttachments.add(Mockito.mock(UploadFile.class));
//
//        Mockito.when(this.taskService.findById(Mockito.anyLong()))
//                .thenReturn(mockTask);
//
//        Mockito.when(mockTask.getAttachments())
//                .thenReturn(mockAttachments);
//
//        Mockito.when(this.fileService.getByteArrayResource((List<UploadFile>) Mockito.mock(List.class)))
//                .thenReturn(Mockito.mock(ByteArrayResource.class));
//
//        ResultActions response = mockMvc.perform(get("/api/task/{id}/attachment/download", 1L));
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}
