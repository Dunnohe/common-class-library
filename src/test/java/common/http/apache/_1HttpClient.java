package common.http.apache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class _1HttpClient {

    private final static ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public Integer testGet() throws IOException {
        //创建连接池
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(123);
        connManager.setDefaultMaxPerRoute(123);

        //连接池配置
        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setConnectTimeout(300)
                        .setConnectionRequestTimeout(200).build();

        //创建http连接客户端
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        //response handler
        BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpGet httpget = null;
        CloseableHttpResponse response = null;
        try {
            httpget = new HttpGet("http://localhost/");
            response = httpClient.execute(httpget);

            //判断http code
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseStr = basicResponseHandler.handleResponse(response);
                return objectMapper.readValue(responseStr, Integer.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放资源
            if(httpget != null) {
                httpget.releaseConnection();
            }
            if(response != null) {
                response.close();
            }
        }
        return -1;
    }

    public void testHttps() throws Exception {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();

        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpGet httpGet = new HttpGet("http://localhost/");
        httpGet.setHeader("Accept", "application/xml");

        HttpResponse response = client.execute(httpGet);

    }
}
