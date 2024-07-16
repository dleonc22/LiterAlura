package cr.dani.literalurachallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private int anioNacimiento;
    private int anioFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Libro> libros = new ArrayList<>();

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
        libro.setAutor(this);
    }
}
