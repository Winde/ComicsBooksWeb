/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.winde.comicsweb.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Winde
 */
public class Config {

    private BasicDataSource datasource;
    private static Map<String, String> cachedValues = new HashMap<String, String>() {

        {
            put("pathComics", "C:\\Comics");
            put("pathLibros", "C:\\Libros");
            put("keepLastRead", "true");
        }
    };
    private static Map<String, Boolean> initialized = new HashMap<String, Boolean>() {

        {
            put("pathComics", false);
            put("pathLibros", false);
            put("keepLastRead", false);
        }
    };

    public void setDatasource(BasicDataSource datasource) {
        this.datasource = datasource;
    }

    private void createConfigTableIfNotExists(Connection connection) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS config (key VARCHAR(100),value VARCHAR(500))");
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getConfigValue(String key) {
        if (initialized.get(key)) {
            return cachedValues.get(key);
        } else {
            Connection connection = null;
            try {
                connection = datasource.getConnection();
                createConfigTableIfNotExists(connection);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT value FROM config WHERE key=?");
                preparedStatement.setString(1, key);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String salida = rs.getString("value");
                    connection.close();
                    initialized.put(key, true);
                    cachedValues.put(key, salida);
                    return salida;
                } else {
                    setConfigValue(key, cachedValues.get(key));
                    initialized.put(key, true);
                    connection.close();
                    return cachedValues.get(key);
                }

            } catch (SQLException ex) {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex1) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cachedValues.get(key);
    }

    public boolean setConfigValue(String key, String value) {
        Connection connection = null;
        if (value.equals(cachedValues.get(key)) && Boolean.TRUE.equals(initialized.get(key))) {
            return true;
        } else {
            cachedValues.put(key, value);
            initialized.put(key, true);
            try {
                connection = datasource.getConnection();
                createConfigTableIfNotExists(connection);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT value FROM config WHERE key=?;");
                preparedStatement.setString(1, key);
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    preparedStatement = connection.prepareStatement("INSERT INTO config (key, value) VALUES (?, ?);");
                    preparedStatement.setString(1, key);
                    preparedStatement.setString(2, value);
                    preparedStatement.execute();
                } else {
                    preparedStatement = connection.prepareStatement("UPDATE config SET value=? WHERE  key=?; ");
                    preparedStatement.setString(1, value);
                    preparedStatement.setString(2, key);
                    preparedStatement.execute();
                }
                connection.close();
                return true;
            } catch (SQLException ex) {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex1) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return false;
    }
}
