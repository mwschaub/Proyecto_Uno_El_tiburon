import java.util.*;

public class ArbolBinarioBusqueda {
    private class Nodo {
        Contacto dato;
        Nodo izquierdo, derecho;
        Nodo(Contacto c) { this.dato = c; }
    }

    private Nodo raiz;
    private Comparator<Contacto> cmp;

    public ArbolBinarioBusqueda(Comparator<Contacto> cmp) {
        this.cmp = cmp;
    }

    public void insertar(Contacto c) {
        raiz = insertarRec(raiz, c);
    }

    private Nodo insertarRec(Nodo nodo, Contacto c) {
        if (nodo == null) return new Nodo(c);
        if (cmp.compare(c, nodo.dato) < 0)
            nodo.izquierdo = insertarRec(nodo.izquierdo, c);
        else
            nodo.derecho = insertarRec(nodo.derecho, c);
        return nodo;
    }

    public Contacto buscar(String clave) {
        return buscarRec(raiz, clave);
    }

    private Contacto buscarRec(Nodo nodo, String clave) {
        if (nodo == null) return null;
        int comp = cmp.compare(new ContactoStub(clave), nodo.dato);
        if (comp == 0) return nodo.dato;
        if (comp < 0) return buscarRec(nodo.izquierdo, clave);
        return buscarRec(nodo.derecho, clave);
    }

    public List<String> recorridoEnNivel() {
        List<String> ids = new ArrayList<>();
        if (raiz == null) return ids;
        Queue<Nodo> q = new LinkedList<>();
        q.add(raiz);
        while (!q.isEmpty()) {
            Nodo n = q.poll();
            ids.add(n.dato.getId());
            if (n.izquierdo != null) q.add(n.izquierdo);
            if (n.derecho   != null) q.add(n.derecho);
        }
        return ids;
    }

    // Stub para comparar solo por clave
    public static class ContactoStub extends Contacto {
        private String clave;
        ContactoStub(String clave) { super("","", "", "", "", "", ""); this.clave=clave; }
        @Override public String getApellido() { return clave; }
        @Override public String getNombre() { return clave; }
        @Override public String getTelefono() { return clave; }
    }
}
