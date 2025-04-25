import java.util.*;

public class GestorContactos {
    private List<Contacto> contactos;
    private ArchivoContactos archivoContactos;
    private GestorIndices gestorIndices;
    private GestorIndices gi = new GestorIndices();
    private static final String CSV_FILE = "contacts.csv";
    public GestorContactos() {
        archivoContactos = new ArchivoContactos();
        gestorIndices = new GestorIndices();
        contactos = archivoContactos.cargar(); // si ya existe el archivo
    }

    // comparators por campo (puedes ampliar con otros campos si creas índices)
    private Map<String, Comparator<Contacto>> cmps = Map.of(
            "nombre", Comparator.comparing(Contacto::getNombre),
            "apellido", Comparator.comparing(Contacto::getApellido),
            "telefono", Comparator.comparing(Contacto::getTelefono)
    );

    public GestorIndices getGestorIndices() {
        return this.gi;
    }
    public void guardarContactosEnArchivo() {
        archivoContactos.guardar(contactos);
    }

    public void guardarIndicesEnArchivos() {
        gestorIndices.guardarIndices("nombres.txt", "telefonos.txt", "correos.txt");
    }


    /**
     * Agrega un contacto y actualiza todos los índices existentes.
     */
    public void agregarContacto(Contacto c) {
        GestorCSV.guardarContacto(c);
        // insertar en índices existentes
        cmps.forEach((campo, cmp) -> {
            if (gi.mapBst.containsKey(campo)) gi.mapBst.get(campo).insertar(c);
            if (gi.mapAvl.containsKey(campo)) gi.mapAvl.get(campo).insertar(c);
        });
    }

    /**
     * Elimina por ID y limpia los índices (se reconstruirán si es necesario).
     */
    public void eliminarContactoPorId(String id) {
        List<Contacto> todos = GestorCSV.leerContactos();
        List<Contacto> filtrados = new ArrayList<>();
        for (Contacto c : todos) if (!c.getId().equals(id)) filtrados.add(c);
        EscritorCSV.escribirContactosEnArchivo(CSV_FILE, filtrados);
        gi.mapBst.clear();
        gi.mapAvl.clear();
    }

    /**
     * Actualiza un contacto existente (mismo ID) pidiendo todos los campos.
     */
    public void actualizarContactoInteractivo(Scanner scanner, String id) {
        Contacto existente = buscarContacto("id", id);
        if (existente == null) {
            System.out.println("No encontrado.");
            return;
        }

        System.out.print("Nombre (" + existente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isBlank()) existente.setNombre(nombre);

        System.out.print("Apellido (" + existente.getApellido() + "): ");
        String apellido = scanner.nextLine();
        if (!apellido.isBlank()) existente.setApellido(apellido);

        System.out.print("Apodo (" + existente.getApodo() + "): ");
        String apodo = scanner.nextLine();
        if (!apodo.isBlank()) existente.setApodo(apodo);

        System.out.print("Teléfono (" + existente.getTelefono() + "): ");
        String telefono = scanner.nextLine();
        if (!telefono.isBlank()) existente.setTelefono(telefono);

        System.out.print("Email (" + existente.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isBlank()) existente.setEmail(email);

        System.out.print("Dirección (" + existente.getDireccion() + "): ");
        String direccion = scanner.nextLine();
        if (!direccion.isBlank()) existente.setDireccion(direccion);

        System.out.print("Fecha de nacimiento (DD-MM-YYYY) (" + existente.getFechaNacimiento() + "): ");
        String fechaNac = scanner.nextLine();
        if (!fechaNac.isBlank()) existente.setFechaNacimiento(fechaNac);

        actualizarContacto(existente);
        System.out.println("Contacto ID " + id + " actualizado.");
    }

    /**
     * Actualiza contacto directamente.
     */
    public void actualizarContacto(Contacto c) {
        eliminarContactoPorId(c.getId());
        agregarContacto(c);
    }

    /**
     * Retorna todos los contactos.
     */
    public List<Contacto> listarContactos() {
        return GestorCSV.leerContactos();
    }

    /**
     * Busca un contacto según campo y valor. Ahora soporta también 'id'.
     */
    public Contacto buscarContacto(String campo, String valor) {
        if ("id".equalsIgnoreCase(campo)) {
            return listarContactos().stream()
                    .filter(x -> x.getId().equals(valor))
                    .findFirst().orElse(null);
        }
        if (gi.mapBst.containsKey(campo)) {
            return gi.mapBst.get(campo).buscar(valor);
        }
        if (gi.mapAvl.containsKey(campo)) {
            return gi.mapAvl.get(campo).buscar(valor);
        }
        return listarContactos().stream()
                .filter(x -> {
                    switch (campo.toLowerCase()) {
                        case "nombre": return x.getNombre().equalsIgnoreCase(valor);
                        case "apellido": return x.getApellido().equalsIgnoreCase(valor);
                        case "telefono": return x.getTelefono().equals(valor);
                        default: return false;
                    }
                })
                .findFirst().orElse(null);
    }

    /**
     * Muestra todos los contactos o imprime mensaje si está vacía la lista.
     */
    public void mostrarTodos() {
        List<Contacto> lista = listarContactos();
        if (lista.isEmpty()) {
            System.out.println("Lista de contactos vacía");
            return;
        }
        for (Contacto c : lista) {
            System.out.println(c);
        }
    }
}
