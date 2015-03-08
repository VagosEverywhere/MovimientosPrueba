
package inicio;

import deposito.Depo;
import deposito.HiloRevisa;
import deposito.HiloDeposita;
import retiros.HiloRetiros;
import consignaciones.HiloConsignaciones;

/**
 *
 * @author MARLON
 */
public class PrincipalMovimientosPrueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Depo Depos = new Depo();
        HiloRevisa HiloRev = new HiloRevisa(2000, Depos);
        HiloDeposita HiloDep = new HiloDeposita(Depos);
        
        //HiloGeneraHilos HilosRet = new HiloGeneraHilos(100);
        HiloRetiros hh1 = new HiloRetiros(333,5);
        HiloRetiros hh2 = new HiloRetiros(333,4);
        
        HiloConsignaciones hcon1 = new HiloConsignaciones(1000);
        HiloConsignaciones hcon2 = new HiloConsignaciones(1000);
        
        HiloRev.inicio();
        HiloDep.inicio();
        hh1.inicio();
        hh2.inicio();
        hcon1.inicio();
        hcon2.inicio();
        
        //HilosRet.inicio();
        
    }
    
}
