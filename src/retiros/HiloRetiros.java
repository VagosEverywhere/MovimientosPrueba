/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package retiros;
import inicio.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class HiloRetiros implements Runnable{

    Thread hilo;
    int tiempo ;
    int prioridad;
    
    Conexion con1  = new Conexion(), con2 = new Conexion(), con3 = new Conexion(), con4 = new Conexion(), ConActualizar = new Conexion();
    
    public HiloRetiros(int tiempo, int prioridad) {
        this.tiempo = tiempo;
        this.prioridad = prioridad;
    }
    
    public void inicio() {
        if (hilo == null) {
            hilo = new Thread(this);
            hilo.setPriority(prioridad);
            hilo.start();
        }
    }
    
    public void pausa (int TiempoEspera) throws InterruptedException
    {
        Thread.sleep(TiempoEspera);
    }
    
//    public void pausa () throws InterruptedException
//    {
//        Thread.sleep(tiempo);
//    }
    
    
//    public ResultSet mm (ResultSet temp)
//    {
//        
//        
//        
//        return temp;
//    }

    @Override
    public void run() {
        Thread hiloactual = Thread.currentThread();
        ResultSet TempCajeros;
        ResultSet TempClientes;
        ResultSet TempCantCaj;
        ResultSet TempCantCli;
        
        while (hilo == hiloactual) {
            try {
                con1.ConexionPostgres();
                con2.ConexionPostgres();
                con3.ConexionPostgres();
                con4.ConexionPostgres();
                
                ConActualizar.ConexionPostgres();
                
                TempCajeros = con1.consultar("SELECT id_cajero, cantidad_minima, cantidad_maxima, dinero, estado, retiros FROM cajeros;");
                TempClientes = con2.consultar("SELECT clave, saldo, retiros  FROM clientes;");
                TempCantCli = con3.consultar("SELECT count(*) FROM clientes;");
                TempCantCaj = con4.consultar("SELECT count(*) FROM cajeros;");
                
                TempCantCli.next();
                TempCantCaj.next();
                
                int poscliente = (int) (Math.random() * TempCantCli.getInt(1)) + 1;
                for (int i = 0; i < poscliente; i++) {
                    TempClientes.next();
                }
                boolean cr = (TempCantCaj.getInt(1) > 0) && (TempCantCli.getInt(1) > 0);
                
                
                if (cr){
                    int nn = 0;
                    do{
                        if (nn > 77) {
                            cr = false;
                            break;
                        }
                        int poscajero = (int) (Math.random() * TempCantCaj.getInt(1)) + 1;
                        TempCajeros.beforeFirst();

                        for (int i = 0; i < poscajero; i++) {
                            TempCajeros.next();
                        }

                        nn++;
                    }while (!TempCajeros.getBoolean("estado"));
                }
                
                if (cr) {
                    ConActualizar.actualizar("UPDATE cajeros SET estado = false WHERE id_cajero = " + TempCajeros.getInt("id_cajero") + " ;");
                    
                    int saldo = TempClientes.getInt("saldo");
                    int dinero = TempCajeros.getInt("dinero");
                    
                    int retiro = (int)(Math.random() * ( dinero < saldo  ? dinero : saldo ));
                    
                    if (retiro  > 10000) {
                        
                        int tiemporetiro = (int) (Math.random() * 910) + 10 ;
                        
                        ConActualizar.actualizar("INSERT INTO transacciones(id_cajero, clave, fecha, valor, saldo_cajero, saldo_cliente, tiempo)VALUES ( "
                                + TempCajeros.getInt("id_cajero") + " , " + TempClientes.getInt("clave") + " , now(), -" 
                                + retiro + " , " + (dinero - retiro) + " , " + (saldo - retiro) + ", " + tiemporetiro + "  );");
                        
                        
                        
                        
                        ConActualizar.actualizar("UPDATE cajeros SET dinero = " + (dinero - retiro) + " , estado = true, retiros = " 
                                + (TempCajeros.getInt("retiros") + 1) + " WHERE id_cajero = " + TempCajeros.getInt("id_cajero") + " ;");
                        
                        ConActualizar.actualizar("UPDATE clientes SET  saldo = " + (saldo - retiro) + ", retiros = " + (TempClientes.getInt("retiros") + 1) + " WHERE clave = " + TempClientes.getInt("clave") + " ;");
                        
                        //pausa(tiemporetiro);
                    }                    
                }
                
                
                
                con1.cerrar();
                con2.cerrar();
                con3.cerrar();
                con4.cerrar();
                
                ConActualizar.cerrar();
                
                //pausa();
                
            } catch (    SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException  ex) {
                Logger.getLogger(HiloRetiros.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
