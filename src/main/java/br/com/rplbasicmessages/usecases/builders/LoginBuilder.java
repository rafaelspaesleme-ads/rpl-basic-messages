package br.com.rplbasicmessages.usecases.builders;

import br.com.rplbasicmessages.commons.exceptions.RplMessageUnauthorizedException;
import br.com.rplbasicmessages.commons.utils.SecretUtils;
import br.com.rplbasicmessages.entrypoints.dtos.requests.LoginRequest;
import br.com.rplbasicmessages.entrypoints.dtos.responses.LoginResponse;
import br.com.rplbasicmessages.gateways.entities.CredentialsDocument;
import br.com.rplbasicmessages.gateways.entities.LoginDocument;
import br.com.rplbasicmessages.gateways.entities.ProfileDocument;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginBuilder extends GenericBuilder<LoginRequest, LoginResponse> {

    private String tokenPublic;

    private CredentialsDocument credential;

    private ProfileDocument profile;

    private LoginDocument loginDocument;

    public LoginBuilder(LoginRequest dto, Class<LoginResponse> entityClass) {
        super(dto, entityClass);
    }

    public LoginDocument getLogin() {
        return LoginDocument.builder()
                .nickname(profile.getNickname())
                .token(profile.getPassword())
                .build();
    }

    @Override
    protected void mapSpecificFields() {
        boolean passOk = SecretUtils.isPassOk(dto.getPassword(), profile.getPassword(), credential.getSecretKey());

        if (passOk) {
            entity.setId(loginDocument.getId());
            entity.setNickname(loginDocument.getNickname());
            entity.setTokenPublic(this.tokenPublic);
            entity.setToken(loginDocument.getToken());
            entity.setLoginOn(loginDocument.getLoginOn());
            entity.setLoginOff(loginDocument.getLoginOff());
            return;
        }

        throw new RplMessageUnauthorizedException("Você não esta autorizado a realizar este login", this.getClass());
    }
}
