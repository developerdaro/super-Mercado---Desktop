package controlador;

import java.sql.Connection;
import java.sql.ResultSetMetaData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import modelo.CifrarClave;
import modelo.SQLUsuario;
import modelo.Usuarios;
import view.Programa;
import vista.IniciarSesion;
import vista.Inicio;
import static vista.Inicio.registro;
import javax.swing.table.DefaultTableModel;
import modelo.Conexion;
import vista.Registro;

public class ControladorPersona implements ActionListener {

    private Programa programa;
    private Inicio inicio;
    private IniciarSesion vistaIniciarSesion;
    private Registro vistaRegistro;
    private Usuarios usuario;
    private SQLUsuario modelo;

    public ControladorPersona(Registro vistaRegistro, Inicio inicio, IniciarSesion vistaIniciarSesion, Programa programa, Usuarios usuario, SQLUsuario modelo) {
        this.vistaRegistro = vistaRegistro;
        this.inicio = inicio;
        this.vistaIniciarSesion = vistaIniciarSesion;
        this.programa = programa;

        this.usuario = usuario;
        this.modelo = modelo;

        vistaRegistro.btnRegistro.addActionListener(this);
        inicio.btnregistroincio.addActionListener(this);
        inicio.btnLogin.addActionListener(this);
        vistaIniciarSesion.btnIniciaSesion.addActionListener(this);
        programa.btnSave.addActionListener(this);
        programa.btnClear.addActionListener(this);
        programa.btnBuscar.addActionListener(this);
        programa.btnListaClientes.addActionListener(this);
        programa.btnDelete.addActionListener(this);

    }

    public void iniciar() {
        inicio.setTitle("Registro de usuario");
        inicio.setLocationRelativeTo(null);

    }

    public void limpiarRegistro() {
        vistaRegistro.txtNombr.setText("");
        vistaRegistro.txtUsuario.setText("");
        vistaRegistro.txtcontra1.setText("");
        vistaRegistro.txtcontra2.setText("");
        vistaRegistro.txtemail.setText("");
        vistaRegistro.txtIdentificacion.setText("");

    }

    public void limpiarRegistroUsuario() {
        programa.txtNombre.setText("");
        programa.txtUsuario.setText("");
        programa.txtContra.setText("");
        programa.txtContra2.setText("");
        programa.txtCorreo.setText("");
        programa.txtIdentificacion.setText("");

    }

