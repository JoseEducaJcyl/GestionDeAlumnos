import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sistema de Gestion de Alumnos");
        boolean seguir = true;
        while(seguir){
            System.out.println("-------MENU-------");
            System.out.println("1. Nota media de cada alumno");
            System.out.println("2. Listar Alumnos (Aprobados)");
            System.out.println("3. Listar Alumnos (Ordenado)");
            System.out.println("4. Guardar Resultados (Fichero resultado.txt)");
            System.out.println("5. Salir");
            System.out.println("Ingrese opcion: ");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    listarAlumnos();
                    break;
                case 2:
                    listarAprobados();
                    break;
                case 3:
                    listarAlumnosOrdenado();
                    break;
                case 4:
                    guardarFichero();
                    break;
                case 5:
                    System.out.println("Adios.");
                    seguir = false;
                    break;
                default:
                    System.out.println("Opcion incorrecta");
                    break;
            }
        }
    }
    public static void listarAlumnos() {
        for (String alumno : leerAlumnos().keySet()) {
            List<Double> notas = leerAlumnos().get(alumno);

            double suma = 0.0;
            for (double nota : notas) {
                suma += nota;
            }
            double media = suma / notas.size();
            System.out.println(alumno + " - " + media);
        }
    }

    public static void listarAprobados() {
        for (String alumno : leerAlumnos().keySet()) {
            List<Double> notas = leerAlumnos().get(alumno);

            double suma = 0.0;
            for (double nota : notas) {
                suma += nota;
            }
            double media = suma / notas.size();
            if (media >= 5) {
                System.out.println(alumno + " - " + media);
            }
        }
    }

    public static HashMap<String, List<Double>> leerAlumnos() {
        HashMap<String, List<Double>> alumnos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("datos.txt"))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");

                if (partes.length == 2) {
                    String alumno = partes[0].trim();
                    double nota = Double.parseDouble(partes[1].trim());

                    alumnos.putIfAbsent(alumno, new ArrayList<>());
                    alumnos.get(alumno).add(nota);
                }
            }

        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo: " + e.getMessage());
        }
        return alumnos;
    }

    public static void listarAlumnosOrdenado() {
        HashMap<String, List<Double>> alumnos = leerAlumnos();
        Map<String, Double> medias = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : alumnos.entrySet()) {
            double suma = 0;
            for (double nota : entry.getValue()) {
                suma += nota;
            }
            double media = suma / entry.getValue().size();
            medias.put(entry.getKey(), media);
        }
        List<Map.Entry<String, Double>> listaOrdenada =
                new ArrayList<>(medias.entrySet());

        listaOrdenada.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (Map.Entry<String, Double> entry : listaOrdenada) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public static void guardarFichero() {
        HashMap<String, List<Double>> alumnos = leerAlumnos();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultado.txt"))) {

            for (Map.Entry<String, List<Double>> entry : alumnos.entrySet()) {
                double suma = 0;
                for (double nota : entry.getValue()) {
                    suma += nota;
                }
                double media = suma / entry.getValue().size();

                writer.write(entry.getKey() + " - " + media);
                writer.newLine();
            }

            System.out.println("Resultados guardados correctamente en resultado.txt");

        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

}