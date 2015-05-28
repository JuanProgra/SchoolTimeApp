package Beans;

/**
 * Created by Godinez Miranda on 24/05/2015.
 */
public class Horario {
    public String Salon;
    public String Materia;
    public String Profesor;
    public String Hora;

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getSalon() {
        return Salon;
    }

    public void setSalon(String salon) {
        Salon = salon;
    }

    public String getMateria() {
        return Materia;
    }

    public void setMateria(String materia) {
        Materia = materia;
    }

    public String getProfesor() {
        return Profesor;
    }

    public void setProfesor(String profesor) {
        Profesor = profesor;
    }
}
