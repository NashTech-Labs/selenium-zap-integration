import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utility {
    static final String proxy_apiKey_zap = "your_api_key";
    private static ClientApi api;
    static Logger logger = LogManager.getLogger();

    public static void spiderScan(String webAppUrl, String zapAddress, int zapPort) throws ClientApiException {
        api = new ClientApi(zapAddress, zapPort, proxy_apiKey_zap);
        ApiResponse apiResponse = api.spider.scan(webAppUrl, null, null, null, null);
        logger.info("apiResponse : " + apiResponse.getName());
    }

    public static void getReports(String webAppUrl) throws ClientApiException, IOException {
        byte[] bytes = api.core.htmlreport();

        // getting the alert messages and just printing those.
        ApiResponse messages =  api.core.messages(webAppUrl,"0","99999999");
        logger.info("messages : " + messages);

        // storing the bytes in to html report.
        String str = new String(bytes, StandardCharsets.UTF_8);
        File newTextFile = new File("security_report.html");
        FileWriter fw = new FileWriter(newTextFile);
        fw.write(str);
        fw.close();
    }
}
