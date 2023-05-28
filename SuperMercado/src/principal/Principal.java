package principal;

import controlador.ControladorPersona;
import modelo.SQLUsuario;
import modelo.Usuarios;
import vista.IniciarSesion;
import vista.Inicio;
import vista.Registro;

public class Principal {

    public static void main(String[] args) {
   
        
        IniciarSesion vistaIniciarSesion=new IniciarSesion();
        Inicio vistaInicio = new vista.Inicio();
        Registro vistaRegistro = new Registro();
        Usuarios usuario = new Usuarios();
        SQLUsuario modelo = new SQLUsuario();

        ControladorPersona controlador=new ControladorPersona(vistaRegistro,vistaInicio,usuario, modelo);

        controlador.iniciar();
        vistaInicio.setVisible(true);

    }

}
