package io.getarrays.server.service;

import io.getarrays.server.model.Server;

import java.io.IOException;
import java.util.Collection;

//alle Feautures und Funktionen, die die Website am Ende haben soll
//Mit dem Repo können alle unten stehenden Funktionen abgebildet werden
public interface ServerService {
    Server create (Server server); //Einen neuen Server createn
    Server ping (String ipAddress) throws IOException; //Pingt den Server mit der Ip-Address
    Collection<Server> List(int limit); //Soll alle Server auflisten mit einem Limit
    Server get (Long id ); //gibt die id vom Server zurück, den wir finden wollen
    Server update (Server server); //updatet einen Server und speichert ihn in die Datenbank
    Boolean delete (Long id); //Deleted einen Server
    Long count (Collection<Server> list); //Soll die Server zählen
}
