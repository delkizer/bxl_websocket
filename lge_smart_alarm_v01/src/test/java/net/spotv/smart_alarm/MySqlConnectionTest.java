package net.spotv.smart_alarm;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;

public class MySqlConnectionTest {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    // DB 경로
    private static final String URL = "jdbc:mysql://spotvnow-staging-aurora-cluster.cluster-ro-cjvsdh8d84ff.ap-northeast-2.rds.amazonaws.com:3306/spotvnow_renewal_stg?useSSL=false&serverTimezone=UTC";
    private static final String USER = "dbadmin";
    private static final String PASSWORD = "dhfhfkrhksfl!";
	
    @Test
    public void testConnection() throws Exception {
    	Class.forName(DRIVER);
    	try { 
    		Connection connection = DriverManager.getConnection(URL,USER, PASSWORD);
    		System.out.println( connection );
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    }

}
