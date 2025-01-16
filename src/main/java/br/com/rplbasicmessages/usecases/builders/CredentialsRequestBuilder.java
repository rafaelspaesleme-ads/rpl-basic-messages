package br.com.rplbasicmessages.usecases.builders;

import br.com.rplbasicmessages.commons.utils.SecretUtils;
import br.com.rplbasicmessages.entrypoints.dtos.requests.ProfileRequest;
import br.com.rplbasicmessages.gateways.entities.CredentialsDocument;

public class CredentialsRequestBuilder extends GenericBuilder<ProfileRequest, CredentialsDocument> {

    public CredentialsRequestBuilder(ProfileRequest dto, Class<CredentialsDocument> entityClass) {
        super(dto, entityClass);
    }

    @Override
    protected void mapSpecificFields() {
        String secretKey = SecretUtils.generateSecretKey();
        entity.setNickname(dto.getNickname());
        entity.setSecretKey(secretKey);
    }
}
