package uk.gov.di.gpg45engine.services;

import uk.gov.di.gpg45engine.domain.data.IdentityVerificationBundle;
import uk.gov.di.gpg45engine.domain.gpg45.IdentityProfile;

public interface Gpg45Service {

    public IdentityProfile calculate(IdentityVerificationBundle bundle);
}
