package cl.waypoint.ms.mailer.timer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import cl.waypoint.WrapperCommons;
import cl.waypoint.jedis.JedisPoolInstance;
import cl.waypoint.jedis.JedisPoolManager;
import cl.waypoint.ms.mailer.dto.BounceSummary;

public class TimerBounce extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger("TimerBounce");
	private static final Map<String, BounceSummary> summary = new HashMap<>();
	private static final JedisPoolInstance cache = new JedisPoolInstance(JedisPoolManager.CACHEPOOLDNS);

	@Override
	public void run() {
		synchronized (summary) {
			summary.clear();
			Map<String, BounceSummary> data = getHashFromRedis();
			if (data != null) {
				summary.putAll(data);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, BounceSummary> getHashFromRedis() {
		Map<String, BounceSummary> result = null;
		try {
			byte[] data = WrapperCommons.getCache(cache.getPool(), "HashBounce");
			if (data != null) {
				ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
				ObjectInputStream in = new ObjectInputStream(byteIn);
				result = (Map<String, BounceSummary>) in.readObject();
			}
			return result;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return new HashMap<>();
		}
	}

	public static boolean isBlacklisted(String email) {
		return summary.containsKey(email);
	}

}
