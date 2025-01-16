package br.com.rplbasicmessages.usecases.services;

import br.com.rplbasicmessages.commons.enums.ETypeManipulationMessage;
import br.com.rplbasicmessages.commons.utils.ClassUtils;
import br.com.rplbasicmessages.entrypoints.dtos.requests.ChatRequest;
import br.com.rplbasicmessages.entrypoints.dtos.responses.ChatResponse;
import br.com.rplbasicmessages.gateways.MessagesGateway;
import br.com.rplbasicmessages.gateways.entities.ChatDocument;
import br.com.rplbasicmessages.usecases.builders.MessagesBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagesService {

    private final MessagesGateway gateway;

    @Transactional
    public List<ChatResponse> send(ChatRequest request) {
        MessagesBuilder builder = this.getBuilder(request);

        List<ChatDocument> historicMessages = gateway.send(builder.getDocument());

        builder.setHistoricMessages(historicMessages);

        return builder.build();

    }

    @Transactional
    public List<ChatResponse> edit(ChatRequest request) {
        MessagesBuilder builder = this.getBuilder(request);

        List<ChatDocument> historicMessages = gateway.editMessage(builder.getDocument());

        builder.setHistoricMessages(historicMessages);

        return builder.build();

    }

    @Transactional
    public List<ChatResponse> manipulate(String id, ETypeManipulationMessage action) {
        MessagesBuilder builder = this.getBuilder();

        List<ChatDocument> historicMessages = gateway.manipulateMessage(id, action);

        builder.setHistoricMessages(historicMessages);

        return builder.build();

    }

    @Transactional
    public List<ChatResponse> deleteMessage(String id) {
        MessagesBuilder builder = this.getBuilder();

        List<ChatDocument> historicMessages = gateway.deleteMessage(id);

        builder.setHistoricMessages(historicMessages);

        return builder.build();
    }

    public List<ChatResponse> getMessagesActive(String nickname, String nicknameDestination) {
        MessagesBuilder builder = this.getBuilder();

        List<ChatDocument> historicMessages = gateway.getMessagesActive(nickname, nicknameDestination);

        builder.setHistoricMessages(historicMessages);

        return builder.build();
    }

    public List<ChatResponse> getArchiveMessages(String nickname, String nicknameDestination) {
        MessagesBuilder builder = this.getBuilder();

        List<ChatDocument> historicMessages = gateway.getArchiveMessages(nickname, nicknameDestination);

        builder.setHistoricMessages(historicMessages);

        return builder.build();
    }

    public List<ChatResponse> getMessages(String nickname, String nicknameDestination) {
        MessagesBuilder builder = this.getBuilder();

        List<ChatDocument> historicMessages = gateway.getMessages(nickname, nicknameDestination, null);

        builder.setHistoricMessages(historicMessages);

        return builder.build();
    }

    private MessagesBuilder getBuilder(ChatRequest request) {
        return new MessagesBuilder(request, ClassUtils.getClassList());
    }

    private MessagesBuilder getBuilder() {
        return new MessagesBuilder(ClassUtils.getClassList());
    }

}
