package br.com.rplbasicmessages.entrypoints.controllers;

import br.com.rplbasicmessages.commons.enums.ETypeManipulationMessage;
import br.com.rplbasicmessages.entrypoints.dtos.requests.ChatRequest;
import br.com.rplbasicmessages.entrypoints.dtos.responses.ChatResponse;
import br.com.rplbasicmessages.usecases.services.AccountService;
import br.com.rplbasicmessages.usecases.services.MessagesService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/messages")
public class MessagesController {

    private final AccountService accountService;

    private final MessagesService service;

    @CrossOrigin
    @PostMapping(value = "/send")
    public ResponseEntity<List<ChatResponse>> send(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @RequestBody ChatRequest body) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.send(body);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PutMapping(value = "/edit")
    public ResponseEntity<List<ChatResponse>> edit(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @RequestBody ChatRequest body) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.edit(body);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/unarchive/{messageId}")
    public ResponseEntity<List<ChatResponse>> unarchive(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String messageId) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.manipulate(messageId, ETypeManipulationMessage.UNARCHIVE);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/archive/{messageId}")
    public ResponseEntity<List<ChatResponse>> archive(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String messageId) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.manipulate(messageId, ETypeManipulationMessage.ARCHIVE);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/view/{messageId}")
    public ResponseEntity<List<ChatResponse>> view(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String messageId) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.manipulate(messageId, ETypeManipulationMessage.VIEW);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/fixed/{messageId}")
    public ResponseEntity<List<ChatResponse>> fixed(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String messageId) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.manipulate(messageId, ETypeManipulationMessage.FIXED);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @PatchMapping(value = "/edited/{messageId}")
    public ResponseEntity<List<ChatResponse>> edited(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String messageId) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.manipulate(messageId, ETypeManipulationMessage.EDITED);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{messageId}")
    public ResponseEntity<List<ChatResponse>> delete(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @PathVariable String messageId) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.deleteMessage(messageId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @GetMapping(value = "/actives")
    public ResponseEntity<List<ChatResponse>> getMessagesActive(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "nicknameDestination") String nicknameDestination) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.getMessagesActive(nickname, nicknameDestination);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @GetMapping(value = "/archives")
    public ResponseEntity<List<ChatResponse>> getArchiveMessages(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "nicknameDestination") String nicknameDestination) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.getArchiveMessages(nickname, nicknameDestination);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @GetMapping(value = "")
    public ResponseEntity<List<ChatResponse>> getMessages(
            @RequestHeader(value = "tokenPrivate") String tokenPrivate,
            @RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "nicknameDestination") String nicknameDestination) {

        accountService.checkToken(tokenPrivate);

        List<ChatResponse> response = service.getMessages(nickname, nicknameDestination);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
