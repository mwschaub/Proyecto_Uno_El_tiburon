import java.util.*;

public class ArbolAVL {
    private class Nodo {
        Contacto dato;
        Nodo izq, der;
        int altura;
        Nodo(Contacto c) { dato=c; altura=1; }
    }

    private Nodo raiz;
    private Comparator<Contacto> cmp;

    public ArbolAVL(Comparator<Contacto> cmp) { this.cmp=cmp; }

    public void insertar(Contacto c) { raiz = insertarRec(raiz, c); }

    private Nodo insertarRec(Nodo n, Contacto c) {
        if (n==null) return new Nodo(c);
        if (cmp.compare(c,n.dato)<0) n.izq=insertarRec(n.izq,c);
        else n.der=insertarRec(n.der,c);

        n.altura = 1 + Math.max(altura(n.izq), altura(n.der));
        int balance = getBalance(n);

        // LL
        if (balance>1 && cmp.compare(c,n.izq.dato)<0) return rotarDerecha(n);
        // RR
        if (balance<-1 && cmp.compare(c,n.der.dato)>0) return rotarIzquierda(n);
        // LR
        if (balance>1 && cmp.compare(c,n.izq.dato)>0) {
            n.izq = rotarIzquierda(n.izq);
            return rotarDerecha(n);
        }
        // RL
        if (balance<-1 && cmp.compare(c,n.der.dato)<0) {
            n.der = rotarDerecha(n.der);
            return rotarIzquierda(n);
        }
        return n;
    }

    private int altura(Nodo n) { return n==null?0:n.altura; }
    private int getBalance(Nodo n) { return n==null?0:altura(n.izq)-altura(n.der); }

    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izq;
        Nodo T2 = x.der;
        x.der = y; y.izq = T2;
        y.altura = Math.max(altura(y.izq),altura(y.der))+1;
        x.altura = Math.max(altura(x.izq),altura(x.der))+1;
        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.der;
        Nodo T2 = y.izq;
        y.izq = x; x.der = T2;
        x.altura = Math.max(altura(x.izq),altura(x.der))+1;
        y.altura = Math.max(altura(y.izq),altura(y.der))+1;
        return y;
    }

    public Contacto buscar(String clave) {
        return buscarRec(raiz, clave);
    }

    private Contacto buscarRec(Nodo n, String clave) {
        if (n==null) return null;
        int comp = cmp.compare(new ArbolBinarioBusqueda.ContactoStub(clave), n.dato);
        if (comp==0) return n.dato;
        return comp<0? buscarRec(n.izq,clave): buscarRec(n.der,clave);
    }

    public List<String> recorridoEnNivel() {
        List<String> ids = new ArrayList<>();
        if (raiz==null) return ids;
        Queue<Nodo> q = new LinkedList<>();
        q.add(raiz);
        while(!q.isEmpty()) {
            Nodo n = q.poll();
            ids.add(n.dato.getId());
            if(n.izq!=null) q.add(n.izq);
            if(n.der!=null) q.add(n.der);
        }
        return ids;
    }
}
