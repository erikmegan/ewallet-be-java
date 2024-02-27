package com.flip.assignment.web.controller;

import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.service.api.UserService;
import com.flip.assignment.service.model.BalanceServiceResponse;
import com.flip.assignment.service.model.RegisterUserRequest;
import com.flip.assignment.service.model.UserServiceResponse;
import com.flip.assignment.web.constant.ApiPath;
import com.flip.assignment.web.model.BalanceApiResponse;
import com.flip.assignment.web.model.UserApiRequest;
import com.flip.assignment.web.model.UserApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = ApiPath.CREATE_USER)
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserApiRequest userApiRequest){
        userService.registerUser(RegisterUserRequest.builder().username(userApiRequest.getUsername()).build());
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(StringUtils.EMPTY);
    }

    @GetMapping
    public UserApiResponse getUser(@RequestParam String token){
        UserServiceResponse serviceResponse = userService.getUserFromToken(token);
        return buildResponse(serviceResponse);
    }

    private UserApiResponse buildResponse (UserServiceResponse serviceResponse) {
        return UserApiResponse.builder()
            .username(serviceResponse.getUsername())
            .createdDate(serviceResponse.getCreatedDate())
            .build();
    }

    @GetMapping(value = ApiPath.BALANCE_READ)
    public BalanceApiResponse getBalance(
        @RequestHeader(name = CommonConstant.AUTHORIZED_HEADER) String token
    ){
        BalanceServiceResponse balanceServiceResponse = userService.getBalance(token);
        return buildResponse(balanceServiceResponse);
    }

    private BalanceApiResponse buildResponse (BalanceServiceResponse serviceResponse) {
        return BalanceApiResponse.builder()
                .balance(serviceResponse.getBalance())
                .build();
    }

    @GetMapping(value = ApiPath.GET_TOKEN)
    public String getTokenFromUsername(@RequestParam String username){
        String serviceResponse = userService.getTokenFromUsername(username);
        return serviceResponse;
    }
}
