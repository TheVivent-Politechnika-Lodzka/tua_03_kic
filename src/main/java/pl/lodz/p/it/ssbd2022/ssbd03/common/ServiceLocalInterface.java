package pl.lodz.p.it.ssbd2022.ssbd03.common;

import javax.ejb.Local;

@Local
public interface ServiceLocalInterface {
    boolean isLastTransactionCommited();
}
