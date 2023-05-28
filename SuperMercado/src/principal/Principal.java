package principal;

import controlador.ControladorPersona;
import modelo.SQLUsuario;
import modelo.Usuarios;
import vista.Programa;
import vista.IniciarSesion;
import vista.Inicio;
import vista.Registro;

public class Principal {

    public static void main(String[] args) {
        Programa programa=new Programa();
        IniciarSesion vistaIniciarSesion=new IniciarSesion();
        Inicio vistaInicio = new vista.Inicio();
        Registro vistaRegistro = new Registro();
        Usuarios usuario = new Usuarios();
        SQLUsuario modelo = new SQLUsuario();

        ControladorPersona controlador=new ControladorPersona(vistaRegistro,vistaInicio,vistaIniciarSesion,programa,usuario, modelo);

        controlador.iniciar();
        vistaInicio.setVisible(true);
        
        
        
        

    }

}
