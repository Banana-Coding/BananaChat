/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011-2013  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf verändert, aber weder in andere Projekte eingefügt noch
 * reproduziert werden.
 *
 * Der Emulator dient - sofern der Client nicht aus Eigenproduktion
 * stammt - nur zu Lernzwecken, das Hosten des originalen Clients
 * ist untersagt und wird der Knuddels GmbH gemeldet.
 */

package tools.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * @author Flav
 */

public class PoolConnection {
	private final static String url, user, password;

	private Connection con;
	private boolean inUse;

	static {
		Properties config = new Properties();

		try {
			FileInputStream in = new FileInputStream("database.properties");
			config.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		url = config.getProperty("url");
		user = config.getProperty("user");
		password = config.getProperty("password");
	}

	public PoolConnection() {
		inUse = true;
	}

	public Connection connect() throws SQLException {
		if (con == null || !con.isValid(0)) {
			con = DriverManager.getConnection(url, user, password);
		}

		return con;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse() {
		inUse = true;
	}

	public void close() {
		inUse = false;
	}
}
