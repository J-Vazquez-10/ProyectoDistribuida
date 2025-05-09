package codigo;

import java.util.*;

public class Kardex {
    List<Materia> cursando= new ArrayList<Materia>();
    private float promedio;

    public void setMateria(Materia materia){
        cursando.add(materia);
    }

    public void calificar(){
        Scanner scanner= new Scanner(System.in);
        for (Materia materia : cursando){
            System.out.println("Ingrese la calificación del alumno para la materia "+materia.getNombre());
            materia.setCalif(scanner.nextFloat());
        }
    }

    public void removeMateria(Materia materia){
        cursando.remove(materia);
    }

    public float getPromedio(){
        for (Materia materia : cursando){
            promedio+=materia.getCalif();
        }
        return promedio/cursando.size();
    }

    public void setPromedio(float prom){
        this.promedio= prom;
    }
}
