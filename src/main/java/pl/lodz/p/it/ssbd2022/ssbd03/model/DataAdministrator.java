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
@Table(name = "data_administrator")
@NamedQueries({
        @NamedQuery(name = "DataAdministrator.findAll", query = "SELECT d FROM DataAdministrator d"),
        @NamedQuery(name = "DataAdministrator.findByOrderByIdAsc", query = "select d from DataAdministrator d order by d.id"),
        @NamedQuery(name = "DataAdministrator.findByOrderByPhoneAsc", query = "select d from DataAdministrator d order by d.phone"),
        @NamedQuery(name = "DataAdministrator.findByOrderByVersionAsc", query = "select d from DataAdministrator d order by d.version")
})
@ToString
@NoArgsConstructor
public class DataAdministrator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "phone")
    @Getter @Setter
    private String phone;

    @Column(name = "version")
    @Getter @Setter
    private long version;

    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @Getter @Setter
    private AccessLevel accessLevel;


    public DataAdministrator(Long id) {
        this.id = id;
    }

    public DataAdministrator(Long id, String phone, long version) {
        this.id = id;
        this.phone = phone;
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataDoctor)) {
            return false;
        }
        DataAdministrator other = (DataAdministrator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}