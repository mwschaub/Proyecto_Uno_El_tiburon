import java.io.*;
import java.util.*;

public class GestorCSV {
    private static final String NOMBRE_ARCHIVO = "contacts.csv";

    // Guardar un nuevo contacto en el archivo CSV con ID secuencial
    public static void guardarContacto(Contacto c) {
        List<Contacto> existentes = leerContactos();
        // Calcular siguiente ID basado solo en IDs numéricos
        int next = existentes.stream()
                .map(Contacto::getId)
                .filter(id -> id.matches("\\d+"))      // solo IDs numéricos
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        c.setId(Integer.toString(next));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, true))) {
            if (archivoVacio()) {
                writer.write("id,nombre,apellido,apodo,telefono,email,direccion,fecha_nacimiento\n");
            }
            writer.write(c.toCSV() + "\n");
        } catch (IOException e) {
            System.err.println("Error al guardar el contacto: " + e.getMessage());
        }
    }

    private static boolean archivoVacio() {
        File archivo = new File(NOMBRE_ARCHIVO);
        return !archivo.exists() || archivo.length() == 0;
    }

    public static List<Contacto> leerContactos() {
        List<Contacto> contactos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            String linea = reader.readLine(); // encabezado
            while ((linea = reader.readLine()) != null) {
                Contacto contacto = Contacto.fromCSV(linea);
                if (contacto != null) contactos.add(contacto);
            }
        } catch (IOException e) {
            // Si no existe archivo, devolvemos lista vacía
        }
        return contactos;
    }
}
