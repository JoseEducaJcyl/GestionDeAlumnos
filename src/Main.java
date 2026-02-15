import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Crear objeto Scanner para leer la entrada del usuario
        Scanner sc = new Scanner(System.in);
        System.out.println("Sistema de Gestion de Alumnos");
        
        // Variable de control para el bucle del menú
        boolean seguir = true;
        
        // Bucle principal del programa que se ejecuta hasta que el usuario elija salir
        while(seguir){
            // Mostrar el menú de opciones
            System.out.println("-------MENU-------");
            System.out.println("1. Nota media de cada alumno");
            System.out.println("2. Listar Alumnos (Aprobados)");
            System.out.println("3. Listar Alumnos (Ordenado)");
            System.out.println("4. Guardar Resultados (Fichero resultado.txt)");
            System.out.println("5. Salir");
            System.out.println("Ingrese opcion: ");
            
            // Leer la opción elegida por el usuario
            int opcion = sc.nextInt();
            sc.nextLine(); // Limpiar el buffer del scanner
            
            // Ejecutar la acción correspondiente según la opción seleccionada
            switch (opcion) {
                case 1:
                    listarAlumnos(); // Mostrar lista de alumnos con sus notas medias
                    break;
                case 2:
                    listarAprobados(); // Mostrar solo alumnos aprobados (media >= 5)
                    break;
                case 3:
                    listarAlumnosOrdenado(); // Mostrar alumnos ordenados por nota media (descendente)
                    break;
                case 4:
                    guardarFichero(); // Guardar resultados en un archivo de texto
                    break;
                case 5:
                    System.out.println("Adios."); // Mensaje de despedida
                    seguir = false; // Salir del bucle y terminar el programa
                    break;
                default:
                    System.out.println("Opcion incorrecta"); // Opción no válida
                    break;
            }
        }
    }
    
    /**
     * Método que muestra todos los alumnos con su nota media
     * Lee los datos del archivo y calcula la media para cada alumno
     */
    public static void listarAlumnos() {
        // Recorrer cada alumno obtenido del archivo
        for (String alumno : leerAlumnos().keySet()) {
            List<Double> notas = leerAlumnos().get(alumno);

            // Calcular la suma de todas las notas del alumno
            double suma = 0.0;
            for (double nota : notas) {
                suma += nota;
            }
            // Calcular la media (suma / número de notas)
            double media = suma / notas.size();
            System.out.println(alumno + " - " + media);
        }
    }

    /**
     * Método que muestra solo los alumnos aprobados (nota media >= 5)
     * Lee los datos del archivo y filtra por aquellos con media >= 5
     */
    public static void listarAprobados() {
        // Recorrer cada alumno obtenido del archivo
        for (String alumno : leerAlumnos().keySet()) {
            List<Double> notas = leerAlumnos().get(alumno);

            // Calcular la suma de todas las notas del alumno
            double suma = 0.0;
            for (double nota : notas) {
                suma += nota;
            }
            // Calcular la media
            double media = suma / notas.size();
            
            // Mostrar solo si la media es mayor o igual a 5 (aprobado)
            if (media >= 5) {
                System.out.println(alumno + " - " + media);
            }
        }
    }

    /**
     * Método que lee el archivo "datos.txt" y devuelve un HashMap con los alumnos y sus notas
     * Formato esperado del archivo: nombre_alumno;nota (una línea por cada nota)
     * 
     * @return HashMap donde la clave es el nombre del alumno y el valor es una lista de sus notas
     */
    public static HashMap<String, List<Double>> leerAlumnos() {
        HashMap<String, List<Double>> alumnos = new HashMap<>();
        
        // Try-with-resources para asegurar que el archivo se cierra correctamente
        try (BufferedReader reader = new BufferedReader(new FileReader("datos.txt"))) {
            String linea;

            // Leer el archivo línea por línea
            while ((linea = reader.readLine()) != null) {
                // Separar la línea por el carácter ';'
                String[] partes = linea.split(";");

                // Verificar que la línea tiene el formato correcto (nombre y nota)
                if (partes.length == 2) {
                    String alumno = partes[0].trim(); // Nombre del alumno (sin espacios)
                    double nota = Double.parseDouble(partes[1].trim()); // Nota (convertir a double)

                    // Si el alumno no existe en el mapa, crear una nueva lista para él
                    alumnos.putIfAbsent(alumno, new ArrayList<>());
                    // Añadir la nota a la lista del alumno
                    alumnos.get(alumno).add(nota);
                }
            }

        } catch (IOException e) {
            // Manejar posibles errores de lectura del archivo
            System.out.println("Ocurrió un error al leer el archivo: " + e.getMessage());
        }
        return alumnos;
    }

    /**
     * Método que muestra los alumnos ordenados por nota media de forma descendente
     * (de mayor a menor media)
     */
    public static void listarAlumnosOrdenado() {
        // Obtener los datos de los alumnos
        HashMap<String, List<Double>> alumnos = leerAlumnos();
        
        // Mapa para almacenar las medias de cada alumno
        Map<String, Double> medias = new HashMap<>();
        
        // Calcular la media para cada alumno
        for (Map.Entry<String, List<Double>> entry : alumnos.entrySet()) {
            double suma = 0;
            for (double nota : entry.getValue()) {
                suma += nota;
            }
            double media = suma / entry.getValue().size();
            medias.put(entry.getKey(), media);
        }
        
        // Convertir el mapa a una lista para poder ordenarlo
        List<Map.Entry<String, Double>> listaOrdenada =
                new ArrayList<>(medias.entrySet());

        // Ordenar la lista por valor (nota media) de forma descendente
        listaOrdenada.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        // Mostrar los alumnos ordenados
        for (Map.Entry<String, Double> entry : listaOrdenada) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    /**
     * Método que guarda los resultados (alumno y nota media) en un archivo llamado "resultado.txt"
     */
    public static void guardarFichero() {
        // Obtener los datos de los alumnos
        HashMap<String, List<Double>> alumnos = leerAlumnos();

        // Try-with-resources para escribir en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultado.txt"))) {

            // Recorrer cada alumno
            for (Map.Entry<String, List<Double>> entry : alumnos.entrySet()) {
                // Calcular la suma de las notas
                double suma = 0;
                for (double nota : entry.getValue()) {
                    suma += nota;
                }
                // Calcular la media
                double media = suma / entry.getValue().size();

                // Escribir en el archivo: nombre - media
                writer.write(entry.getKey() + " - " + media);
                writer.newLine(); // Añadir salto de línea
            }

            System.out.println("Resultados guardados correctamente en resultado.txt");

        } catch (IOException e) {
            // Manejar posibles errores de escritura
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}
