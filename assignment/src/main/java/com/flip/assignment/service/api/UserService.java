package com.flip.assignment.service.api;

import com.flip.assignment.service.model.BalanceServiceResponse;
import com.flip.assignment.service.model.RegisterUserRequest;
import com.flip.assignment.service.model.UserServiceResponse;

public interface UserService {

    public String registerUser(RegisterUserRequest registerUserRequest);

    public UserServiceResponse getUserFromToken(String token);
    public String getTokenFromUsername(String username);

    public BalanceServiceResponse getBalance(String token);



}
