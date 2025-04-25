import java.io.*;
import java.util.List;

public class ArchivoContactos {
    private static final String NOMBRE_ARCHIVO = "contactos.dat";

    public void guardar(List<Contacto> contactos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO))) {
            oos.writeObject(contactos);
        } catch (IOException e) {
            System.out.println("Error al guardar contactos: " + e.getMessage());
        }
    }

    public List<Contacto> cargar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO))) {
            return (List<Contacto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo cargar contactos. Se iniciará una lista vacía.");
            return new java.util.ArrayList<>();
        }
    }
}
