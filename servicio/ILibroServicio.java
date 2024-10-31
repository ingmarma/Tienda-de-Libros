package marma.tienda_libros.servicio;

import marma.tienda_libros.modelo.Libro;

import java.util.List;

public interface ILibroServicio {
    public List<Libro> listarlibros();
    public Libro buscarLibrPorId(Integer idLibro);
    public void guardarLibro(Libro libro);
    public void eliminarLibro(Libro libro);

}
