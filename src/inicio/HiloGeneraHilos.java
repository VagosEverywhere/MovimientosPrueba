
package inicio;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import retiros.HiloRetiros;

/**
 *
 * @author USUARIO
 */
public class HiloGeneraHilos implements Runnable{
    
    Thread hilo;
    private final int tiempo;

    public HiloGeneraHilos(int tiempo) {
        this.tiempo = tiempo;
    }
    
    public void inicio()
    {
        if (hilo == null) {
            hilo = new Thread(this);
            hilo.start();
        }
    }
    
    @Override
    public void run() {
        Thread hiloactual = Thread.currentThread();
        while(hilo == hiloactual)
        {
            try {
                
                
                LinkedList <HiloRetiros> lista = new LinkedList<>();
                int cantidad = (int) (Math.random()*10);
                
                for (int i = 0; i < cantidad; i++) {
                    HiloRetiros HNuevo = new HiloRetiros(( ((int) (Math.random() * 100) +25) *10),5);
                    HNuevo.inicio();
                    lista.add(HNuevo);
                }
                
                
                pausa();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloGeneraHilos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void pausa () throws InterruptedException
    {
        Thread.sleep(tiempo);
    }
    
    public void stop()
    {
        hilo = null;
    }
}
