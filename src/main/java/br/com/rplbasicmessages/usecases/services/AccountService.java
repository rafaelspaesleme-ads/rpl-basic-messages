package br.com.rplbasicmessages.usecases.services;

import br.com.rplbasicmessages.commons.exceptions.RplMessageUnauthorizedException;
import br.com.rplbasicmessages.entrypoints.dtos.requests.LoginRequest;
import br.com.rplbasicmessages.entrypoints.dtos.requests.ProfileRequest;
import br.com.rplbasicmessages.entrypoints.dtos.responses.LoginResponse;
import br.com.rplbasicmessages.entrypoints.dtos.responses.ProfileResponse;
import br.com.rplbasicmessages.gateways.AccountGateway;
import br.com.rplbasicmessages.gateways.entities.CredentialsDocument;
import br.com.rplbasicmessages.gateways.entities.LoginDocument;
import br.com.rplbasicmessages.gateways.entities.ProfileDocument;
import br.com.rplbasicmessages.usecases.builders.AccountRequestBuilder;
import br.com.rplbasicmessages.usecases.builders.AccountResponseBuilder;
import br.com.rplbasicmessages.usecases.builders.CredentialsRequestBuilder;
import br.com.rplbasicmessages.usecases.builders.LoginBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Getter
    @Value("${property.security.tokenPublic}")
    private String tokenPublic;

    private final AccountGateway gateway;

    public void isTokenPublic(String tokenPublic) {
        if (!Objects.equals(this.tokenPublic, tokenPublic)) {
            throw new RplMessageUnauthorizedException("Você não tem autorização para acessar este recurso.", this.getClass());
        }
    }

    @Transactional
    public void createAccount(ProfileRequest request) {
        CredentialsRequestBuilder credentialsRequestBuilder = getCredentialsRequestBuilder(request);

        CredentialsDocument credentials = credentialsRequestBuilder.build();
        gateway.createCredential(credentials);

        AccountRequestBuilder requestBuilder = getRequestBuilder(request);
        requestBuilder.setCredentialsDocument(credentials);

        gateway.createAccount(requestBuilder.build());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        CredentialsDocument credential = gateway.getCredential(request.getNickname());

        ProfileDocument profile = gateway.getAccount(credential.getNickname());

        LoginBuilder loginBuilder = this.getLoginBuilder(request);

        loginBuilder.setTokenPublic(this.tokenPublic);
        loginBuilder.setCredential(credential);
        loginBuilder.setProfile(profile);

        LoginDocument loginDocument = gateway.setLogin(loginBuilder.getLogin());

        loginBuilder.setLoginDocument(loginDocument);

        return loginBuilder.build();

    }

    public String getTokenLogin(String nickname) {
        return gateway.getTokenLogin(nickname);
    }

    @Transactional
    public boolean isLogoff(String nickname) {
        return gateway.isLogoff(nickname);
    }

    @Transactional
    public void setLogoff(String nickname) {
        gateway.setLogoff(nickname);
    }

    public void checkToken(String token) {
        gateway.checkToken(token);
    }

    public ProfileResponse getAccount(String nickname) {
        ProfileDocument profileDocument = gateway.getAccount(nickname);
        return getResponseBuilder(profileDocument).build();
    }

    @Transactional
    public void editAccount(ProfileRequest request) {
        AccountRequestBuilder requestBuilder = getRequestBuilder(request);
        gateway.editAccount(requestBuilder.build());
    }

    @Transactional
    public void deleteAccount(String id) {
        gateway.deleteAccount(id);
    }

    private LoginBuilder getLoginBuilder(LoginRequest request) {
        return new LoginBuilder(request, LoginResponse.class);
    }

    private CredentialsRequestBuilder getCredentialsRequestBuilder(ProfileRequest request) {
        return new CredentialsRequestBuilder(request, CredentialsDocument.class);
    }

    private AccountRequestBuilder getRequestBuilder(ProfileRequest request) {
        return new AccountRequestBuilder(request, ProfileDocument.class);
    }

    private AccountResponseBuilder getResponseBuilder(ProfileDocument document) {
        return new AccountResponseBuilder(document, ProfileResponse.class);
    }

}
