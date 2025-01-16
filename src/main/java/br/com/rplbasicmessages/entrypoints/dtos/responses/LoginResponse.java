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
public class LoginResponse {

    private String id;

    private String nickname;

    private String token;

    private LocalDateTime loginOn;

    private LocalDateTime loginOff;

    private String tokenPublic;
}
