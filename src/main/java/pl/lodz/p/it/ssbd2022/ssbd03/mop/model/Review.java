package pl.lodz.p.it.ssbd2022.ssbd03.mop.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.DataClient;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="review")
@NamedQueries({
        @NamedQuery(name = "Review.findAll", query = "select a from Review a"),
        @NamedQuery(name = "Review.findById", query = "select a from Review a where a.id = :id"),
})
@ToString
@NoArgsConstructor
public class Review extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "implant_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter @Setter
    private Implant implant;

    @JoinColumn(name = "client_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @Getter @Setter
    private DataClient client;

    @Basic(optional = false)
    @Column(name = "text", nullable = false, length = 512)
    @Getter @Setter
    private String text;

    @Basic(optional = false)
    @Column(name = "date", nullable = false)
    @Getter @Setter
    private Date date;
}