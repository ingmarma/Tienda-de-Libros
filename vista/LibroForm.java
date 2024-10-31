package marma.tienda_libros.vista;

import marma.tienda_libros.modelo.Libro;
import marma.tienda_libros.servicio.LIbroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {
    LIbroServicio lIbroServicio;
    private JPanel Panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTextoTextField;
    private JTextField ExistenciaTexto;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LIbroServicio lIbroServicio){
        this.lIbroServicio = lIbroServicio;
        iniciarForma();
        agregarButton.addActionListener(e ->agregarLibro());
//        agregarButton.addActionListener(e ->agregarLibro());{
//            @Override
//            public void actionPerformed(ActionEvent e ->agregarLibro());
//        });
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarLibro());
//            @Override
//            public void actionPerformed( e -> modificarLibro()); {
//
//            });
        eliminarButton.addActionListener(e -> eliminarLibro());
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }

    }
    private void iniciarForma(){
        setContentPane(Panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width = getWidth()/2);
        int y = (tamanioPantalla.height = getHeight()/2);
        setLocation(x, y);
    }
    private void agregarLibro(){
        //Leer Valores del formulario
        if (libroTexto.getText().equals("")){
            mostrarMensaje("Proporciona el nombre del Libro");
            libroTexto.requestFocusInWindow();
            return;
        }
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTextoTextField.getText());
        var existencias = Integer.parseInt(ExistenciaTexto.getText());
        //Crear el Objeto Libro
        var libro = new Libro();
        libro.setNombreLibro(nombreLibro);
        libro.setAutor(autor);
        libro.setPrecio(precio);
        libro.setExistencias(existencias);
        this.lIbroServicio.guardarLibro(libro);
        mostrarMensaje("Se agrego el libro...");
        limpiarFormulario();
        listarLibros();
    }
    private void cargarLibroSeleccionado(){
        //Los indices de las columnas inician en 0
        var renglon = tablaLibros.getSelectedRow();
        if (renglon != 1){// Regresa -1 si no selecciono ningun registro
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro = tablaLibros.getModel().getValueAt(renglon, 1).toString();
            libroTexto.setText(nombreLibro);
            String autor = tablaLibros.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);
            String precio = tablaLibros.getModel().getValueAt(renglon, 3).toString();
            precioTextoTextField.setText(precio);
            String existencias = tablaLibros.getModel().getValueAt(renglon, 4).toString();
            ExistenciaTexto.setText(existencias);
        }
    }
    private void modificarLibro(){
        if (this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un registro...");
        }
        else {
            //Verificamos que nombre del libro no sea null
            if (libroTexto.getText().equals("")){
                mostrarMensaje(" Proporciona el nombre del libro...");
                libroTexto.requestFocusInWindow();
                return;
            }
            //Llenamos el objeto libro a actualizar
            int idLIbro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = Double.parseDouble(precioTextoTextField.getText());
            var existencias = Integer.parseInt(ExistenciaTexto.getText());
            var libro = new Libro(idLIbro, nombreLibro, autor, precio, existencias);
            lIbroServicio.guardarLibro(libro);
            mostrarMensaje("Se modifico el libro...");
            limpiarFormulario();
            listarLibros();
        }
    }
    private void eliminarLibro(){
        var renglon = tablaLibros.getSelectedRow();
        if (renglon!=-1){
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            var libro = new Libro();
            libro.setIdLibro(Integer.parseInt(idLibro));
            lIbroServicio.eliminarLibro(libro);
            mostrarMensaje("Libro" + idLibro + "Eliminado.");
            limpiarFormulario();
            listarLibros();
        }
        else {
            mostrarMensaje("No se ha seleccionado ningun libro a eliminar...");
        }
    }
    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTextoTextField.setText("");
        ExistenciaTexto.setText("");
    }
    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        //Creamos el elemento idTexto oculto
        idTexto = new JTextField();
        idTexto.setVisible(false);
        this.tablaModeloLibros = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };
        String[] cabeceros = {"Id", "Libro", "Autor", "Precio", "Existencias"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);
        //Instanciar Objetos
        this.tablaLibros = new JTable(tablaModeloLibros);
        // Evitar que se seleccione varios registros
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }
    private void listarLibros(){
        //Limpiar Tabla
        tablaModeloLibros.setRowCount(0);
        //Obtener los libros
        var libros = lIbroServicio.listarlibros();
        libros.forEach(libro -> {
            Object[] renglonLibro ={
              libro.getIdLibro(),
              libro.getNombreLibro(),
              libro.getAutor(),
              libro.getPrecio(),
              libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}
