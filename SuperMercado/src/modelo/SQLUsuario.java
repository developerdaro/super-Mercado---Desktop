package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class SQLUsuario {

    public boolean registrar(Usuarios usuario) {
        Conexion con = new Conexion();

        PreparedStatement ps = null;

        try {

            Connection conexion = con.getConnection();
            ps = conexion.prepareStatement("insert into usuario (nombreUsuario,contrasena,nombre,correo,idTipo_usuario,identificacion) values(?,?,?,?,?,?)");
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getCorreo());
            ps.setInt(5, usuario.getIdTipo_usuario());
            ps.setString(6, usuario.getIdentificacion());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("e = " + e);
            return false;
        }

    }

    public int verificarUsuario(String usuario) {
        Conexion con = new Conexion();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conexion = con.getConnection();
            ps = conexion.prepareStatement("select count(identificacion) from usuario where nombreUsuario=?");
            ps.setString(1, usuario);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1;

        } catch (Exception e) {
            System.out.println("e = " + e);
            return 1;
        }

    }

    public boolean comprarobarEmail(String correo) {

        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(correo);

        return mather.find();

    }

    public boolean iniciarsesion(Usuarios usuario) {
        Conexion con = new Conexion();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conexion = con.getConnection();
            ps = conexion.prepareStatement("select  u.identificacion, u.nombreUsuario,u.contrasena,u.nombre,u.idTipo_usuario,t.nombre from usuario as u inner join tipo_usuario as t on u.idTipo_usuario=t.idtipo_usuario  where nombreUsuario=?");
            ps.setString(1, usuario.getNombreUsuario());

            rs = ps.executeQuery();
            if (rs.next()) {
                if (usuario.getContrasena().equals(rs.getString("contrasena"))) {

                    ps = conexion.prepareStatement("update usuario set ultima_sesion=? where identificacion=?");
                    ps.setString(1, usuario.getUltima_sesion());
                    ps.setString(2, rs.getString("identificacion"));
                    ps.executeUpdate();

                    usuario.setIdentificacion(rs.getString("u.identificacion"));
                    usuario.setNombre(rs.getString("u.nombre"));
                    usuario.setIdTipo_usuario(rs.getInt("u.idTipo_Usuario"));
                    usuario.setNombreRol(rs.getString("t.nombre"));
                    return true;
                } else {
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
            System.out.println("e = " + e);
            return false;
        }

    }

    //Eliminar
    public boolean eliminar(Usuarios usuario) {
        Conexion con = new Conexion();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conexion = con.getConnection();
        try {
            ps = conexion.prepareStatement("delete from usuario where identificacion=?");

            ps.setString(1, usuario.getIdentificacion());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("e = " + e);
            return false;
        } //siempre se ejecuta
        finally {
            try {
                conexion.close();
            } catch (Exception e) {
                System.out.println("e = " + e);

            }
        }
    }

}
