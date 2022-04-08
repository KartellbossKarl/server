package io.getarrays.server.service.implementation;

import io.getarrays.server.Repo.ServerRepo;
import io.getarrays.server.model.Server;
import io.getarrays.server.enumeration.Status;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static java.lang.Boolean.TRUE;

//Um die Funktionen vom ServerService zu implementieren

@RequiredArgsConstructor //LOMBAK created einen Konstruktor für serverrepo -> Dependency Injection
@Service
@Transactional
@Slf4j //Damit man in der Konsole sieht was passiert

public class ServerServiceImplementation  implements ServerService {
    private final ServerRepo serverRepo; //um die Funktionen abbilden zu können
    @Override
    public Server create(Server server) {
        log.info("Saving new Server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server); //um den Server zu saven in die DB
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress); //gibt die InetAddress aus -> The getByName() method of InetAddress class determines the IP address of a host from the given host's name. If
        server.setStatus(address.isReachable(10000)? Status.SERVER_UP : Status.SERVER_DOWN); //kann der Server innerhalb des Timeouts erreicht werden?
                                                                                        // Wenn ja-> Server_IP -> Wenn nein -> Server_Down
        serverRepo.save(server); //Den server mit dem neuen/alten Status saven
        return server;
    }

    @Override
    public Collection<Server> List(int limit) {
        log.info("Fetching all Servers");
        return serverRepo.findAll(PageRequest.of(0,limit)).toList();//Mehrere Findall methoden, hier mit Page -> gibt alle Seiten von 0 (erste Seite) bis zum Limit aus
    }


    @Override
    public Server get(Long id) {
        log.info("Fetching Server by Id: {} ", id);
        return serverRepo.findById(id).get(); //get returnt die ID vom Server, und serverRepo sucht genau nach dieser ID

    }

    @Override
    public Server update(Server server) {
        log.info("Updating Server: {}", server.getName());
        return serverRepo.save(server); //updated einen Server -> Wenn eine ID gegeben wird, weiß Repo dass es nur updaten muss, wenn keine Id, geht er zu Create und erstellt eine  neue
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting Server by Id: {} ", id);
        serverRepo.deleteById(id);
        return TRUE;

    }

    @Override
    public Long count(Collection<Server> Liste) {
        log.info("Count Servers");
        return serverRepo.count();
    }


    private String setServerImageUrl() {
        String[] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"} ; //aus dem Download

        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/"+imageNames[new Random().nextInt(4)]).toUriString(); //Random Bildauswahl der 3 Bilder
    }
}
