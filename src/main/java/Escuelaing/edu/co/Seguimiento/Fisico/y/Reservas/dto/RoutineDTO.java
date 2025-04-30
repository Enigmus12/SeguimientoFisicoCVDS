package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class RoutineDTO {
    private String id;
    private String name;
    private String description;
    private String goal;
    private String trainer;
}
