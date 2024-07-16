package cr.dani.literalurachallenge.repository;

import cr.dani.literalurachallenge.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Autor findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <=:anio and a.anioFallecimiento >=:anio")
    List<Autor> buscarAutoresVivosPorAnio(int anio);
}