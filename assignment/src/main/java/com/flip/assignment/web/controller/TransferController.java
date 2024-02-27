package com.flip.assignment.web.controller;

import com.flip.assignment.service.api.TransactionHistoryService;
import com.flip.assignment.service.model.TopUpBalanceServiceRequest;
import com.flip.assignment.service.model.TopTransactionUserServiceResponse;
import com.flip.assignment.service.model.TopUserServiceResponse;
import com.flip.assignment.service.model.TransactionServiceRequest;
import com.flip.assignment.web.constant.ApiPath;
import com.flip.assignment.web.model.TopUpBalanceApiRequest;
import com.flip.assignment.web.model.TopTransactionUserApiResponse;
import com.flip.assignment.web.model.TopUserApiResponse;
import com.flip.assignment.web.model.TransferApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.TRANSACTION)
@RequiredArgsConstructor
public class TransferController {

  private final TransactionHistoryService transactionHistoryService;

  @PostMapping(value = ApiPath.TRANSFER)
  public ResponseEntity<String> transfer(@RequestBody TransferApiRequest transferApiRequest,
                         @RequestHeader(name = "Authorization") String token) {
    transactionHistoryService.transfer(TransactionServiceRequest.builder().amount(transferApiRequest.getAmount())
            .from(token)
            .to(transferApiRequest.getToUsername())
        .build());
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .contentType(MediaType.APPLICATION_JSON)
        .body("Transfer success");
  }


  @PostMapping(value = ApiPath.BALANCE_TOPUP)
  public ResponseEntity<String> topUpBalance(@RequestBody TopUpBalanceApiRequest topUpBalanceApiRequest,
                             @RequestHeader(name = "Authorization") String token){

    transactionHistoryService.topUpBalance(TopUpBalanceServiceRequest.builder().amount(topUpBalanceApiRequest.getAmount()).token(token).build());
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .contentType(MediaType.APPLICATION_JSON)
        .body("Topup successful");
  }

  @GetMapping(value = ApiPath.TOP_TRANSACTIONS_PER_USER)
  public List<TopTransactionUserApiResponse> topTransactionUsers(@RequestHeader(name = "Authorization") String token){
      List<TopTransactionUserServiceResponse> resp = transactionHistoryService.topTransactionUsers(token);
      return resp.stream().map(this::buildResponse).collect(Collectors.toList());
  }

  private TopTransactionUserApiResponse buildResponse (TopTransactionUserServiceResponse serviceResponse) {
    return TopTransactionUserApiResponse.builder()
            .username(serviceResponse.getUsername())
            .amount(serviceResponse.getAmount())
            .type(serviceResponse.getType())
            .build();

  }

  @GetMapping(value = ApiPath.TOP_USERS)
  public List<TopUserApiResponse> getTopUsers(@RequestHeader(name = "Authorization") String token) {
    List<TopUserServiceResponse> resp = transactionHistoryService.topUsers(token);
    return resp
        .stream()
        .map(this::buildResponse).collect(Collectors.toList());
  }

  private TopUserApiResponse buildResponse (TopUserServiceResponse serviceResponse) {
    return TopUserApiResponse.builder()
            .username(serviceResponse.getUsername())
            .transactedValue(serviceResponse.getTransactedValue())
            .build();

  }

}
