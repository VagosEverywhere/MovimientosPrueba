

package consignaciones;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import inicio.Conexion;

/**
 *
 * @author USUARIO
 */
public class HiloConsignaciones implements Runnable{
    
    Thread hilo;
    Conexion con1 = new Conexion(), con2 = new Conexion(), ConActualizar = new Conexion();
    int tiempo;

    public HiloConsignaciones(int tiempo) {        
        this.tiempo = tiempo;
    }
    
    public void inicio (){
        if (hilo == null) {
            hilo = new Thread(this);
            hilo.start();
        }
    }
    
    public void pausa () throws InterruptedException{
        Thread.sleep(tiempo);
    }
    
    @Override
    public void run() {
        Thread hiloactual = Thread.currentThread();
        ResultSet temp, tempcant;
        
        while (hilo == hiloactual) {
            try {
                con1.ConexionPostgres();
                con2.ConexionPostgres();
                ConActualizar.ConexionPostgres();
                temp = con1.consultar("SELECT clave, saldo, consignaciones FROM clientes;");
                Random aleatorio = new Random();
                
                tempcant = con2.consultar("SELECT count(*) FROM clientes;");
                tempcant.next();
                    
                if (aleatorio.nextBoolean() && (tempcant.getInt(1) > 0)) {
                    //temp = con1.consultar("SELECT clave, saldo FROM clientes;");
                    
                    int cl = (int) (Math.random() * tempcant.getInt(1)) + 1;
                    
                    for (int i = 0; i < cl; i++) {
                        temp.next();
                    }
                    
                    int consignacion =  (int)(aleatorio.nextDouble() * 10000000) + 10000;
                    int saldo = temp.getInt("saldo");
                    int clave =  temp.getInt("clave");
                    
                    ConActualizar.actualizar("INSERT INTO transacciones (clave, fecha, valor, saldo_cliente) VALUES ( " + clave 
                            + ", now(), " + consignacion + ", " + (saldo + consignacion) + " )");
                    ConActualizar.actualizar("UPDATE clientes SET  saldo = " + (saldo + consignacion) 
                            + ", consignaciones = " + (temp.getInt("consignaciones") + 1)
                            + "  WHERE clave = " + clave + ";");
                    
                }
                
                con1.cerrar();
                con2.cerrar();
                ConActualizar.cerrar();
                
                pausa();
                
            } catch (    ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | InterruptedException ex) {
                Logger.getLogger(HiloConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }
    
}
