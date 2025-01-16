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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doc_profile")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDocument {

    @Id
    @NotNull(message = "Id não pode ser nulo.")
    private String id;

    @Indexed(unique = true)
    @NotNull(message = "Nickname não pode ser nulo.")
    private String nickname;

    @NotNull(message = "Nickname não pode ser nulo.")
    private String password;

    @NotNull(message = "Nome não pode ser nulo.")
    private String name;

    @NotNull(message = "Data de nascimento não pode ser nulo.")
    private LocalDate birthDate;

    @NotNull(message = "Status não pode ser nulo.")
    private Boolean active;

    @NotNull(message = "Data de registro não pode ser nulo.")
    private LocalDateTime registerDate;

    private LocalDateTime updatedDate;

}
