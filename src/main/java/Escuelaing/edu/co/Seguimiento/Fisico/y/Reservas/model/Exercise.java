package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import lombok.Data;

@Data // Lombok generará automáticamente getters, setters, toString, equals, hashCode
public class Exercise {
    private String id;
    private String name;
    private String description;
    private String muscleGroup;
    private int sets;
    private int repetitions;
    private String instructions;
    private String difficultyLevel;
}
