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

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doc_login")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull(message = "Id não pode ser nulo.")
    private String id;

    @Indexed
    @NotNull(message = "Nickname não pode ser nulo.")
    private String nickname;

    @Indexed(unique = true)
    @NotNull(message = "Token não pode ser nulo.")
    private String token;

    private LocalDateTime loginOn;

    private LocalDateTime loginOff;

}
