import java.util.*;

public class MenuPrincipal {
    private static final Scanner sc = new Scanner(System.in);
    private static final GestorContactos gestor = new GestorContactos();

    public static void main(String[] args) {
        boolean cont = true;
        while (cont) {
            mostrarMenu();
            String op = sc.nextLine();
            try {
                switch (op) {
                    case "1":
                        agregarContacto();
                        break;
                    case "2":
                        eliminarContacto();
                        break;
                    case "3":
                        actualizarContacto();
                        break;
                    case "4":
                        buscarContacto();
                        break;
                    case "5":
                        verTodosLosContactos();
                        break;
                    case "6":
                        importarCSV();
                        break;
                    case "7":
                        exportarCSV();
                        break;
                    case "8":
                        gestionarIndices();
                        break;
                    case "9":
                        cont = false;
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
                esperarEnterParaContinuar();
            }
        }
        sc.close();
    }

    private static void esperarEnterParaContinuar() {
        System.out.println("Presiona enter para regresar al menú...");
        sc.nextLine();
    }

    private static void agregarContacto() {
        System.out.println("=== Agregar nuevo contacto ===");
        System.out.print("Nombre: "); String n = sc.nextLine();
        System.out.print("Apellido: "); String a = sc.nextLine();
        System.out.print("Apodo: "); String p = sc.nextLine();
        System.out.print("Teléfono: "); String t = sc.nextLine();
        System.out.print("Email: "); String e = sc.nextLine();
        System.out.print("Dirección: "); String d = sc.nextLine();
        System.out.print("Fecha Nac (DD-MM-YYYY): "); String f = sc.nextLine();
        gestor.agregarContacto(new Contacto(n,a,p,t,e,d,f));
        System.out.println("Contacto guardado.");
        esperarEnterParaContinuar();
    }

    private static void eliminarContacto() {
        System.out.print("ID a eliminar: "); String id = sc.nextLine();
        if (id.isBlank()) {
            System.out.println("Opción no válida. Intente nuevamente.");
        } else {
            gestor.eliminarContactoPorId(id);
            System.out.println("Operación de eliminación completada.");
        }
        esperarEnterParaContinuar();
    }

    private static void actualizarContacto() {
        System.out.print("ID a actualizar: "); String id = sc.nextLine();
        gestor.getGestorIndices().cargarIndicesDesdeArchivos();
        gestor.actualizarContactoInteractivo(sc, id);
        esperarEnterParaContinuar();
    }

    private static void buscarContacto() {
        System.out.print("ID a buscar: "); String id = sc.nextLine();
        Contacto res = gestor.buscarContacto("id", id);
        System.out.println(res != null ? res : "No existe contacto con ID " + id);
        esperarEnterParaContinuar();
    }

    private static void verTodosLosContactos() {
        gestor.mostrarTodos();
        esperarEnterParaContinuar();
    }

    private static void importarCSV() {
        System.out.print("Ruta de archivo CSV a importar: ");
        String ruta = sc.nextLine();
        if (ruta.isBlank()) {
            System.out.println("Opción no válida. Intente nuevamente.");
        } else {
            List<Contacto> imp = LectorCSV.leerContactosDesdeArchivo(ruta);
            for (Contacto c : imp) gestor.agregarContacto(c);
            System.out.println("Importación finalizada.");
        }
        esperarEnterParaContinuar();
    }

    private static void exportarCSV() {
        EscritorCSV.escribirContactosEnArchivo("contacts-export.csv", gestor.listarContactos());
        System.out.println("Exportado a contacts-export.csv");
        esperarEnterParaContinuar();
    }

    private static void gestionarIndices() {
        System.out.print("Campo para índice (nombre/apellido/telefono): ");
        String campo = sc.nextLine();
        System.out.print("Tipo (BST/AVL): ");
        String tipo = sc.nextLine();
        Comparator<Contacto> cmp;
        switch (campo.toLowerCase()) {
            case "nombre":   cmp = Comparator.comparing(Contacto::getNombre); break;
            case "apellido": cmp = Comparator.comparing(Contacto::getApellido); break;
            default:          cmp = Comparator.comparing(Contacto::getTelefono); break;
        }
        gestor.getGestorIndices().crearIndice(campo, tipo, cmp);
        gestor.getGestorIndices().guardarIndice(campo, tipo);
        System.out.println("Índice " + campo + "-" + tipo + " creado.");
        esperarEnterParaContinuar();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║       LIBRETA DE CONTACTOS   ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ 1. Agregar contacto          ║");
        System.out.println("║ 2. Eliminar contacto         ║");
        System.out.println("║ 3. Actualizar contacto       ║");
        System.out.println("║ 4. Buscar contacto           ║");
        System.out.println("║ 5. Ver todos los contactos   ║");
        System.out.println("║ 6. Importar CSV              ║");
        System.out.println("║ 7. Exportar CSV              ║");
        System.out.println("║ 8. Crear/Reconstruir Índice  ║");
        System.out.println("║ 9. Salir                     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }
}
