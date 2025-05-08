package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.model;

import java.util.List;

import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.ExerciseDTO;
import Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.dto.RoutineDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document(collection = "Routines")
public class Routine {

    @MongoId
    private String id;
    private String name;
    private String objective;
    private String description;
    private List<ExerciseDTO> exercises;
    private String duration;
    private String frequency;

    @PersistenceCreator
    public Routine(String id, String name, String objective, String description, List<ExerciseDTO> exercises) {
        this.id = id;
        this.name = name;
        this.objective = objective;
        this.description = description;
        this.exercises = exercises;
        this.duration = "0";
        this.frequency = "0";
    }

    public Routine(RoutineDTO routinedto) {
        this.name = routinedto.getName();
        this.objective = routinedto.getObjective();
        this.description = routinedto.getDescription();
        this.exercises = routinedto.getExercises();
        this.duration = routinedto.getDuration();
        this.frequency = routinedto.getFrequency();
    }
}