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
@Table(name = "data_client")
@NamedQueries({
        @NamedQuery(name = "DataClient.findAll", query = "SELECT d FROM DataClient d"),
        @NamedQuery(name = "DataClient.findById", query = "select d from DataClient d order by d.id"),
        @NamedQuery(name = "DataClient.findByNip", query = "select d from DataClient d order by d.nip"),
        @NamedQuery(name = "DataClient.findByVersion", query = "select d from DataClient d order by d.version")
})
@ToString(callSuper = true)
@NoArgsConstructor
public class DataClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @Getter
    private Long id;

    @Getter @Setter
    @Column(name = "nip")
    private String nip;

    @Getter @Setter
    @Column(name = "version")
    private long version;

    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private AccessLevel accessLevel;


    public DataClient(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DataClient)) {
            return false;
        }
        DataClient other = (DataClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}