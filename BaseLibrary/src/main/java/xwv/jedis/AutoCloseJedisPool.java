package xwv.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;

public class AutoCloseJedisPool extends JedisPool {

    public AutoCloseJedisPool(String host, String password) {
        this(new JedisPoolConfig(), host, password);
    }

    public AutoCloseJedisPool(JedisPoolConfig jedisPoolConfig, String s, String password) {
        this(jedisPoolConfig, s, Protocol.DEFAULT_PORT, password);
    }

    public AutoCloseJedisPool(JedisPoolConfig jedisPoolConfig, String host, int port, String password) {
        this(jedisPoolConfig, host, port, Protocol.DEFAULT_TIMEOUT, password);
    }


    public AutoCloseJedisPool() {
        super();
    }


    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host) {
        super(poolConfig, host);
    }

    public AutoCloseJedisPool(String host, int port) {
        super(host, port);
    }

    public AutoCloseJedisPool(String host) {
        super(host);
    }


    public AutoCloseJedisPool(String host, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(URI uri) {
        super(uri);
    }

    public AutoCloseJedisPool(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(URI uri, int timeout) {
        super(uri, timeout);
    }

    public AutoCloseJedisPool(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password) {
        super(poolConfig, host, port, timeout, password);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, boolean ssl) {
        super(poolConfig, host, port, timeout, password, ssl);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, password, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port) {
        super(poolConfig, host, port);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, boolean ssl) {
        super(poolConfig, host, port, ssl);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout) {
        super(poolConfig, host, port, timeout);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, boolean ssl) {
        super(poolConfig, host, port, timeout, ssl);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database) {
        super(poolConfig, host, port, timeout, password, database);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, boolean ssl) {
        super(poolConfig, host, port, timeout, password, database, ssl);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, password, database, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName) {
        super(poolConfig, host, port, timeout, password, database, clientName);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl) {
        super(poolConfig, host, port, timeout, password, database, clientName, ssl);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, URI uri) {
        super(poolConfig, uri);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, URI uri, int timeout) {
        super(poolConfig, uri, timeout);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, URI uri, int connectionTimeout, int soTimeout) {
        super(poolConfig, uri, connectionTimeout, soTimeout);
    }

    public AutoCloseJedisPool(GenericObjectPoolConfig poolConfig, URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, uri, connectionTimeout, soTimeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public AutoCloseJedisPool(JedisPoolConfig jedisPoolConfig, String s) {
        super(jedisPoolConfig, s);
    }


    public <T> T call(JedisCaller<T> caller) {
        try (Jedis jedis = getResource()) {
            return caller.call(jedis);
        } catch (JedisConnectionException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
}
