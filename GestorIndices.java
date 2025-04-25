import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GestorIndices {

    private List<String> indicesNombres;
    private List<String> indicesTelefonos;
    private List<String> indicesCorreos;

    public void guardarIndices(String archivoNombres, String archivoTelefonos, String archivoCorreos) {
        try (FileWriter fwNombres = new FileWriter(archivoNombres);
             FileWriter fwTelefonos = new FileWriter(archivoTelefonos);
             FileWriter fwCorreos = new FileWriter(archivoCorreos)) {

            for (String nombre : indicesNombres) {
                fwNombres.write(nombre + "\n");
            }

            for (String telefono : indicesTelefonos) {
                fwTelefonos.write(telefono + "\n");
            }

            for (String correo : indicesCorreos) {
                fwCorreos.write(correo + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error al guardar los índices: " + e.getMessage());
        }
    }
    // Mapas de índices
    Map<String, ArbolBinarioBusqueda> mapBst = new HashMap<>();
    Map<String, ArbolAVL> mapAvl = new HashMap<>();

    /**
     * Crea un índice sobre el campo y tipo indicado.
     * @param campo Nombre del campo (e.g. "apellido").
     * @param tipoEstructura "BST" o "AVL".
     * @param cmp Comparador para el campo.
     */
    public void crearIndice(String campo, String tipoEstructura, Comparator<Contacto> cmp) {
        List<Contacto> todos = GestorCSV.leerContactos();
        if ("BST".equalsIgnoreCase(tipoEstructura)) {
            ArbolBinarioBusqueda arbol = new ArbolBinarioBusqueda(cmp);
            for (Contacto c : todos) arbol.insertar(c);
            mapBst.put(campo, arbol);
        } else {
            ArbolAVL arbol = new ArbolAVL(cmp);
            for (Contacto c : todos) arbol.insertar(c);
            mapAvl.put(campo, arbol);
        }
    }

    /**
     * Guarda en un archivo de texto el recorrido en nivel del índice.
     */
    public void guardarIndice(String campo, String tipo) {
        List<String> ids;
        if ("BST".equalsIgnoreCase(tipo)) {
            ids = mapBst.get(campo).recorridoEnNivel();
        } else {
            ids = mapAvl.get(campo).recorridoEnNivel();
        }
        String nombreArchivo = campo.toLowerCase() + "-" + tipo.toLowerCase() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String id : ids) {
                writer.write(id);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar índice: " + e.getMessage(), e);
        }
    }
    private Comparator<Contacto> obtenerComparador(String campo) {
        switch (campo.toLowerCase()) {
            case "apellido": return Comparator.comparing(Contacto::getApellido);
            case "nombre": return Comparator.comparing(Contacto::getNombre);
            case "telefono": return Comparator.comparing(Contacto::getTelefono);
            // Agrega otros campos según tus necesidades
            default: return null;
        }
    }

    public void cargarIndicesDesdeArchivos() {
        File carpetaActual = new File(".");
        File[] archivos = carpetaActual.listFiles((dir, name) ->
                name.endsWith("-bst.txt") || name.endsWith("-avl.txt")
        );

        if (archivos == null) return;

        for (File archivo : archivos) {
            String nombre = archivo.getName(); // ejemplo: apellido-bst.txt
            String[] partes = nombre.split("-");
            if (partes.length != 2) continue;

            String campo = partes[0];
            String tipo = partes[1].replace(".txt", "");

            Comparator<Contacto> cmp = obtenerComparador(campo);
            if (cmp != null) {
                cargarIndice(campo, tipo.toUpperCase(), cmp);
                System.out.println("Índice cargado: " + campo + " (" + tipo.toUpperCase() + ")");
            }
        }
    }

    /**
     * Carga un índice desde un archivo de recorrido en nivel.
     */
    public void cargarIndice(String campo, String tipo, Comparator<Contacto> cmp) {
        List<String> ids = new ArrayList<>();
        String nombreArchivo = campo.toLowerCase() + "-" + tipo.toLowerCase() + ".txt";
        try (Scanner sc = new Scanner(new java.io.File(nombreArchivo))) {
            while (sc.hasNextLine()) {
                ids.add(sc.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar índice: " + e.getMessage(), e);
        }
        // Reconstruir el árbol según el tipo
        if ("BST".equalsIgnoreCase(tipo)) {
            ArbolBinarioBusqueda arbol = new ArbolBinarioBusqueda(cmp);
            for (String id : ids) {
                Contacto c = GestorCSV.leerContactos().stream()
                        .filter(x -> x.getId().equals(id))
                        .findFirst().orElse(null);
                if (c != null) arbol.insertar(c);
            }
            mapBst.put(campo, arbol);
        } else {
            ArbolAVL arbol = new ArbolAVL(cmp);
            for (String id : ids) {
                Contacto c = GestorCSV.leerContactos().stream()
                        .filter(x -> x.getId().equals(id))
                        .findFirst().orElse(null);
                if (c != null) arbol.insertar(c);
            }
            mapAvl.put(campo, arbol);
        }
    }
}
