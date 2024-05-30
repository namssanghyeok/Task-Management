package sparta.task.presentation.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.task.application.dto.request.SignupUserRequestDto;
import sparta.task.application.usecase.UserUseCase;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    @PostMapping
    ResponseEntity<?> signup(@Valid @RequestBody SignupUserRequestDto requestDto) {
        return ResponseEntity.ok(this.userUseCase.signup(requestDto));
    }
}
