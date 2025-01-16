package br.com.rplbasicmessages.gateways;

import br.com.rplbasicmessages.commons.exceptions.RplMessageBusinessException;
import br.com.rplbasicmessages.commons.exceptions.RplMessageNotFoundException;
import br.com.rplbasicmessages.commons.exceptions.RplMessageUnauthorizedException;
import br.com.rplbasicmessages.commons.utils.DateTimeUtils;
import br.com.rplbasicmessages.commons.utils.SecretUtils;
import br.com.rplbasicmessages.gateways.entities.CredentialsDocument;
import br.com.rplbasicmessages.gateways.entities.LoginDocument;
import br.com.rplbasicmessages.gateways.entities.ProfileDocument;
import br.com.rplbasicmessages.gateways.repositories.ChatRepository;
import br.com.rplbasicmessages.gateways.repositories.CredentialsRepository;
import br.com.rplbasicmessages.gateways.repositories.LoginRepository;
import br.com.rplbasicmessages.gateways.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountGateway {

    @Value("${property.security.timelogoff}")
    private Long timeLogoff;

    private final ProfileRepository profileRepository;

    private final CredentialsRepository credentialsRepository;

    private final LoginRepository loginRepository;

    private final ChatRepository chatRepository;

    @Transactional
    public void createAccount(ProfileDocument document) {
        try {

            if (chatRepository.existsByNickname(document.getNickname()) || chatRepository.existsByNicknameDestination(document.getNickname())) {
                throw new RplMessageBusinessException("Não é permitida a criação de conta com este nickname.", this.getClass());
            }

            document.setRegisterDate(LocalDateTime.now());
            profileRepository.save(document);
        } catch (Exception e) {
            throw new RplMessageBusinessException(Optional.ofNullable(e.getMessage()).orElse("Não foi possivel criar conta."), e.getCause(), this.getClass());
        }
    }

    public ProfileDocument getAccount(String nickname) {
        return profileRepository.findByNicknameAndActive(nickname, true)
                .orElseThrow(() -> new RplMessageNotFoundException("Conta não encontrada.", this.getClass()));
    }

    @Transactional
    public void editAccount(ProfileDocument document) {

        profileRepository.findById(document.getId())
                .ifPresentOrElse(profileDocument -> {

                    Optional.ofNullable(document.getName())
                            .ifPresent(profileDocument::setName);

                    Optional.ofNullable(document.getNickname())
                            .ifPresent(profileDocument::setNickname);

                    Optional.ofNullable(document.getPassword())
                            .ifPresent(profileDocument::setPassword);

                    Optional.ofNullable(document.getBirthDate())
                            .ifPresent(profileDocument::setBirthDate);

                    Optional.ofNullable(document.getActive())
                            .ifPresent(profileDocument::setActive);

                    profileDocument.setUpdatedDate(LocalDateTime.now());

                    if (!Objects.equals(profileDocument.getNickname(), document.getNickname()) &&
                            (chatRepository.existsByNickname(document.getNickname()) || chatRepository.existsByNicknameDestination(document.getNickname()))) {
                        throw new RplMessageBusinessException("Não é permitida a criação de conta com este nickname.", this.getClass());
                    }

                    profileRepository.save(profileDocument);

                }, () -> {
                    throw new RplMessageNotFoundException("Conta não existente.", this.getClass());
                });
    }

    @Transactional
    public void deleteAccount(String id) {
        try {
            profileRepository.findById(id)
                    .ifPresent(document -> {
                        CredentialsDocument credential = getCredential(document.getNickname());
                        credentialsRepository.delete(credential);
                        profileRepository.delete(document);
                    });
        } catch (Exception e) {
            throw new RplMessageBusinessException("Não foi possivel deletar sua conta.", e.getCause(), this.getClass());
        }
    }

    public void createCredential(CredentialsDocument document) {
        try {

            Optional<CredentialsDocument> doctumentOptional = credentialsRepository.findByNickname(document.getNickname());

            if (doctumentOptional.isPresent()) {
                throw new RplMessageBusinessException("Credencial já existe para esse usuário.", this.getClass());
            }

            document.setRegisterDate(LocalDateTime.now());
            document.setActive(true);
            credentialsRepository.save(document);
        } catch (Exception e) {
            throw new RplMessageBusinessException(Optional.ofNullable(e.getMessage()).orElse("Não foi possivel criar credencial de acesso."), e.getCause(), this.getClass());
        }
    }

    public CredentialsDocument getCredential(String nickname) {
        return credentialsRepository.findByNicknameAndActive(nickname, true)
                .orElseThrow(() -> new RplMessageNotFoundException("Não existe credencial para este usuário.", this.getClass()));
    }

    public CredentialsDocument getCredentialWithoutThrows(String nickname) {
        return credentialsRepository.findByNicknameAndActive(nickname, true)
                .orElse(null);
    }

    public void deleteCredential(CredentialsDocument document) {
        credentialsRepository.delete(document);
    }

    public LoginDocument setLogin(LoginDocument loginDocument) {
        try {
            loginDocument.setLoginOn(LocalDateTime.now());
            loginDocument.setLoginOff(LocalDateTime.now().plusMinutes(this.timeLogoff));
            loginDocument.setToken(SecretUtils.generateObjectHash(loginDocument));
            return loginRepository.save(loginDocument);
        } catch (Exception e) {
            throw new RplMessageBusinessException("Erro ao realizar login.", e.getCause(), this.getClass());
        }
    }

    public String getTokenLogin(String nickname) {
        LoginDocument loginDocument = loginRepository.findByNickname(nickname)
                .orElseThrow(() -> new RplMessageNotFoundException("Usuário não esta logado.", this.getClass()));

        return loginDocument.getToken();

    }

    public void checkToken(String token) {
        loginRepository.findByToken(token)
                .orElseThrow(() -> new RplMessageUnauthorizedException("Você não tem autorização para acessar este recurso.", this.getClass()));
    }

    public boolean isLogoff(String nickname) {
        LoginDocument loginDocument = loginRepository.findByNickname(nickname)
                .orElseThrow(() -> new RplMessageNotFoundException("Usuário não esta logado.", this.getClass()));

        LocalDateTime loginOn = loginDocument.getLoginOn();
        LocalDateTime loginOff = loginDocument.getLoginOff();

        boolean durationExceeded = DateTimeUtils.isDurationExceeded(loginOn, loginOff, this.timeLogoff);

        if (durationExceeded) {
            loginRepository.delete(loginDocument);
            return true;
        }

        return false;
    }

    public void setLogoff(String nickname) {
        try {
            LoginDocument loginDocument = loginRepository.findByNickname(nickname)
                    .orElseThrow(() -> new RplMessageNotFoundException("Usuário não esta logado.", this.getClass()));

            loginRepository.delete(loginDocument);
        } catch (Exception e) {
            throw new RplMessageBusinessException(Optional.ofNullable(e.getMessage()).orElse("Não foi possivel realizar logoff."), e.getCause(), this.getClass());
        }
    }

}
