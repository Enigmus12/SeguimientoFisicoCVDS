package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto;

import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class ExerciseDTO {
    private String name;
    private String description;
    private int sets;
    private int repetitions;
    private String instructions;
}
