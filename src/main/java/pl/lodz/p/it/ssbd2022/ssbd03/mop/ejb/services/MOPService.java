package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {
}
