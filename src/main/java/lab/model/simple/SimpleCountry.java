package lab.model.simple;

import lab.model.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "country")
@Table(name = "country")
@Component("country")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SimpleCountry implements Country, Serializable {
    @Id
    @GeneratedValue
    int id;
    String name;
    String codeName;
}
