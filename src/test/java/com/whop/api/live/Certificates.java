package com.whop.api.live;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/** A self-signed certificate made at test time with the JDK's keytool; no key is ever committed. */
final class Certificates {
    private static final char[] PASSWORD = "live-test".toCharArray();

    private final KeyStore keys;

    private Certificates(KeyStore keys) {
        this.keys = keys;
    }

    /** A new certificate for {@code subjectAlternativeName}, such as {@code ip:127.0.0.1} or {@code dns:other.test}. */
    static Certificates selfSigned(String subjectAlternativeName) throws IOException, GeneralSecurityException {
        Path dir = Files.createTempDirectory("live-certificates");
        Path store = dir.resolve("server.p12");
        try {
            Process keytool = new ProcessBuilder(
                            Paths.get(System.getProperty("java.home"), "bin", "keytool")
                                    .toString(),
                            "-genkeypair",
                            "-alias",
                            "server",
                            "-keyalg",
                            "RSA",
                            "-keysize",
                            "2048",
                            "-validity",
                            "1",
                            "-dname",
                            "CN=live-test",
                            "-ext",
                            "SAN=" + subjectAlternativeName,
                            "-storetype",
                            "PKCS12",
                            "-keystore",
                            store.toString(),
                            "-storepass",
                            new String(PASSWORD),
                            "-keypass",
                            new String(PASSWORD))
                    .redirectErrorStream(true)
                    .start();
            if (keytool.waitFor() != 0) {
                throw new IOException("keytool could not make a certificate");
            }
            KeyStore keys = KeyStore.getInstance("PKCS12");
            try (InputStream input = Files.newInputStream(store)) {
                keys.load(input, PASSWORD);
            }
            return new Certificates(keys);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IOException(interrupted);
        } finally {
            Files.deleteIfExists(store);
            Files.deleteIfExists(dir);
        }
    }

    /** What a server presenting the certificate uses. */
    SSLContext server() throws GeneralSecurityException {
        KeyManagerFactory keyManagers = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagers.init(keys, PASSWORD);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagers.getKeyManagers(), null, null);
        return context;
    }

    /** A trust manager that trusts the certificate alone. */
    X509TrustManager trust() throws GeneralSecurityException, IOException {
        Certificate certificate = keys.getCertificate("server");
        KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
        trusted.load(null, null);
        trusted.setCertificateEntry("server", certificate);
        TrustManagerFactory trustManagers = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagers.init(trusted);
        return (X509TrustManager) trustManagers.getTrustManagers()[0];
    }
}
