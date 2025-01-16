package br.com.rplbasicmessages.usecases.builders;

import br.com.rplbasicmessages.entrypoints.dtos.responses.ProfileResponse;
import br.com.rplbasicmessages.gateways.entities.ProfileDocument;

public class AccountResponseBuilder extends GenericBuilder<ProfileDocument, ProfileResponse> {

    public AccountResponseBuilder(ProfileDocument dto, Class<ProfileResponse> entityClass) {
        super(dto, entityClass);
    }
}
