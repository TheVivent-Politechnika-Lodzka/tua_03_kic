package pl.lodz.p.it.ssbd2022.ssbd03.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "access_level")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "SELECT a FROM AccessLevel a"),
        @NamedQuery(name = "AccessLevel.findByLogin", query = "SELECT a FROM AccessLevel a WHERE a.account.login = :login"),
//        @NamedQuery(name = "AccessLevel.findById", query = "SELECT a FROM AccessLevel a WHERE a.id = :id"),
//        @NamedQuery(name = "AccessLevel.findByLevel", query = "select a from AccessLevel a order by a.level"),
//        @NamedQuery(name = "AccessLevel.findByActive", query = "select a from AccessLevel a order by a.active"),
//        @NamedQuery(name = "AccessLevel.findByVersion", query = "select a from AccessLevel a order by a.version")
})
@ToString(callSuper = true)
public abstract class AccessLevel extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "access_level", insertable = false, updatable = false, length = 20)
    @Getter @Setter
    private String level;

    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @Getter @Setter
    private boolean active = true;

    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @Getter @Setter
    private Account account;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accessLevel")
//    private DataDoctor dataDoctor;
//
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accessLevel")
//    private DataClient dataClient;
//
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accessLevel")
//    private DataAdministrator dataAdministrator;

}