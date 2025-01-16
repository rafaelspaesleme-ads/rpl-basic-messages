package br.com.rplbasicmessages.entrypoints.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileRequest {

    private String id;

    private String nickname;

    private String password;

    private String name;

    private LocalDate birthDate;

    private Boolean active;
}
