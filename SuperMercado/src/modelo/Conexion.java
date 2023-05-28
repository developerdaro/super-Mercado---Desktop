package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Conexion {

    public static final String URL = "jdbc:mysql://localhost:3306/SuperMercado?autoReconnect=true&useSSL=false";
    public static final String usuario = "root";
    public static final String contrasena = "admin";

 

    public Connection getConnection() {

        Connection conexion = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection(URL, usuario, contrasena);

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return conexion;
    }

}
