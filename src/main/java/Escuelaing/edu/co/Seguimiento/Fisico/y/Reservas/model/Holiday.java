package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document(collection = "HolyDays")
public class Holiday {

  @MongoId
  private String id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate date;

  private String name;
  private String description;

  public Holiday(LocalDate date, String name, String description) {
    this.date = date;
    this.name = name;
    this.description = description;
  }
}
