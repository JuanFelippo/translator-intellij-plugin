import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    private static final String TRANSLATE_URL = "http://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=%s&dt=t&q=%s";

    private static final Logger LOG = Logger.getInstance("#Translator");

    private static final Translator TRANSLATOR = new Translator();

    private final LruCache<String, String> mCache = new LruCache<>(200);
    private Future<?> mCurrentTask;

    private Translator() {
    }

    public static Translator get() {
        return TRANSLATOR;
    }

    public void query(String query, Callback callback) {

        if (Utils.isEmptyOrBlankString(query)) {
            if (callback != null) {
                callback.onQuery(query, null);
            }
            return;
        }

        if (mCurrentTask != null) {
            mCurrentTask.cancel(true);
            mCurrentTask = null;
        }

        String cache;
        synchronized (mCache) {
            cache = mCache.get(query);
        }

        if (cache != null) {
            if (callback != null) {
                callback.onQuery(query, cache);
            }
        } else {
            mCurrentTask = ApplicationManager.getApplication().executeOnPooledThread(new QueryRequest(query, callback));
        }
    }

    static String getQueryUrl(String query) {

        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String targetLocale = Settings.getLocale();

        return String.format(TRANSLATE_URL, targetLocale, encodedQuery);
    }

    private final class QueryRequest implements Runnable {

        private final String mQuery;
        private final Callback mCallback;

        QueryRequest(String query, Callback callback) {
            mQuery = query;
            mCallback = callback;
        }

        @Override
        public void run() {
            final String query = mQuery;
            CloseableHttpClient httpclient = HttpClients.createDefault();

            String result;
            try {
                String url = getQueryUrl(query);

                HttpGet httpGet = new HttpGet(url);
                result = httpclient.execute(httpGet, new TranslationResponseHandler());

                if (result != null && !result.isEmpty()) {
                    synchronized (mCache) {
                        mCache.put(Settings.getLocale()+"|"+query, result);
                    }
                }

            } catch (Exception e) {
                LOG.warn("query...", e);
                result = null;
            } finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("query: " + query);
            System.out.println("result: " + result);

            final String translateResult = result;
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onQuery(query, translateResult);
                    }
                }
            });
        }
    }

    private final class TranslationResponseHandler implements ResponseHandler<String> {

        @Override
        public String handleResponse(HttpResponse response) throws IOException {

            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {

                HttpEntity entity = response.getEntity();

                if (entity == null) {
                    return null;
                }

                String json = EntityUtils.toString(entity);
                LOG.info(json);

                json = json.replaceAll(",{2,}", ",");

                Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(json);

                return matcher.find() ? matcher.group(1) : "";
            } else {

                String message = "Unexpected response status: " + status;
                LOG.warn(message);
                throw new ClientProtocolException(message);
            }
        }
    }

    public interface Callback {
        void onQuery(String query, String result);
    }

}
