package UploadDataIntoConfluence;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Creates a Confluence wiki page via the RESTul API
 * using an HTTP Post command.
 * Supplying Basic Auth Setting :
 * https://developer.atlassian.com/cloud/confluence/basic-auth-for-rest-apis/
 */
public class ConfluenceRestAPI {

    //0HvZLM0RbFychJxrxvDAACD2
    //aGFyc2hhbGNoZXBleUBnbWFpbC5jb206MEh2WkxNMFJiRnljaEp4cnh2REFBQ0Qy
    //private static final String BASE_URL = "http://localhost:1990/confluence";
    private static final String BASE_URL = "https://documentaggregator.atlassian.net/wiki";
    //private static final String USERNAME = "harshalchepey@gmail.com";
    //private static final String PASSWORD = "Confluence@clari";
    private static final String ENCODING = "utf-8";
    private String wikiPageTitle;
    private String wikiPage;
    private static final String wikiSpace ="Confluence";
    private String labelToAdd;
    private static final int parentPageId = 9994250;


    private static String createContentRestUrl()throws UnsupportedEncodingException
    {
        return String.format("%s/rest/api/content/", BASE_URL);//?&os_authType=basic&os_username=%s&os_password=%s", BASE_URL, URLEncoder.encode(USERNAME, ENCODING), URLEncoder.encode(PASSWORD, ENCODING));
    }

    public static void main(final String[] args) throws Exception
    {
//        String wikiPageTitle = "Test: My Awesome Page";
//        String wikiPage = "This is a test !@#$$^&:{}[]';/.,'"; //*<>()?
//        String wikiSpace = "Confluence";
//        String labelToAdd = "awesome_stuff";
//        JSONObject newPage = defineConfluencePage(wikiPageTitle,
//                wikiPage,
//                wikiSpace,
//                labelToAdd,
//                parentPageId);
//
//        createConfluencePageViaPost(newPage);

    }

    public void setPageAtrributes(String pageTitle, String pageBody){
        this.wikiPage = pageBody;
        this.wikiPageTitle = pageTitle;
        this.labelToAdd = pageTitle;
    }

    public void publishConfluencePage() {
        JSONObject newPage = null;
        try {
            newPage = defineConfluencePage(wikiPageTitle,
                    wikiPage,
                    wikiSpace,
                    labelToAdd,
                    parentPageId);
            createConfluencePageViaPost(newPage);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createConfluencePageViaPost(JSONObject newPage) throws Exception
    {
        HttpClient client = new DefaultHttpClient();

        // Send update request
        HttpEntity pageEntity = null;

        try
        {
            HttpPost postPageRequest = new HttpPost(createContentRestUrl());
            postPageRequest.addHeader("Authorization","Basic aGFyc2hhbGNoZXBleUBnbWFpbC5jb206MEh2WkxNMFJiRnljaEp4cnh2REFBQ0Qy");
            postPageRequest.addHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(newPage.toString(), ContentType.APPLICATION_JSON);
            postPageRequest.setEntity(entity);

            HttpResponse postPageResponse = client.execute(postPageRequest);
            pageEntity = postPageResponse.getEntity();

            System.out.println("Push Page Request returned " + postPageResponse.getStatusLine().toString());
            System.out.println("");
            System.out.println(IOUtils.toString(pageEntity.getContent()));
        }
        finally
        {
            EntityUtils.consume(pageEntity);
        }
    }

    private static JSONObject defineConfluencePage(String pageTitle,
                                                   String wikiEntryText,
                                                   String pageSpace,
                                                   String label,
                                                   int parentPageId) throws JSONException
    {
        JSONObject newPage = new JSONObject();

        // "type":"page",
        // "title":"My Awesome Page"
        newPage.put("type","page");
        newPage.put("title", pageTitle);

        // "ancestors":[{"id":9994246}],
        JSONObject parentPage = new JSONObject();
        parentPage.put("id",parentPageId);

        JSONArray parentPageArray = new JSONArray();
        parentPageArray.put(parentPage);

        //newPage.put("ancestors", parentPageArray);

        // "space":{"key":"JOUR"},
        JSONObject spaceOb = new JSONObject();
        spaceOb.put("key",pageSpace);
        newPage.put("space", spaceOb);

        JSONObject jsonObjects = new JSONObject();

        jsonObjects.put("value", wikiEntryText);
        jsonObjects.put("representation","storage");

        JSONObject storageObject = new JSONObject();
        storageObject.put("storage", jsonObjects);

        newPage.put("body", storageObject);


        //LABELS
        // "metadata":
        //             {"labels":[
        //                        {"prefix":"global",
        //                        "name":"journal"},
        //                        {"prefix":"global",
        //                        "name":"awesome_stuff"}
        //                       ]
        //             }
//        JSONObject prefixJsonObject1 = new JSONObject();
//        prefixJsonObject1.put("prefix","global");
//        prefixJsonObject1.put("name","journal");
//        JSONObject prefixJsonObject2 = new JSONObject();
//        prefixJsonObject2.put("prefix","global");
//        prefixJsonObject2.put("name",label);
//
//        JSONArray prefixArray = new JSONArray();
//        prefixArray.put(prefixJsonObject1);
//        prefixArray.put(prefixJsonObject2);
//
//        JSONObject labelsObject = new JSONObject();
//        labelsObject.put("labels", prefixArray);
//
//        newPage.put("metadata",labelsObject);

        return newPage;
    }
}
