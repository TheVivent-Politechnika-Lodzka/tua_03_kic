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
@Table(name = "data_doctor")
@NamedQueries({
        @NamedQuery(name = "DataDoctor.findAll", query = "SELECT d FROM DataDoctor d"),
        @NamedQuery(name = "DataDoctor.findById", query = "select d from DataDoctor d order by d.id"),
        @NamedQuery(name = "DataDoctor.findByIntercom", query = "select d from DataDoctor d order by d.intercom"),
        @NamedQuery(name = "DataDoctor.findByPhone", query = "select d from DataDoctor d order by d.phone"),
        @NamedQuery(name = "DataDoctor.findByyWersja", query = "select d from DataDoctor d order by d.version")
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataDoctor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "intercom")
    @Getter @Setter
    private String intercom;

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


    public DataDoctor(Long id) {
        this.id = id;
    }

    public DataDoctor(Long id, String intercom, String phone, long version) {
        this.id = id;
        this.intercom = intercom;
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
        DataDoctor other = (DataDoctor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}