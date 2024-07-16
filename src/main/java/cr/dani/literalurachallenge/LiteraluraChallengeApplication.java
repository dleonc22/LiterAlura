package cr.dani.literalurachallenge;

import cr.dani.literalurachallenge.principal.Principal;
import cr.dani.literalurachallenge.repository.AutorRepository;
import cr.dani.literalurachallenge.repository.LibroRepository;
import cr.dani.literalurachallenge.service.ConsumoAPI;
import cr.dani.literalurachallenge.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraChallengeApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private ConsumoAPI consumoAPI;
    private ConvierteDatos convierteDatos;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraChallengeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(libroRepository, autorRepository, consumoAPI, convierteDatos);
        principal.mostrarMenu();
    }

}
