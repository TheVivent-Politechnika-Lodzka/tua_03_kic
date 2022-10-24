package pl.lodz.p.it.ssbd2022.ssbd03.security;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.IdentityStore;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:jboss/jdbc/ssbd03auth",
        callerQuery = "SELECT DISTINCT password FROM auth_view WHERE login = ?",
        groupsQuery = "SELECT access_level FROM auth_view WHERE login = ?",
        hashAlgorithm = HashAlgorithm.class
)
@ApplicationScoped
public class AuthenticationIdentityStore implements IdentityStore {

    private static final long serialVersionUID = 1L;
}
