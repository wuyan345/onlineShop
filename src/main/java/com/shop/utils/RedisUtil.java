package com.shop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

public class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	
	private static final String IP = "172.31.189.8";
	private static final int PORT = 6379;
	private static final String PASSWORD = "v1RjVmtAyiXvPu6xXodUozXG63wKUzyp";
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), IP, PORT, 1000, PASSWORD);
	
	
	private static Jedis getJedisInstance() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			/// ... do stuff here ... for example
			return jedis;
		} catch (JedisException e) {
			logger.error("连接redis失败", e);
			return null;
		}
	}
	
	public static void destroyPool(){
		pool.destroy();
	}
	
	public static String setKey(String key, String value) {
		Jedis jedis = getJedisInstance();
		if (jedis == null)
			return null;
		String val = jedis.set(key, value);
		jedis.close();
		return val;
	}

	public static String getKey(String key) {
		Jedis jedis = getJedisInstance();
		if (jedis == null)
			return null;
		String val = jedis.get(key);
		jedis.close();
		return val;
	}
	
	public static Long delKey(String key) {
		Jedis jedis = getJedisInstance();
		if (jedis == null)
			return null;
		long val = jedis.del(key);
		jedis.close();
		return val;
	}
	
//	public static void main(String[] args) {
//		System.out.println(RedisUtil.getKey("foo"));
//	}
}
