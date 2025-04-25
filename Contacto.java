import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.io.Serializable;


public class Contacto implements Serializable {
    private String id;
    private String nombre;
    private String apellido;
    private String apodo;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaNacimiento;

    // Formatos de fecha
    private static final DateTimeFormatter FORMATO_ENTRADA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATO_SALIDA  = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Constructor para nuevos (id se asigna luego)
    public Contacto(String nombre, String apellido, String apodo,
                    String teléfono, String email, String dirección,
                    String fechaNacimiento) {
        validarFormato(teléfono, email, fechaNacimiento);
        this.nombre  = nombre;
        this.apellido= apellido;
        this.apodo   = apodo;
        this.telefono= teléfono;
        this.email   = email;
        this.direccion = dirección;
        // parseamos fecha de entrada yyyy-MM-dd
        this.fechaNacimiento = LocalDate.parse(fechaNacimiento, FORMATO_ENTRADA);
    }

    // Constructor desde CSV (incluye id y fecha salida formato dd-MM-yyyy)
    public Contacto(String id, String nombre, String apellido, String apodo,
                    String telefono, String email, String direccion,
                    String fechaNacimiento) {
        validarFormato(telefono, email, fechaNacimiento);
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.apodo    = apodo;
        this.telefono = telefono;
        this.email    = email;
        this.direccion= direccion;
        // al leer CSV la fecha vendrá en formato dd-MM-yyyy
        this.fechaNacimiento = LocalDate.parse(fechaNacimiento, FORMATO_SALIDA);
    }

    // Setter de ID
    public void setId(String id) {
        this.id = id;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getApodo() { return apodo; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public String getFechaNacimiento() { return fechaNacimiento.format(FORMATO_SALIDA); }

    // Setters con validación
    public void setNombre(String n) { this.nombre = n; }
    public void setApellido(String a) { this.apellido = a; }
    public void setApodo(String a) { this.apodo = a; }
    public void setTelefono(String t) { validarFormato(t, email, getFechaNacimiento()); this.telefono = t; }
    public void setEmail(String e) { validarFormato(telefono, e, getFechaNacimiento()); this.email = e; }
    public void setDireccion(String d) { this.direccion = d; }
    public void setFechaNacimiento(String f) {
        validarFormato(telefono, email, f);
        this.fechaNacimiento = LocalDate.parse(f, FORMATO_SALIDA);
    }

    // Convertir a CSV: fecha en dd-MM-yyyy
    public String toCSV() {
        return String.join(",",
                id,
                nombre,
                apellido,
                apodo,
                telefono,
                email,
                direccion,
                fechaNacimiento.format(FORMATO_SALIDA)
        );
    }

    // Crear desde línea CSV
    public static Contacto fromCSV(String linea) {
        String[] campos = linea.split(",", -1);
        if (campos.length < 8) return null;
        return new Contacto(
                campos[0], campos[1], campos[2], campos[3],
                campos[4], campos[5], campos[6], campos[7]
        );
    }

    // Validar teléfono, email y fecha (entrada o salida)
    private void validarFormato(String tel, String mail, String fecha) {
        if (!tel.matches("\\+?\\d+"))
            throw new IllegalArgumentException("Teléfono inválido");
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", mail))
            throw new IllegalArgumentException("Email inválido");
        // fecha puede ser yyyy-MM-dd o dd-MM-yyyy
        if (!(fecha.matches("\\d{4}-\\d{2}-\\d{2}") || fecha.matches("\\d{2}-\\d{2}-\\d{4}")))
            throw new IllegalArgumentException("Fecha inválida, use yyyy-MM-dd o dd-MM-yyyy");
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %s\nNombre: %s\nApellido: %s\nApodo: %s\nTeléfono: %s\nEmail: %s\nDirección: %s\nFecha Nac.: %s\n",
                id, nombre, apellido, apodo, telefono, email, direccion, fechaNacimiento.format(FORMATO_SALIDA)
        );
    }
}
