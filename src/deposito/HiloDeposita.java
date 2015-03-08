
package deposito;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class HiloDeposita implements Runnable{
    
    Thread hilo;
    int prioridad = 10;
    private final Depo sincro  ;
    
    
    public HiloDeposita (Depo p) { 
        this.sincro = p;
    }
    
    public void inicio()
    {
        if (hilo == null) {
            hilo = new Thread(this);
            hilo.setPriority(prioridad);
            hilo.start();
        }
    }
    
    public void stop ()
    {
        hilo = null;
    }
    
    public void pausa() throws InterruptedException
    {
        hilo.sleep(2000);
    }
    
    @Override
    public void run() {
        Thread hiloactual = Thread.currentThread();
        
        while (hilo == hiloactual)
        {
            try {
                sincro.Depositar();
                
                    
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloDeposita.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HiloDeposita.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HiloDeposita.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(HiloDeposita.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(HiloDeposita.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
