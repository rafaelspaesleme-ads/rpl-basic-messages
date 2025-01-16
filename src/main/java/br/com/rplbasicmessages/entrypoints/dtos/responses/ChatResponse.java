package br.com.rplbasicmessages.entrypoints.dtos.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatResponse {

    private String id;

    private String nickname;

    private String nicknameDestination;

    private String message;

    private LocalDateTime datetimeSendMessage;

    private LocalDateTime datetimeEditedMessage;

    private Boolean fixed;

    private Boolean view;

    private Boolean edited;

    private Boolean active;

}
