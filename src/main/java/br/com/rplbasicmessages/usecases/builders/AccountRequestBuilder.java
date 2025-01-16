package br.com.rplbasicmessages.usecases.builders;

import br.com.rplbasicmessages.commons.utils.SecretUtils;
import br.com.rplbasicmessages.entrypoints.dtos.requests.ProfileRequest;
import br.com.rplbasicmessages.gateways.entities.CredentialsDocument;
import br.com.rplbasicmessages.gateways.entities.ProfileDocument;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountRequestBuilder extends GenericBuilder<ProfileRequest, ProfileDocument> {

    private CredentialsDocument credentialsDocument;

    public AccountRequestBuilder(ProfileRequest dto, Class<ProfileDocument> entityClass) {
        super(dto, entityClass);
    }

    @Override
    protected void mapSpecificFields() {
        super.mapSpecificFields();
        dto.setPassword(SecretUtils.encrypt(dto.getPassword(), credentialsDocument.getSecretKey()));
        entity.setPassword(dto.getPassword());
    }
}
