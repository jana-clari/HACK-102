package UploadDataIntoConfluence;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class UpdateConfluenceRestAPI {
    private static final String BASE_URL = "https://documentaggregator.atlassian.net/wiki";
    private static final String USERNAME = "pankti.kh23@gmail.com";
    private static final String ENCODING = "utf-8";
    private String wikiPageTitle;
    private String wikiPage;
    private static final String wikiSpace ="Confluence";
    private String labelToAdd;

    private static String getContentRestUrl(final Long contentId, final String[] expansions) throws UnsupportedEncodingException {
        final String expand = URLEncoder.encode(StringUtils.join(expansions, ","), ENCODING);
        //return String.format("%s/rest/api/content/%s&os_authType=basic&os_username=%s", BASE_URL, contentId, USERNAME);
        return String.format("%s/rest/api/content/%s?expand=%s&os_authType=basic", BASE_URL, contentId, expand);
    }

    public static void main(final String[] args) throws Exception {
        final long pageId = 590255;
        updateConfluencePage(pageId, "Hello, Pankti!");

    }
    public void setPageAtrributes(String pageTitle, String pageBody){
        this.wikiPage = pageBody;
        this.wikiPageTitle = pageTitle;
        this.labelToAdd = pageTitle;
    }

    public static void updateConfluencePage(long pageId, String pageBody) throws IOException, JSONException {
        @SuppressWarnings("deprecation")
        HttpClient client = new DefaultHttpClient();

        // Get current page version
        String pageObj = null;
        HttpEntity pageEntity = null;
        try {
            HttpGet getPageRequest = new HttpGet(getContentRestUrl(pageId, new String[] {"body.storage", "version"}));
            getPageRequest.addHeader("Authorization","Basic cGFua3RpLmtoMjNAZ21haWwuY29tOmZjTkNDVzlFcThMTmNLNHNwU3M0QzIyQg==");
            getPageRequest.addHeader("Content-Type", "application/json");
            HttpResponse getPageResponse = client.execute(getPageRequest);
            pageEntity = getPageResponse.getEntity();

            pageObj = IOUtils.toString(pageEntity.getContent());

            System.out.println("Get Page Request returned " + getPageResponse.getStatusLine().toString());
            System.out.println(pageObj);
        } catch (HttpResponseException | UnsupportedEncodingException e) {
            System.err.println(e);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pageEntity != null) {
                EntityUtils.consume(pageEntity);
            }
        }

        // Parse response into JSON
        JSONObject page = new JSONObject(pageObj);

        // Update page
        // The updated value must be Confluence Storage Format (https://confluence.atlassian.com/display/DOC/Confluence+Storage+Format), NOT HTML.
        page.getJSONObject("body").getJSONObject("storage").put("value", pageBody);

        int currentVersion = page.getJSONObject("version").getInt("number");
        page.getJSONObject("version").put("number", currentVersion + 1);

        // Send update request
        HttpEntity putPageEntity = null;

        try {
            HttpPut putPageRequest = new HttpPut(getContentRestUrl(pageId, new String[]{}));
            putPageRequest.addHeader("Authorization","Basic cGFua3RpLmtoMjNAZ21haWwuY29tOmZjTkNDVzlFcThMTmNLNHNwU3M0QzIyQg==");
            putPageRequest.addHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(page.toString(), ContentType.APPLICATION_JSON);
            putPageRequest.setEntity(entity);

            HttpResponse putPageResponse = client.execute(putPageRequest);
            putPageEntity = putPageResponse.getEntity();

            System.out.println("Put Page Request returned " + putPageResponse.getStatusLine().toString());
            System.out.println("");
            System.out.println(IOUtils.toString(putPageEntity.getContent()));
        } finally {
            EntityUtils.consume(putPageEntity);
        }
    }
}
