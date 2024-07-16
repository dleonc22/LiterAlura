package cr.dani.literalurachallenge.principal;

import cr.dani.literalurachallenge.model.*;
import cr.dani.literalurachallenge.repository.AutorRepository;
import cr.dani.literalurachallenge.repository.LibroRepository;
import cr.dani.literalurachallenge.service.ConsumoAPI;
import cr.dani.literalurachallenge.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;


public class Principal {

    private  Scanner scanner = new Scanner(System.in);
    private static final String BASE_URL = "http://gutendex.com/books/";

    private ConsumoAPI consumoAPI;
    private ConvierteDatos convierteDatos;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository,
                     ConsumoAPI consumoAPI, ConvierteDatos convierteDatos) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    ╔══════════════════════════════════════════╗
                    ║               MENÚ DE OPCIONES            ║
                    ╠══════════════════════════════════════════╣
                    ║ Elija la opción a través de su número:    ║
                    ║ 1 - Buscar libro por título               ║
                    ║ 2 - Listar libros registrados             ║
                    ║ 3 - Listar autores registrados            ║
                    ║ 4 - Listar autores vivos en un determinado año ║
                    ║ 5 - Listar libros por idioma              ║
                    ║ 0 - Salir                                 ║
                    ╚══════════════════════════════════════════╝
                    """);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivosPorAnio();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private DatosLibro obtenerDatosLibro() {
        System.out.println("Que libro deseas buscar?: ");
        String nombreLibro = scanner.nextLine();
        String json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + nombreLibro.replace(" ", "+"));
        DatosResultados datosResultados = convierteDatos.obtenerDatos(json, DatosResultados.class);
        return datosResultados.libros().get(0);
    }

    private void buscarLibro() {
        try {
            DatosLibro datosLibro = obtenerDatosLibro();
            Libro libro = new Libro(datosLibro);

            Autor autor = autorRepository.findByNombre(datosLibro.autores().get(0).nombre());
            if (autor != null) {
                libro.setAutor(autor);
                autor.agregarLibro(libro);
            } else {
                autor = new Autor(datosLibro.autores().get(0));
                autor.agregarLibro(libro);
                libro.setAutor(autor);
                autorRepository.save(autor);
            }

            libroRepository.save(libro);
            System.out.println(libro);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Libro no encontrado");
        } catch (Exception e) {
            System.out.println("No se puede registrar un libro más de una vez");
        }
    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("Ingrese el año por el que desea buscar: ");
        int fecha = scanner.nextInt();
        List<Autor> autoresVivos = autorRepository.buscarAutoresVivosPorAnio(fecha);

        if (autoresVivos.isEmpty()) {
            System.out.println("Ningún autor vivo encontrado en ese año");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                - español
                - inglés
                """);
        String idioma = scanner.nextLine();
        if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma)) {
            List<Libro> librosIdioma = libroRepository.findByIdiomaIgnoreCase(idioma);
            if (librosIdioma.isEmpty()) {
                System.out.println("No hay libros disponibles en el idioma seleccionado");
            } else {
                librosIdioma.forEach(System.out::println);
            }
        } else {
            System.out.println("Opción no válida");
        }
    }
}
