package io.getarrays.server.ressource;


import io.getarrays.server.model.Server;
import io.getarrays.server.model.response;
import io.getarrays.server.enumeration.Status;
import io.getarrays.server.service.implementation.ServerServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController //wird vom HHTP-Client aus Angular gecalled
@RequestMapping("/server") //unter /server erreichbar
@RequiredArgsConstructor //same wie bei ServerRessourceImplementation

public class ServerRessource { //REST-Controller
    private final ServerServiceImplementation serverService; // muss gecalled werden, weil die immer benötigt wird, wenn man was im Backend macht-> Dependency Injection

    @GetMapping("/list")
    public ResponseEntity<response> getService() throws InterruptedException { //Extension of HttpEntity that adds an HttpStatus status code. Used in RestTemplate as well as in @Controller method
        //ResponseEntity represents the whole HTTP response: https://www.baeldung.com/spring-response-entity
        TimeUnit.SECONDS.sleep(0); //Hier wird die Response gebaut -> mit Timeout verzögert sich das etwas von Loading State App Data = null -> 3 Sekunden später ist die App data vorhanden
        return ResponseEntity.ok(
                response.builder() //call of builder -> damit die response gebaut werden kann
                        .timestamp(now()) //timestamp mit now wird geliefert
                        .data(Map.of("servers", serverService.List(30))) //Data mit einer Map mit einer max Anzahl von 30 Servern -> Kann auch mit Parameter sein, den der User eingibt
                        .message("Server retrieved")
                        .status(OK)
                        .statusCode(OK.value()) //actual number e.g. 200
                        .build()
        );
    }  //der body ist die Antwort, die wir erstellt haben in der domain -> returned die List of Servers


    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException { //Übergeben Sie die path variable, die wir in der Url abrufen (ipaddress
        //ResponseEntity represents the whole HTTP response: https://www.baeldung.com/spring-response-entity
        Server server = serverService.ping(ipAddress); //erstmal den Server anpingen
        return ResponseEntity.ok(
                response.builder() //call of builder -> damit die response gebaut werden kann
                        .timestamp(now()) //timestamp mit now wird geliefert
                        .data(Map.of("server", server)) //Data mit einer Map
                        .message(server.getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed") //je nachdem ob ping erfolgreich ist -> Server up oder down message
                        .status(OK)
                        .statusCode(OK.value()) //actual number e.g. 200
                        .build()
        );


    }

    @PostMapping ("/save")
    public ResponseEntity<response> saveServer(@RequestBody @Valid Server server) { //add request body  -> send body and the request
        //grab the body of the request which contains the server and check for the validation of the server we added -> Check for Ipaddress not empty
        //ResponseEntity represents the whole HTTP response: https://www.baeldung.com/spring-response-entity
        return ResponseEntity.ok( //geht auch was anderes als nur ok "vgl Minute 49.40
                response.builder() //call of builder -> damit die response gebaut werden kann
                        .timestamp(now()) //timestamp mit now wird geliefert
                        .data(Map.of("server", serverService.create(server))) //Data mit einer Map -> returned den created server den wir dann als part of the data zur response geben
                        .message("Server created")
                        .status(CREATED)
                        .statusCode(CREATED.value()) //actual number e.g. 200
                        .build()
        );


    }

    @GetMapping("/get/{id}")
    public ResponseEntity<response> getServer(@PathVariable("id") Long id) { //Übergeben der Path variable Id
        //ResponseEntity represents the whole HTTP response: https://www.baeldung.com/spring-response-entity
        return ResponseEntity.ok(
                response.builder() //call of builder -> damit die response gebaut werden kann
                        .timestamp(now()) //timestamp mit now wird geliefert
                        .data(Map.of("server", serverService.get(id))) //Data mit einer Map -> id abrufen
                        .message("Server retrieved") //je nachdem ob ping erfolgreich ist -> Server up oder down message
                        .status(OK)
                        .statusCode(OK.value()) //actual number e.g. 200
                        .build()
        );

    }

    @DeleteMapping ("/delete/{id}") //deleted einen Server
    public ResponseEntity<response> deleteServer(@PathVariable("id") Long id) { //Übergeben der Path variable Id
        //ResponseEntity represents the whole HTTP response: https://www.baeldung.com/spring-response-entity
        return ResponseEntity.ok(
                response.builder() //call of builder -> damit die response gebaut werden kann
                        .timestamp(now()) //timestamp mit now wird geliefert
                        .data(Map.of("deleted", serverService.delete(id))) //Data mit einer Map -> id abrufen
                        .message("Server deleted") //je nachdem ob ping erfolgreich ist -> Server up oder down message
                        .status(OK)
                        .statusCode(OK.value()) //actual number e.g. 200
                        .build()
        );
//alle funktionen drüber returned json aber ServerImage returned ein PNG, daher muss spezifiziert werden
    }

    //Browser sendet beim Loaden eine getRequest -> wird von der Methode intercepted und get Request hat den Filename e.g. localhost 8080/image/server-1.png
    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE) //wenn man was anderes als Path übergeben möchte muss man path = davor schreiben um differenzieren zu können
    //read the URL for the image of the server and return image to the frontend -> Path muss gleich sein wie bei dem setServerimage
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {//Übergeben der Path variable fileName
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/Images/" + fileName));

    }

    @GetMapping("/count")
    public ResponseEntity<response> getCount() { //Extension of HttpEntity that adds an HttpStatus status code. Used in RestTemplate as well as in @Controller method
        //ResponseEntity represents the whole HTTP response: https://www.baeldung.com/spring-response-entity
        return ResponseEntity.ok(
                response.builder() //call of builder -> damit die response gebaut werden kann
                        .timestamp(now()) //timestamp mit now wird geliefert
                        .data(Map.of("server Anzahl:", serverService.count(serverService.List(30)))) //Data mit einer Map mit einer max Anzahl von 30 Servern -> Kann auch mit Parameter sein, den der User eingibt
                        .message("Server counted")
                        .status(OK)
                        .statusCode(OK.value()) //actual number e.g. 200
                        .build()
        );
    }

}
