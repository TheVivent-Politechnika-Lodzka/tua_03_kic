package pl.lodz.p.it.ssbd2022.ssbd03.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "data_personal")
@NamedQueries({
        @NamedQuery(name = "DataPersonal.findAll", query = "SELECT d FROM DataPersonal d"),
        @NamedQuery(name = "DataPersonal.findById", query = "select d from DataPersonal d order by d.id"),
        @NamedQuery(name = "DataPersonal.findByName", query = "select d from DataPersonal d order by d.name"),
        @NamedQuery(name = "DataPersonal.findBySurname", query = "select d from DataPersonal d order by d.surname"),
        @NamedQuery(name = "DataPersonal.findByEmail", query = "select d from DataPersonal d order by d.email"),
        @NamedQuery(name = "DataPersonal.findByVersion", query = "select d from DataPersonal d order by d.version")
})
@NoArgsConstructor
@ToString
public class DataPersonal implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @Getter
    private Long id;

    @Getter @Setter
    @Column(name = "name")
    private String name;

    @Getter @Setter
    @Column(name = "surname")
    private String surname;

    @Getter @Setter
    @Column(name = "email")
    private String email;

    @Getter @Setter
    @Column(name = "version")
    private long version;

    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @Getter @Setter
    private Account account;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DataPersonal)) {
            return false;
        }
        DataPersonal other = (DataPersonal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}