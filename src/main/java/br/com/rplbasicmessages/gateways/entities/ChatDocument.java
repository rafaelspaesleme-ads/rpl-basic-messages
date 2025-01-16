package br.com.rplbasicmessages.gateways.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doc_chat")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDocument {

    @Id
    @NotNull(message = "Id não pode ser nulo.")
    private String id;

    @Indexed
    @NotNull(message = "Nickname não pode ser nulo.")
    private String nickname;

    @Indexed
    @NotNull(message = "Nickname de destino não pode ser nulo.")
    private String nicknameDestination;

    @NotNull(message = "Mensagem não pode ser nulo.")
    private String message;

    @NotNull(message = "Data e hora de envio não pode ser nulo.")
    private LocalDateTime datetimeSendMessage;

    private LocalDateTime datetimeEditedMessage;

    private Boolean fixed;

    private Boolean view;

    private Boolean edited;

    @NotNull(message = "Status não pode ser nulo.")
    private Boolean active;

}
