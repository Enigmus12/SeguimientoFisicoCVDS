package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class Routine {
    private String id;
    private String name;
    private String description;
    private String goal;
    private List<Exercise> exercises;
    private String assignedTrainer;
    private Date creationDate;
    private Date modificationDate;
    private boolean customized;
}
