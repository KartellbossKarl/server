package io.getarrays.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;
//API Grundaufbau -> Genauer Aufbau wird in ServerRessource gecoded
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL) //keine Nulls erlaubt
public class response { // Klasse, die jedes Mal beim builden einer API returned wird -> Failure oder Success Responses
    protected LocalDateTime timestamp;
    protected int statusCode; //404 usw.
    protected HttpStatus status; // HTTP Status-> kommt von Spring Framework
    protected String reason; //Reason for the error
    protected String message; //message beim error bspw. bei der API
    protected String developermessage; //nur für den developer bestimmt
    protected Map < ? , ? > data; //Map: Schnittstelle zum Speichern von Daten in Schlüssel-Wert-Paaren: Response, die der user bekommt, jedes Mal, wenn er eine Request schickt (success, failure)
}
