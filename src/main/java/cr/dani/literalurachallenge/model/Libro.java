package cr.dani.literalurachallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String idioma;

    @Column(name = "numero_descargas")
    private int numeroDescargas;

    @ManyToOne
    private Autor autor;

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = String.join(", ", datosLibro.idiomas());
        this.numeroDescargas = datosLibro.numeroDescargas();
        this.autor = datosLibro.autores().isEmpty() ? null : new Autor(datosLibro.autores().get(0));
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
        if (!autor.getLibros().contains(this)) {
            autor.getLibros().add(this);
        }
    }

    @Override
    public String toString() {
        return "╔═════════════════╗\n" +
                "║     LIBRO       ║\n" +
                "╚═════════════════╝\n" +
                "Título           : " + titulo + "\n" +
                "Autor            : " + (autor != null ? autor.getNombre() : "Desconocido") + "\n" +
                "Idioma           : " + idioma + "\n" +
                "Número de descargas: " + numeroDescargas + "\n" +
                "╚═════════════════╝";
    }
}