    public void buscar() {

        DefaultTableModel modeloTabla = new DefaultTableModel();
        programa.jTableListar.setModel(modeloTabla);

        String campo = programa.txtBuscar.getText();
        String where = "";

        if (!"".equals(campo)) { //Si el campo no esta vacio
            where = "where identificacion='" + campo + "'";
        }

        //Cargar datos
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Conexion con = new Conexion();
            Connection conexion = con.getConnection();

            ps = conexion.prepareStatement("select identificacion,nombre,nombreUsuario,correo from usuario " + where);
            rs = ps.executeQuery();
            System.out.println(ps);
            modeloTabla.addColumn("identificacion");
            modeloTabla.addColumn("nombre");
            modeloTabla.addColumn("nombreUsuario");

            modeloTabla.addColumn("correo");

            ResultSetMetaData rdMD = rs.getMetaData();//Para qeu tome solo el tamaño de columnas 
            int cantidadColumnas = rdMD.getColumnCount();

            int anchos[] = {50, 150, 50, 70};//definimos ancho de columnas
            for (int i = 0; i < cantidadColumnas; i++) { //Asignamos el tamaño
                programa.jTableListar.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

            }

            //Insertamos datos
            while (rs.next()) {
                //Object para que acepte cualquier dato
                Object fila[] = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i] = rs.getObject(i + 1); //i+1 para que no tome el id

                }
                modeloTabla.addRow(fila);

            }

        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    public void ProgramaHome() {

        programa.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        //Registo de usuarios
        if (ae.getSource() == vistaRegistro.btnRegistro) {

            //boton registro
            Usuarios usuario = new Usuarios();
            SQLUsuario sqlUsuario = new SQLUsuario();

            String contrasena = new String(vistaRegistro.txtcontra1.getPassword());
            String confirmarContrasena = new String(vistaRegistro.txtcontra2.getPassword());

            if (vistaRegistro.txtIdentificacion.getText().equals("") || vistaRegistro.txtNombr.getText().equals("") || vistaRegistro.txtUsuario.getText().equals("") || vistaRegistro.txtcontra1.equals("") || vistaRegistro.txtcontra2.equals("") || vistaRegistro.txtemail.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor rellene todos los campos");
            } else {

                if (contrasena.equals(confirmarContrasena)) {

                    //Todo correcto
                    //llamamos a validacion
                    if (sqlUsuario.verificarUsuario(vistaRegistro.txtUsuario.getText()) == 0) {

                        //comprobamos correo electronico
                        if (sqlUsuario.comprarobarEmail(vistaRegistro.txtemail.getText())) {
                            //Ciframos la contraseña llamamos a la clase del modelo cifrarclave
                            String nuevacontra = CifrarClave.md5(contrasena);

                            usuario.setNombreUsuario(vistaRegistro.txtUsuario.getText());
                            usuario.setContrasena(nuevacontra);
                            usuario.setNombre(vistaRegistro.txtNombr.getText());
                            usuario.setCorreo(vistaRegistro.txtemail.getText());
                            usuario.setIdentificacion(vistaRegistro.txtIdentificacion.getText());

                            usuario.setIdTipo_usuario(2); //rol

                            if (sqlUsuario.registrar(usuario)) {
                                JOptionPane.showMessageDialog(null, "Se ha registrado correctamente");
                                this.limpiarRegistro();

                            } else {
                                JOptionPane.showMessageDialog(null, "Error en el registro");

                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "EL correo electornico no es correcto");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "YA existe un usuario con ese nombre");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }

            }
        }
        if (ae.getSource() == inicio.btnregistroincio) {
            vistaRegistro.setTitle("Registro Inicio");
            vistaRegistro.setLocationRelativeTo(null);
            vistaRegistro.setVisible(true);

        }
        if (ae.getSource() == inicio.btnLogin) {
            vistaIniciarSesion.setTitle("Iniciar Sesion");
            vistaIniciarSesion.setLocationRelativeTo(null);
            vistaIniciarSesion.setVisible(true);

        }

//        //Login
        if (ae.getSource() == vistaIniciarSesion.btnIniciaSesion) {

            //iniciar sesion
            Usuarios usuario = new Usuarios();
            SQLUsuario sqlUsuario = new SQLUsuario();

            Date date = new Date();
            DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String contrasena = new String(vistaIniciarSesion.txtcontraLogin.getPassword());

            if (vistaIniciarSesion.txtUSuarioLogin.getText().equals("") || contrasena.equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor rellene todos los campos");
            } else {
                String nuevaContrasena = CifrarClave.md5(contrasena);
                usuario.setNombreUsuario(vistaIniciarSesion.txtUSuarioLogin.getText());
                usuario.setContrasena(nuevaContrasena);
                usuario.setUltima_sesion(fechaHora.format(date));
                if (sqlUsuario.iniciarsesion(usuario)) {
                    //JOptionPane.showMessageDialog(null, "Felicidades acabas de ingresar a Daromitas");

//                this.dispose(); //Cierra la venta de iniciar sesion lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
                    programa.dispose();
                    Programa programa = new Programa(usuario);
                    //this.ProgramaHome();

                    programa.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Error en las credenciales");

                }
            }

        }

        //Registo de usuarios LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
        if (ae.getSource() == programa.btnSave) {

            //boton registro
            Usuarios usuario = new Usuarios();
            SQLUsuario sqlUsuario = new SQLUsuario();

            String contrasena = new String(programa.txtContra.getPassword());
            String confirmarContrasena = new String(programa.txtContra2.getPassword());

            if (programa.txtNombre.getText().equals("") || programa.txtUsuario.getText().equals("") || programa.txtContra.equals("") || programa.txtContra2.equals("") || programa.txtCorreo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Por favor rellene todos los campos");
            } else {

                if (contrasena.equals(confirmarContrasena)) {

                    //Todo correcto
                    //llamamos a validacion
                    if (sqlUsuario.verificarUsuario(programa.txtUsuario.getText()) == 0) {

                        //comprobamos correo electronico
                        if (sqlUsuario.comprarobarEmail(programa.txtCorreo.getText())) {
                            //Ciframos la contraseña llamamos a la clase del modelo cifrarclave
                            String nuevacontra = CifrarClave.md5(contrasena);

                            usuario.setNombreUsuario(programa.txtUsuario.getText());
                            usuario.setContrasena(nuevacontra);
                            usuario.setNombre(programa.txtNombre.getText());
                            usuario.setCorreo(programa.txtCorreo.getText());
                            usuario.setIdentificacion(programa.txtIdentificacion.getText());

                            usuario.setIdTipo_usuario(2); //rol

                            if (sqlUsuario.registrar(usuario)) {
                                JOptionPane.showMessageDialog(null, "Se ha registrado correctamente");
                                this.limpiarRegistroUsuario();
                                this.buscar();

                            } else {
                                JOptionPane.showMessageDialog(null, "Error en el registro");

                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "EL correo electornico no es correcto");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "YA existe un usuario con ese nombre");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }

            }
        }

        //Metodo para limpiar
        if (ae.getSource() == programa.btnClear) {
            this.limpiarRegistroUsuario();
        }

        if (ae.getSource() == programa.btnListaClientes) {

            DefaultTableModel modeloTabla = new DefaultTableModel();
            programa.jTableListar.setModel(modeloTabla);

            //Cargar datos
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                Conexion con = new Conexion();
                Connection conexion = con.getConnection();

                ps = conexion.prepareStatement("select identificacion,nombre,nombreUsuario,correo from usuario");
                rs = ps.executeQuery();

                modeloTabla.addColumn("identificacion");
                modeloTabla.addColumn("nombre");
                modeloTabla.addColumn("nombreUsuario");
                modeloTabla.addColumn("correo");

                //Insertamos datos
                while (rs.next()) {
                    //Object para que acepte cualquier dato
                    Object fila[] = new Object[4];
                    for (int i = 0; i < 4; i++) {
                        fila[i] = rs.getObject(i + 1); //i+1 para que no tome el id

                    }
                    modeloTabla.addRow(fila);

                    ////
                    ////
                }

            } catch (Exception e) {
                System.out.println("e = " + e);
            }
        }

        //Metodo buscar
        if (ae.getSource() == programa.btnBuscar) {
            this.buscar();
        }

        if (ae.getSource() == programa.btnDelete) {
            usuario.setIdentificacion(programa.txtBuscar.getText());

            if (modelo.eliminar(usuario)) {
                JOptionPane.showMessageDialog(null, "Registro elimiado correctamente");
                this.limpiarRegistroUsuario();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el registro");
                this.limpiarRegistroUsuario();
            }
        }

        //
    }

}
