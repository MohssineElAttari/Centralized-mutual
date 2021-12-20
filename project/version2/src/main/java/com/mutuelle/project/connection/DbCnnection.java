package com.mutuelle.project.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbCnnection {
    public Connection databaseLink;

    private String databaseName;
    private String databaseUser;
    private String databasePassword;

    public Connection getConnection(String databaseName,String databaseUser,String databasePassword){
        String url = "jdbc:mysql://localhost/"+databaseName;
        try{
            //mysql.cj.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
