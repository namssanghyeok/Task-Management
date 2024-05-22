package sparta.task.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.task.dto.SignupUserRequestDto;
import sparta.task.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    ResponseEntity<?> signup(@Valid @RequestBody SignupUserRequestDto requestDto) {
        return ResponseEntity.ok(this.userService.signup(requestDto));
    }
}
