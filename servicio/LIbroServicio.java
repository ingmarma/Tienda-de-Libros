package marma.tienda_libros.servicio;

import marma.tienda_libros.modelo.Libro;
import marma.tienda_libros.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LIbroServicio implements ILibroServicio{
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Override
    public List<Libro> listarlibros() {
        return libroRepositorio.findAll();
    }

    @Override
    public Libro buscarLibrPorId(Integer idLibro) {
        Libro libro = libroRepositorio.findById(idLibro).orElse(null);
        return libro;
    }

    @Override
    public void guardarLibro(Libro libro) {
        libroRepositorio.save(libro);
    }

    @Override
    public void eliminarLibro(Libro libro) {
        libroRepositorio.delete(libro);
    }
}
