package com.example.user.controller;

import com.example.user.controller.model.LoginRequest;
import com.example.user.controller.model.SignUpRequest;
import com.example.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@AllArgsConstructor
@RestController
public class UserController {
	private final UserService userService;

	@PostMapping("/v1/signUp")
	public String postSignUpV1(@RequestBody SignUpRequest signUp) {
		String msg = userService.registerUser(
				signUp.getUsername(),
				signUp.getPassword(),
				signUp.getEmail()
		);
		return "success " + msg;
	}

	@PostMapping("/v1/login")
	public String postLoginV1(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
		String msg = userService.loginRequest(
				loginRequest.getUsername(),
				loginRequest.getPassword()
		);
		return "success " + msg;
	}
}
