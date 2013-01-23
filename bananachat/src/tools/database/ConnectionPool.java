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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Flav
 */
public class ConnectionPool {
	private final static List<PoolConnection> pool;

	static {
		pool = new ArrayList<PoolConnection>();
	}

	public static PoolConnection getConnection() {
		synchronized (pool) {
			for (PoolConnection pcon : pool) {
				if (!pcon.isInUse()) {
					pcon.setInUse();
					return pcon;
				}
			}

			PoolConnection pcon = new PoolConnection();
			pool.add(pcon);
			return pcon;
		}
	}
}
