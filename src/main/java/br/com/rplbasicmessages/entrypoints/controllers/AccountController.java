package br.com.rplbasicmessages.entrypoints.controllers;

import br.com.rplbasicmessages.entrypoints.dtos.requests.LoginRequest;
import br.com.rplbasicmessages.entrypoints.dtos.requests.ProfileRequest;
import br.com.rplbasicmessages.entrypoints.dtos.responses.LoginResponse;
import br.com.rplbasicmessages.entrypoints.dtos.responses.ProfileResponse;
import br.com.rplbasicmessages.usecases.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account")
public class AccountController {

    private final AccountService service;

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<Void> createAccount(
            @RequestHeader(value = "tokenPublic") String tokenPublic,
            @RequestBody ProfileRequest body) {

        service.isTokenPublic(tokenPublic);

        service.createAccount(body);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CrossOrigin
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(
            @RequestHeader(value = "tokenPublic") String tokenPublic,
            @RequestBody LoginRequest body) {

        service.isTokenPublic(tokenPublic);

        LoginResponse response = service.login(body);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/check/logoff/{nickname}")
    public ResponseEntity<Boolean> isLogoff(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String nickname) {

        service.checkToken(tokenPrivate);

        Boolean response = service.isLogoff(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/logoff/{nickname}")
    public ResponseEntity<Void> logoff(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String nickname) {

        service.checkToken(tokenPrivate);

        service.setLogoff(nickname);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @CrossOrigin
    @GetMapping(value = "/{nickname}")
    public ResponseEntity<ProfileResponse> getAccount(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String nickname) {

        service.checkToken(tokenPrivate);

        ProfileResponse response = service.getAccount(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PutMapping(value = "/edit")
    public ResponseEntity<Void> editAccount(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @RequestBody ProfileRequest body) {

        service.checkToken(tokenPrivate);

        service.editAccount(body);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String accountId) {

        service.checkToken(tokenPrivate);

        service.deleteAccount(accountId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
