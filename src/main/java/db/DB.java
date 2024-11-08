package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    public static Connection getConnection(){
      try{
          if(conn == null){
              String url = loadProperties().getProperty("dbUrl");
              Properties properties = loadProperties();
              conn = DriverManager.getConnection(url, properties);
          }
          return conn;
      }catch (SQLException e){
          throw new DbException(e.getMessage());
      }
    }

    public static void closeConnection(){
        try{
            conn.close();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    private static Properties loadProperties(){
        try(FileInputStream fs = new FileInputStream("db.properties")){
            Properties props = new Properties();
            props.load(fs);

            return props;
        }catch (IOException e){
            throw new DbException(e.getMessage());
        }
    }
}
