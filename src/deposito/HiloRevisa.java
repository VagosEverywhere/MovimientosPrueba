
package deposito;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class HiloRevisa implements Runnable{
    
    Thread hilo;
    final int prioridad = 9;
    private final int tiempo;
    private final Depo sincro;
    
    public HiloRevisa (int t, Depo p)
    {
        this.tiempo = t;
        this.sincro = p;
    }
    
    public void inicio ()
    {
        if (hilo == null) {
            hilo = new Thread(this);
            hilo.setPriority(prioridad);
            hilo.start();
        }
    }
    
    public void pausa() throws InterruptedException
    {
        hilo.sleep(tiempo);
    }
    
    public void stop()
    {
        hilo = null;
    }
    
    @Override
    public void run() {
        Thread hiloactual = Thread.currentThread();
        while (hilo == hiloactual)
        {
            try {
                
                sincro.Revisar();
                    
                
                
                
                pausa();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloRevisa.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HiloRevisa.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HiloRevisa.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(HiloRevisa.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HiloRevisa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
