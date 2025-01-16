package br.com.rplbasicmessages.usecases.builders;

import br.com.rplbasicmessages.entrypoints.dtos.requests.ChatRequest;
import br.com.rplbasicmessages.entrypoints.dtos.responses.ChatResponse;
import br.com.rplbasicmessages.gateways.entities.ChatDocument;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class MessagesBuilder extends GenericBuilder<ChatRequest, List<ChatResponse>> {

    private ChatDocument chatDocument;

    private List<ChatDocument> historicMessages = new ArrayList<>();

    public MessagesBuilder(Class<List<ChatResponse>> entityClass) {
        super(null, entityClass);
    }

    public MessagesBuilder(ChatRequest dto, Class<List<ChatResponse>> entityClass) {
        super(dto, entityClass);
    }

    public ChatDocument getDocument() {
        if (Objects.isNull(dto)) return null;

        return ChatDocument.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .nicknameDestination(dto.getNicknameDestination())
                .message(dto.getMessage())
                .datetimeSendMessage(dto.getDatetimeSendMessage())
                .datetimeEditedMessage(dto.getDatetimeEditedMessage())
                .fixed(dto.getFixed())
                .view(dto.getView())
                .edited(dto.getEdited())
                .active(dto.getActive())
                .build();
    }

    @Override
    protected void mapSpecificFields() {
        historicMessages.forEach(document -> entity.add(ChatResponse.builder()
                        .id(document.getId())
                        .nickname(document.getNickname())
                        .nicknameDestination(document.getNicknameDestination())
                        .message(document.getMessage())
                        .datetimeSendMessage(document.getDatetimeSendMessage())
                        .datetimeEditedMessage(document.getDatetimeEditedMessage())
                        .fixed(document.getFixed())
                        .view(document.getView())
                        .edited(document.getEdited())
                        .active(document.getActive())
                        .input(document.getInput())
                        .output(document.getOutput())
                .build()));
    }
}
