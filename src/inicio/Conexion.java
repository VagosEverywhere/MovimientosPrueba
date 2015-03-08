package inicio;

import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexion {

    Connection conexion;
    Statement sentencia;
    String usuario = "postgres", password = "mkoaskiap", iP = "localhost", puerto = "5432", nombreBD = "banco";

    public Conexion() {
        this.usuario = usuario;
        this.password = password;
        this.iP = iP;
        this.puerto = puerto;
        this.nombreBD = nombreBD;
    }

	// sw es true si va a conectar con MySQL (3306) y postgresql (5432)
    // sw es false si va a conectar con access porque es por medio del odbc
    private void conectar(String driver, String puente, boolean sw)
            throws ClassNotFoundException,
            SQLException,
            InstantiationException,
            IllegalAccessException {

        Class.forName(driver).newInstance();
        if (sw) {
            conexion = DriverManager.getConnection("jdbc:" + puente
                    + "://" + iP
                    + ":" + puerto
                    + "/" + nombreBD, usuario, password);
        } else {
            conexion = DriverManager.getConnection("jdbc:" + puente
                    + ":" + nombreBD, usuario, password);
        }
        sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
    }

    // Con JDBC
    public void ConexionJDBC() throws ClassNotFoundException,
            SQLException,
            InstantiationException,
            IllegalAccessException {
        conectar("sun.jdbc.odbc.JdbcOdbcDriver", "odbc", false);
    }

    // Con MySQL
    public void ConexionMySQL() throws ClassNotFoundException,
            SQLException,
            InstantiationException,
            IllegalAccessException {
        conectar("com.mysql.jdbc.Driver", "mysql", true);
    }

    // Con PostgreSql
    public void ConexionPostgres() throws ClassNotFoundException,
            SQLException,
            InstantiationException,
            IllegalAccessException {
        conectar("org.postgresql.Driver", "postgresql", true);
    }

    public void ConexionOracle(String driver, String cadena_conexion, String usuario, String clave) throws ClassNotFoundException,
            SQLException,
            InstantiationException,
            IllegalAccessException {
	  //Class.forName("oracle.jdbc.driver.OracleDriver");

      //conexion = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521", "provision", "provision");
        Class.forName(driver);
        conexion = DriverManager.getConnection(cadena_conexion, usuario, clave);
        sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

//    Statement st=conexion.createStatement();              
    }

    public void actualizar(String actualiza) throws SQLException {
        sentencia.executeUpdate(actualiza);
    }

    public int actualizar1(String actualiza) throws SQLException {
        int s = sentencia.executeUpdate(actualiza);

        return s;
    }

    public ResultSet consultar(String consulta) throws SQLException {
        return (sentencia.executeQuery(consulta));
    }

    // Devuelve el numero de filas de la tabla virtual
    public int contar(ResultSet rS) throws SQLException {
        int cont = 0;
        rS.beforeFirst();
        while (rS.next()) {
            cont++;
        }
        return (cont);
    }

    public void cerrar() throws SQLException {
        conexion.close();
        sentencia.close();
    }
}
