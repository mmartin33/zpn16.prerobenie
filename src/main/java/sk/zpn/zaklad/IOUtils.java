package sk.zpn.zaklad;

import javax.net.ssl.*;
import java.io.Closeable;
import java.io.IOException;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.X509Certificate;

public class IOUtils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ignored) {
            }
        }
    }

    public static void considerAllCertificatesAsTrusted(URLConnection con)
            throws NoSuchAlgorithmException, KeyManagementException
    {
        if (con instanceof HttpsURLConnection) {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new AllCertsTrusted() }, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            ((HttpsURLConnection) con).setSSLSocketFactory(sc.getSocketFactory());
        }
    }

    public static void considerLocalhostNameAsVerified(URLConnection con) {
        if (con instanceof HttpsURLConnection) {
            ((HttpsURLConnection) con).setHostnameVerifier(new ConsiderLocalhostAsVerified());
        }
    }

    private static class AllCertsTrusted implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }

    private static class ConsiderLocalhostAsVerified implements HostnameVerifier {
        public boolean verify(String hostName, SSLSession sslSession) {
            if ("localhost".equalsIgnoreCase(hostName)) {
                return true;
            }
            HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
            return (verifier != null) && verifier.verify(hostName, sslSession);
        }
    }
}
