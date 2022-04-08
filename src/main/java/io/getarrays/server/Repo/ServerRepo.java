package io.getarrays.server.Repo;

import io.getarrays.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServerRepo extends JpaRepository <Server,Long > {
    Server findByIpAddress (String ipAddress); //Methode zur SELECT ABFRAGE nach IPADRESS ->unique, deswegen funktioniert das
}
