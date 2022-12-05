package pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel.CONSTRAINT_ACCESS_LEVEL_FOR_ACCOUNT_UNIQUE;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(
        name = "access_level",
        indexes = {@Index(name = "access_level_account_id", columnList = "account_id")},
        uniqueConstraints = {
                @UniqueConstraint(
                        name = CONSTRAINT_ACCESS_LEVEL_FOR_ACCOUNT_UNIQUE, columnNames = {"account_id", "access_level"})}
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "select a from AccessLevel a"),
        @NamedQuery(name = "AccessLevel.findByLogin", query = "select a from AccessLevel a where a.account.login = :login"),
        @NamedQuery(name = "AccessLevel.findById", query = "select a from AccessLevel a where a.id = :id"),
        @NamedQuery(name = "AccessLevel.findByAccountId", query = "select a from AccessLevel a where a.account.id = :id"),
        @NamedQuery(name = "AccessLevel.findByAccessLevel", query = "select a from AccessLevel a WHERE a.level = :level"),
})
@ToString(callSuper = true)
public class AccessLevel extends AbstractEntity implements Serializable {

    public static final String CONSTRAINT_ACCESS_LEVEL_FOR_ACCOUNT_UNIQUE = "constraint_unique_access_level_for_account";
    private static final long serialVersionUID = 1L;

    @Column(name = "access_level", insertable = false, updatable = false, length = 20)
    @Getter
    private String level;

    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @Getter
    @Setter
    private boolean active = true;

    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    @ToString.Exclude
    private Account account;

}