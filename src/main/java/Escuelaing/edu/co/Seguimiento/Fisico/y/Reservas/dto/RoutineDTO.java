package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.Data;

import java.util.List;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class RoutineDTO {
    private String name;
    private String objective;
    private String description;
    private List<ExerciseDTO> exercises;
    private String duration;
    private String frequency;

}
