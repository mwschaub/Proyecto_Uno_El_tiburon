import java.io.*;
import java.util.*;
public class EscritorCSV {
    public static void escribirContactosEnArchivo(String ruta, List<Contacto> contactos) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(ruta))) {
            w.write("id,nombre,apellido,apodo,telefono,email,direccion,fecha_nacimiento\n");
            for (Contacto c: contactos) w.write(c.toCSV()+"\n");
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    public static void escribirIndice(String ruta, List<String> ids) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(ruta))) {
            for (String id: ids) w.write(id + "\n");
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}
