
package deposito;

import java.sql.ResultSet;
import java.sql.SQLException;
import inicio.Conexion;

/**
 *
 * @author USUARIO
 */
public class Depo {
    
    public static boolean depositar = false;
    Conexion con = new Conexion();
    Conexion con1 = new Conexion();
    ResultSet TempRevisar;
    
    
    
    public synchronized void Revisar () throws  InterruptedException, 
                                                ClassNotFoundException, 
                                                SQLException,
                                                InstantiationException,
                                                IllegalAccessException
    {
        if (!depositar) {
            con.ConexionPostgres();
            TempRevisar = con.consultar("SELECT * FROM cajeros  WHERE dinero < (cantidad_minima + (cantidad_minima * 0.05) )"
                    + " ORDER BY tipo_cajero desc, dinero;");
            while (TempRevisar.next())
            {
                depositar = true;
                notifyAll();
                while (depositar)
                {
                    wait();
                }
            }
            
            con.cerrar();
        }
        
    }
    
    public synchronized void Depositar () throws    InterruptedException, 
                                                    ClassNotFoundException, 
                                                    SQLException,
                                                    InstantiationException,
                                                    IllegalAccessException
    {
        while (!depositar)
        {
            wait();
        }
        
        if (depositar) {
            int id = TempRevisar.getInt("id_cajero");
            int dinero = TempRevisar.getInt("dinero");
            int maximo = TempRevisar.getInt("cantidad_maxima");
            int deposito = (int) (Math.random() * (maximo - dinero));
            
            
            String Actualiza = "UPDATE cajeros SET dinero = " + (deposito + dinero) 
                    + ", depositos = " + (TempRevisar.getInt("depositos") + 1)
                    + " WHERE id_cajero = " + id + " ;";
            String RegistroNuevo = "INSERT INTO transacciones(id_cajero, fecha, valor, saldo_cajero) VALUES ( " 
                    + id + ", now(), " + deposito + "," + (dinero + deposito) + " );";
            
            con1.ConexionPostgres();
            
            con1.actualizar(Actualiza);
            con1.actualizar(RegistroNuevo);
            con1.cerrar();
            depositar = false;
        }
        notifyAll();
    }
    
    
}
