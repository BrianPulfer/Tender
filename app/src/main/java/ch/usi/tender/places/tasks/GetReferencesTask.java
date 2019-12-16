package ch.usi.tender.places.tasks;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ch.usi.tender.places.PlacesAPI;

public class GetReferencesTask extends AsyncTask<Location, Void, ArrayList<String>[]> {

    /**
     * Class responsable for retrieving references to photos given a location.
     * The photo references should point to photos that are close to the given locaiton.
     */


    @Override
    protected ArrayList<String>[] doInBackground(Location... locations) {
        try {
            return getPhotosReferences(locations[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String>[] getPhotosReferences(Location location) throws IOException {
        /**
         * Given the location, returns 3 ArrayLists of Strings:
         *      The references to the photos
         *      The names of the restaurants
         *      The latitude and longitude under form of string
         *
         * The returned arrays are sorted such that the first element of each array refers to the
         * first restaurant and so on.
         */

        if(location == null) {
            return null;
        }

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        // TODO: Consider changing 'types' to 'food'
        String requestURL =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=" + lat + "," + lon +
                        "&radius=500" +
                        "&types=restaurant" +
                        "&key=" + PlacesAPI.API_KEY;


        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;

        while((line = br.readLine()) != null){
            sb.append(line);
        }

        //TODO: REMOVE
        //String content = getDebugContent();
        String content = sb.toString();
        String[] refs = content.split("\"photo_reference\" : \"");
        String[] nms = content.split("\"name\" : \"");
        String[] lats = content.split("\"lat\" : ");
        String[] lons = content.split("\"lng\" : ");

        // TODO: REMOVE
        Log.d("Brian", "Photo references: "+ (refs.length-1) + ". Names:: "+(nms.length-1));

        ArrayList<String> references = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> latLons = new ArrayList<>();

        for(int i = 1; i<refs.length-1; i++){
            String latitude = lats[3*(i-1)+1].split(",")[0];
            String longitude = lons[3*(i-1)+1].split(" ")[0];

            String reference = refs[i].split("\"")[0];
            String name = nms[i].split("\"")[0];
            String latLon = latitude+" "+longitude;

            references.add(reference);
            names.add(name);
            latLons.add(latLon);
        }

        //TODO: REMOVE
        Log.d("Brian", content);

        return new ArrayList[]{references, names, latLons};
    }


    //TODO: REMOVE
    private String getDebugContent(){
        /**
         * Debug method to simulate behaviour
         */
        return "{\n" +
                "   \"html_attributions\" : [],\n" +
                "   \"next_page_token\" : \"CqQCEgEAAPxzFRnZnRI98BMu1JOGDC83oKEl5TPTT4Qjl15D5iZ2zeFVT74ZvFGfHOZS9gDcYFbH2_5k3W2FYC95jQG9GNt1a00SBNboIjQQ-0s3_KZHJxfkEcW4qAGoVlHjAeWgybu1b-miGgpAob-GlelXzGT9l9dAlyAt1JEQRZmMeVDlVsvapjmebB13cXfyiRVy92Xpkh9gjYr3alDZEgp5SJASvV284G6SzdEPLFotDtX8paQq0zB8zM52GPM9W7z7qBNMDYyz8T-5qSzbivUljk3y998pc6tPU9wPXu3ZHEyGRApfTS-21OECTbANdEjVtHpAAjNQ4D9MqMKzUhb0wPhXSLnwGH1O0JcBH3kYBMdM5Sl9JGulszoJqo4CsdJuJBIQH1FVsSriSREpr0QYUYPmYxoUbdpoePrx_zlwoulOqvqr-4waF3w\",\n" +
                "   \"results\" : [\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0036778,\n" +
                "               \"lng\" : 8.951051999999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.1191401,\n" +
                "                  \"lng\" : 9.089630099999999\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 45.93971,\n" +
                "                  \"lng\" : 8.893699999999999\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png\",\n" +
                "         \"id\" : \"c0da2b92ec9266875f3abb0d9e7e98b9499c50ce\",\n" +
                "         \"name\" : \"Lugano\",\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1440,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/110944583287051285957\\\"\\u003efelisa pinilla\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAA8DMtawervsBh5r25C39sIAn_cOuyXMctNDSKims3x-0IN9_6iinVJw5S2RjVldXmrAJviHG0Lm1mBSpcM7buGcQl_PIouRqNzWHU-o3QRPFpABrogn4Kr_tqY-6E7jJmEhCEZ-yHxNfw1_C-R4vJfp1JGhQm4m9Ald8uhs0Y_l8XBtrla5_KZA\",\n" +
                "               \"width\" : 2560\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJ8RFCavcthEcR4PihLiEEjO8\",\n" +
                "         \"reference\" : \"ChIJ8RFCavcthEcR4PihLiEEjO8\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"locality\", \"political\" ],\n" +
                "         \"vicinity\" : \"Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0009496,\n" +
                "               \"lng\" : 8.949284199999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0022724302915,\n" +
                "                  \"lng\" : 8.950860530291502\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 45.9995744697085,\n" +
                "                  \"lng\" : 8.948162569708497\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"a9f4bcfe6d1dec6a9757a617f244d5e8412ee6da\",\n" +
                "         \"name\" : \"Nassa\",\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 3264,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/116254611951775637214\\\"\\u003ePaula Elliott\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAX3csONcz_a9miecwXdLTXt3fo_zBxB4uITW9ipISoFVFYBAx7UZvFOn1__ouXR16XHP2rSAnVhievM3MxtaPDX_28wBQfs6F3q-aaegPwBGK4bfsfv4wVhTDG3XN5i50EhAdG4o8oxvw3y1LRvBsHcBQGhSIDQjKRbJMFJ0oc2JcV32uYWX_EA\",\n" +
                "               \"width\" : 1836\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJuXa5uJEthEcRHev0sOZ8bqQ\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W2X+9P Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W2X+9P\"\n" +
                "         },\n" +
                "         \"rating\" : 4.2,\n" +
                "         \"reference\" : \"ChIJuXa5uJEthEcRHev0sOZ8bqQ\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 124,\n" +
                "         \"vicinity\" : \"Via Nassa 60, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00279,\n" +
                "               \"lng\" : 8.95041\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0042160302915,\n" +
                "                  \"lng\" : 8.951783230291504\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0015180697085,\n" +
                "                  \"lng\" : 8.949085269708499\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"1faed7fbc458f7fd1ee94f19f48d730bd3502f22\",\n" +
                "         \"name\" : \"Hotel Walter au Lac\",\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 720,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/100517264686426033100\\\"\\u003eHotel Walter Au Lac\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAqj0W6nEf2_JFRpW6GTKAYLgMR6xdEuM2pwzU8UNG7qiU9BFrjmmIX0mesM5nFOlQwHL4mTUKm42cpJTRVRnQZwqL8BtlAbae9LOM-9gm3qfcTESBuZxHMwyJMcvhN4NTEhA1km7K-pxCNHyDu9RRCgn4GhRILrOtng2l3RJhLYNawFz5Z1147A\",\n" +
                "               \"width\" : 960\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJiwDcGY4thEcRgnyrr9tOw1E\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2X32+45 Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2X32+45\"\n" +
                "         },\n" +
                "         \"rating\" : 4.3,\n" +
                "         \"reference\" : \"ChIJiwDcGY4thEcRgnyrr9tOw1E\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 205,\n" +
                "         \"vicinity\" : \"Piazza Riziero Rezzonico 7, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0026832,\n" +
                "               \"lng\" : 8.946722099999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0039638802915,\n" +
                "                  \"lng\" : 8.948149480291502\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0012659197085,\n" +
                "                  \"lng\" : 8.945451519708497\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"478ed74c59d2762dcdf879b26d3f815d78816217\",\n" +
                "         \"name\" : \"Continental Parkhotel Lugano\",\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 2992,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/116252385487382415295\\\"\\u003eTyler Durden\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAA-MjhD25flvj5cb25QflR8RJAl4tFCKi2276EbsoqmHZvvJikB5uG39Gljh1yVX1mtOcpBr_QnQ2O2LeUUwxU4Y5_r7a91lolBIenAJVdPIu_dad1IAyP5g2a91Hx0n2KEhDxQzP4AXxX2QIxbRJYvTgtGhRRttfKpxhszv6ZA0Y8pMnHiSFeFQ\",\n" +
                "               \"width\" : 4000\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJJ6u9448thEcRANkNXfDh9GQ\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W3W+3M Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W3W+3M\"\n" +
                "         },\n" +
                "         \"rating\" : 4.3,\n" +
                "         \"reference\" : \"ChIJJ6u9448thEcRANkNXfDh9GQ\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 410,\n" +
                "         \"vicinity\" : \"Via Basilea 28, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0026881,\n" +
                "               \"lng\" : 8.9496234\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0039284802915,\n" +
                "                  \"lng\" : 8.951363480291501\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0012305197085,\n" +
                "                  \"lng\" : 8.948665519708499\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"7cf9db220b2fd018f11bd8553d622ad3fa1aaf8e\",\n" +
                "         \"name\" : \"San Carlo\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 3968,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/112444078659429510449\\\"\\u003eRodolfo Varela\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAy3K_mSmNK_akUHHQFTqpU1rTD3yLJGKIlq2y0xc2CZ-oapF6o5vsNo8RddYFwmLQ4rgulh75wE5UNa5iPTky6omCPrbt5-ShAEV9hsxD2d11I7HrOGWmKiF_x9e8aojXEhD9DT4lcELV6NXPM3tc-iPxGhQG9dZYD4lcivdEiBsCGxGj3T89cQ\",\n" +
                "               \"width\" : 2976\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJqwcZJI4thEcRtzvMOIYyUzE\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W3X+3R Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W3X+3R\"\n" +
                "         },\n" +
                "         \"rating\" : 3.9,\n" +
                "         \"reference\" : \"ChIJqwcZJI4thEcRtzvMOIYyUzE\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 146,\n" +
                "         \"vicinity\" : \"Via Nassa 28, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00027679999999,\n" +
                "               \"lng\" : 8.949040499999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.00159908029149,\n" +
                "                  \"lng\" : 8.950509530291502\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 45.9989011197085,\n" +
                "                  \"lng\" : 8.947811569708497\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"67e98d5f094ca9409e8d132056890fdbe9afb118\",\n" +
                "         \"name\" : \"International au Lac Historic Lakeside Hotel\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 2160,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/100842160650143377770\\\"\\u003eInternational au Lac Historic Lakeside Hotel\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAPIt8VZmbP0dif3Syt6OSXdigDenh-mIjHCuoJ9RJoroM1t5NIt9n_ftLLYTiF25u-XaIMm5AuTYKWPCmlzHFPs5lkHzX05aYlr3VxpODTY9WCs6CiG5PI0cxrMyBRL0fEhDWxyohzBSXRtNMa1YmO40eGhQ-DLaFiNYloLIksvJH_pcUjB7RGg\",\n" +
                "               \"width\" : 3840\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJf98xopEthEcRHbXteG0pm_k\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W2X+4J Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W2X+4J\"\n" +
                "         },\n" +
                "         \"rating\" : 4.3,\n" +
                "         \"reference\" : \"ChIJf98xopEthEcRHbXteG0pm_k\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 271,\n" +
                "         \"vicinity\" : \"Via Nassa 68, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0034266,\n" +
                "               \"lng\" : 8.9463001\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0049438802915,\n" +
                "                  \"lng\" : 8.947507030291503\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0022459197085,\n" +
                "                  \"lng\" : 8.944809069708498\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"925042e6c39ff940185dcdab137e9e26e8318046\",\n" +
                "         \"name\" : \"Albergo Montarina\",\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1152,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/105655028944360399161\\\"\\u003eFlo B. aus B.\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAsmPo2UddBKmw9AJQX7Oq38a4zsWZZL7dbd_FTXsm9WLRb28HpX2LzFxPsMYUSvznC0D8n3Nsekfv593zEDHQTdRd7nMsPUNJsTV7gBUYZOAZuWTPfi1667KRKnRihcufEhAUW5DzqAmBZZDbVWaIzkVdGhSGaHVvTeQAwRfzadYPAE2UIFnkWg\",\n" +
                "               \"width\" : 2048\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJqfwC9I8thEcRr8SIHFlBy-M\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W3W+9G Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W3W+9G\"\n" +
                "         },\n" +
                "         \"rating\" : 3.9,\n" +
                "         \"reference\" : \"ChIJqfwC9I8thEcRr8SIHFlBy-M\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 291,\n" +
                "         \"vicinity\" : \"Via Montarina 1, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0044314,\n" +
                "               \"lng\" : 8.9493262\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.00545148029149,\n" +
                "                  \"lng\" : 8.950948680291503\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0027535197085,\n" +
                "                  \"lng\" : 8.948250719708499\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"c026c8c6e83910c5f771d7bc59d86c27eec29ef4\",\n" +
                "         \"name\" : \"Hotel Albergo Acquarello\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 2268,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/105311397982053751099\\\"\\u003eNikola Stepanovic\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAA4T5gw91yj7L1NVrfhiFXgvXneFPJlbaBXS3ly5GgI3HMygOJsPsu81l6qZMqwvoBiXcOvddgdAiI_18eEjf9iWdBKEVS3EWtI5UFYcIeNfXO03W_4FWujXsJSNh7LcfcEhD7XrIbqp_V5zMgAF8Mj07pGhT3BwBQQlg6_j7v3lK1FxkTeUwUeA\",\n" +
                "               \"width\" : 4032\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJGc8wq48thEcRbPkwurg1CU4\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W3X+QP Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W3X+QP\"\n" +
                "         },\n" +
                "         \"rating\" : 3.9,\n" +
                "         \"reference\" : \"ChIJGc8wq48thEcRbPkwurg1CU4\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 246,\n" +
                "         \"vicinity\" : \"Piazza Cioccaro 9, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0015854,\n" +
                "               \"lng\" : 8.949412299999997\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0029559302915,\n" +
                "                  \"lng\" : 8.9510313802915\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0002579697085,\n" +
                "                  \"lng\" : 8.948333419708495\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"def3fad2c1b2405ca69c4d448267afcd18fbe32b\",\n" +
                "         \"name\" : \"Splendori Suite\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 4032,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/104177375907970798010\\\"\\u003eGábor Kovács\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAATFis2MKp6X9MQKHC7ciuBREYWEdP3KbBDBsWDbD0s9g_jssPqBN8o8IrwMU2qZQtAx_s48G4SbcGFr-uqh_gliry3xBzoyWEtRUtfg-ghLbrnqFxiNxTqAuYr5ySpGS6EhC3a4Gsg9sbJriVM8Wo2F3gGhThGsCG_ItpCXhlDS3bORuwTG97Xw\",\n" +
                "               \"width\" : 3024\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJpTaZxJEthEcResAurny8gHs\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W2X+JQ Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W2X+JQ\"\n" +
                "         },\n" +
                "         \"rating\" : 4.1,\n" +
                "         \"reference\" : \"ChIJpTaZxJEthEcResAurny8gHs\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 19,\n" +
                "         \"vicinity\" : \"Via Nassa 52, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 45.99835030000001,\n" +
                "               \"lng\" : 8.9443582\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 45.9997929802915,\n" +
                "                  \"lng\" : 8.945611580291501\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 45.9970950197085,\n" +
                "                  \"lng\" : 8.942913619708499\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"9cb6d4772c90910a39db157bfd30e5f55577f55c\",\n" +
                "         \"name\" : \"Hotel Colorado Lugano\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 4256,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/110806773971596893075\\\"\\u003eHotel Colorado Lugano\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAA0gmZgoIylEaeQyRkB6noqNNES-SNVROp3vus95l8O4m2kR9ss7YdYjCJmGQvVPIx5FfQ1oSAev8dpcBV2X1VVzpboK1aXuXZ2ZwOLrapCbBrwtm13uvy-rZ7aShdP_mxEhAM3C4hmTvUOjMREFdrKmL8GhRR0DiHde_EGNKnI4QPDcRs-ipBUg\",\n" +
                "               \"width\" : 2832\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJE-pEBpothEcRrQt1wqez1bQ\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"XWXV+8P Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FQCXWXV+8P\"\n" +
                "         },\n" +
                "         \"rating\" : 4.1,\n" +
                "         \"reference\" : \"ChIJE-pEBpothEcRrQt1wqez1bQ\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"restaurant\", \"food\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 133,\n" +
                "         \"vicinity\" : \"Via Clemente Maraini 19, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00435960000001,\n" +
                "               \"lng\" : 8.9497111\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0054155802915,\n" +
                "                  \"lng\" : 8.951070380291503\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.00271761970851,\n" +
                "                  \"lng\" : 8.948372419708498\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"9b24513e0dd9c1f61832110ccffe5d135c3334ab\",\n" +
                "         \"name\" : \"Hotel Lugano Dante Center\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1367,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/117775536480330530723\\\"\\u003eHotel Lugano Dante Center\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAt5p-hgzvaZzA39Kl4rZP4mIro0OLUdfAm_Nw5QiAffcfccZD0F4G0j53_yFZlk1W3H51k0qOX_kk7ZghG2M15QEVoCocs-W60fS5XN9lDHKoQHNyBuGIg2_aWvuQYsk2EhAZrrhDHM-GKgbepP8pLHP0GhQ-1jHx55epXFaYkDTQ_d2PEBVkRg\",\n" +
                "               \"width\" : 2048\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJQZjNVo4thEcRG_saPzSnmo8\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W3X+PV Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W3X+PV\"\n" +
                "         },\n" +
                "         \"rating\" : 4.7,\n" +
                "         \"reference\" : \"ChIJQZjNVo4thEcRG_saPzSnmo8\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 433,\n" +
                "         \"vicinity\" : \"Piazza Cioccaro 5, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00433470000001,\n" +
                "               \"lng\" : 8.950100199999998\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0054050302915,\n" +
                "                  \"lng\" : 8.951238330291503\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0027070697085,\n" +
                "                  \"lng\" : 8.948540369708498\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/lodging-71.png\",\n" +
                "         \"id\" : \"f081e77d10e0a81000a6f400079d47cc24415589\",\n" +
                "         \"name\" : \"GABBANI HOTEL\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1593,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/113425338030890430032\\\"\\u003eGABBANI HOTEL\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAYzMChIPWbV7s5_Jl15eBJZBvNoZOi-2DnUQU6U51oGuJoW9ondbbw6GnxUSWF5cG6WllZCdID8BpA9cFLJLdTtpWrkSoFakhK4_0GwOZja1rIX6nTCFKiD6VyjrO1RLoEhBPQF9wbAlmY-Jwq7tnVFHfGhTuxwdcaL85JbRu7quUkFKSrTJCPg\",\n" +
                "               \"width\" : 1194\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJeR5LXI4thEcRb3dHAkNWcZg\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2X32+P2 Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2X32+P2\"\n" +
                "         },\n" +
                "         \"rating\" : 4.4,\n" +
                "         \"reference\" : \"ChIJeR5LXI4thEcRb3dHAkNWcZg\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"lodging\", \"restaurant\", \"food\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 125,\n" +
                "         \"vicinity\" : \"Piazza Cioccaro 1, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00282929999999,\n" +
                "               \"lng\" : 8.949842\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0042443802915,\n" +
                "                  \"lng\" : 8.9514766302915\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0015464197085,\n" +
                "                  \"lng\" : 8.948778669708496\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\",\n" +
                "         \"id\" : \"8296e3fc0916fad07ec82c3d4efa1dbfa81566fc\",\n" +
                "         \"name\" : \"Coop City Lugano\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 3024,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/116459879008617019538\\\"\\u003eMichele Zinnai\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAYIhP3u5gzzsnjrapqfMUZpeB1YMRx2RzUB8NSg9JL3sj47TDw9QAs8uYQgVHQSeMq2Qj6wG8hnVAAIEgEToy_iPDJyKoBXjO6_KpUdk5gNleeWMVppPdIWqDIc1yCFIeEhDNxmWVlYIRQ33LLUBNMcNsGhRXtrE3LIITJBLWbniwq9CmOJMsBQ\",\n" +
                "               \"width\" : 4032\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJYymqPI4thEcRfKdQXj2BfGw\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W3X+4W Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W3X+4W\"\n" +
                "         },\n" +
                "         \"rating\" : 4.1,\n" +
                "         \"reference\" : \"ChIJYymqPI4thEcRfKdQXj2BfGw\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"department_store\", \"point_of_interest\", \"store\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 678,\n" +
                "         \"vicinity\" : \"Via Nassa 22, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00223829999999,\n" +
                "               \"lng\" : 8.949569500000001\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0035741302915,\n" +
                "                  \"lng\" : 8.951254180291503\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0008761697085,\n" +
                "                  \"lng\" : 8.948556219708498\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\",\n" +
                "         \"id\" : \"19213f841425e6c73c910685640f921095368459\",\n" +
                "         \"name\" : \"Lacoste\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 2988,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/105344958636468985749\\\"\\u003eMOHAMMAD KHALIFA\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAA-t8FXviYAvnH5vKi5ECK46go_2LVBHeL1j_MQLyuJ_39euF-UhhR610k5ZFxZG1nHpjqdkZEMFuYrH09mVD8MhlbYOpYPk0wQAvtt3OAC_PFGatnOsLW-hMmZoisPysbEhBiUi2OchnGIW0oez6TwX0lGhRK4wTG47ravuRjG4pw8quDly8KUQ\",\n" +
                "               \"width\" : 5312\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJKT1jJ44thEcRS7SK_T_I9bs\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W2X+VR Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W2X+VR\"\n" +
                "         },\n" +
                "         \"rating\" : 4.8,\n" +
                "         \"reference\" : \"ChIJKT1jJ44thEcRS7SK_T_I9bs\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [\n" +
                "            \"clothing_store\",\n" +
                "            \"shoe_store\",\n" +
                "            \"point_of_interest\",\n" +
                "            \"store\",\n" +
                "            \"establishment\"\n" +
                "         ],\n" +
                "         \"user_ratings_total\" : 4,\n" +
                "         \"vicinity\" : \"Via Nassa 36, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0037636,\n" +
                "               \"lng\" : 8.950742399999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0048484802915,\n" +
                "                  \"lng\" : 8.9522214302915\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0021505197085,\n" +
                "                  \"lng\" : 8.949523469708495\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/bank_dollar-71.png\",\n" +
                "         \"id\" : \"07c48fe6595fbb409aa8ae6f685d52e5f1181444\",\n" +
                "         \"name\" : \"BancaStato\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 510,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/105545981176255601753\\\"\\u003eBancaStato\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAA-XIU1_1nRhTGwHw1-v1TBUHhE2ELYrfyjDRH7opNurLC2ODSVhldkNa7sjrzRYEPauwbubf2hVaEnthUiFq7l4xqNPlhHnYz2x93tdUTq2JUErq5okmTMD1eO4XUzFWsEhAmScQgugmSv6mqQSO4rHM9GhR98oPBMUD4X0Z1n0ls0254Y4_MAg\",\n" +
                "               \"width\" : 667\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJ61qaDY4thEcRyjCWAAVEd_8\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2X32+G7 Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2X32+G7\"\n" +
                "         },\n" +
                "         \"rating\" : 2,\n" +
                "         \"reference\" : \"ChIJ61qaDY4thEcRyjCWAAVEd_8\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"bank\", \"finance\", \"point_of_interest\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 3,\n" +
                "         \"vicinity\" : \"Piazza Riforma, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00356590000001,\n" +
                "               \"lng\" : 8.951261200000001\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0048165302915,\n" +
                "                  \"lng\" : 8.952476080291504\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0021185697085,\n" +
                "                  \"lng\" : 8.949778119708499\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/civic_building-71.png\",\n" +
                "         \"id\" : \"a0d5902f755e2beb1aa623e4975732c052d9f7f3\",\n" +
                "         \"name\" : \"Municipio di Lugano\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1000,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/103722650369146983344\\\"\\u003eCloudia Chen\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAwGk8raEphjdtMetXD4sGizgPweDSEU9-WhVtfMrABVqfe84T9E-XPfrgppGa981YgUDau4hcFpC1uHKzhUEPx9BJYE06JQAjONOUyCqmttUjaQoPWNKCPFNL87WhvjEpEhBUsxU_x_VybSxWppJJDoJTGhSkPbHjBSEu_ae22NxoH8MiAwYdww\",\n" +
                "               \"width\" : 1500\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJoWcFco4thEcR7Zi9EEaUBUs\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2X32+CG Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2X32+CG\"\n" +
                "         },\n" +
                "         \"rating\" : 4.3,\n" +
                "         \"reference\" : \"ChIJoWcFco4thEcR7Zi9EEaUBUs\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [\n" +
                "            \"city_hall\",\n" +
                "            \"local_government_office\",\n" +
                "            \"point_of_interest\",\n" +
                "            \"establishment\"\n" +
                "         ],\n" +
                "         \"user_ratings_total\" : 44,\n" +
                "         \"vicinity\" : \"Piazza Riforma 1, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00033790000001,\n" +
                "               \"lng\" : 8.949474700000001\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.00163483029149,\n" +
                "                  \"lng\" : 8.950726030291502\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 45.99893686970849,\n" +
                "                  \"lng\" : 8.948028069708498\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\",\n" +
                "         \"id\" : \"b61a8a2e8a9ab679054738fb5062a7a4c194c8cd\",\n" +
                "         \"name\" : \"Louis Vuitton\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 4032,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/110386484042153744009\\\"\\u003eVincenzo Caci\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAUASmU3fa4B5mz_Dr-CJ3AXXKC5LBIiGj4RVOB1sssnmDuDyhVCpXdq-HI1lRxX-mN0YDyNYU-Fq1cKhL8w-tl9Bs47TZqJoOITKvEOvWOOnzcc633zJhzDMFwtUmDEUuEhBWYBa9kth0wxBdnY5Hez6dGhSu7veWhZNuhcFOJGKHvDvlmSYYrw\",\n" +
                "               \"width\" : 3024\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJhdT0mJEthEcRFM_bqzcwEmg\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W2X+4Q Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W2X+4Q\"\n" +
                "         },\n" +
                "         \"price_level\" : 4,\n" +
                "         \"rating\" : 4.5,\n" +
                "         \"reference\" : \"ChIJhdT0mJEthEcRFM_bqzcwEmg\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [\n" +
                "            \"shoe_store\",\n" +
                "            \"point_of_interest\",\n" +
                "            \"clothing_store\",\n" +
                "            \"store\",\n" +
                "            \"establishment\"\n" +
                "         ],\n" +
                "         \"user_ratings_total\" : 136,\n" +
                "         \"vicinity\" : \"Via Nassa 31, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.004428,\n" +
                "               \"lng\" : 8.950313599999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0054846802915,\n" +
                "                  \"lng\" : 8.951337130291501\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0027867197085,\n" +
                "                  \"lng\" : 8.948639169708496\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\",\n" +
                "         \"id\" : \"6806751e54169050390e9b7ff7d848ccfa5e0a62\",\n" +
                "         \"name\" : \"Swatch Lugano\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1872,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/104938664531155980658\\\"\\u003eFranck\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAk6ECDYAW5f7JUf1-Ec3OJjH1o9frTfgISCxd4P9UgxZtLGpN4kck-hVN9V0gIt5qItmubEdjWJd8XGZ2jTa9RUG-FJwaVrFrl_CKSDz3LBa0J9ZcOZWz25G7F9FDv_nFEhC81kXBpgmq-Jv1Gho300uXGhTN4hzMkYM0FXTObZhi9tRRnZnylQ\",\n" +
                "               \"width\" : 3328\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJqX45WY4thEcRlj8kz_tmX1s\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2X32+Q4 Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2X32+Q4\"\n" +
                "         },\n" +
                "         \"rating\" : 4.6,\n" +
                "         \"reference\" : \"ChIJqX45WY4thEcRlj8kz_tmX1s\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"jewelry_store\", \"point_of_interest\", \"store\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 25,\n" +
                "         \"vicinity\" : \"Via Francesco Soave 9, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.0031393,\n" +
                "               \"lng\" : 8.950281599999999\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.0044423302915,\n" +
                "                  \"lng\" : 8.951659430291501\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.0017443697085,\n" +
                "                  \"lng\" : 8.948961469708497\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\",\n" +
                "         \"id\" : \"c3ffb48f5c82e88a83c290d6638f9367977e2b92\",\n" +
                "         \"name\" : \"Les Ambassadeurs AG\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 1200,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/114507861401557877389\\\"\\u003eLes Ambassadeurs AG\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAsPXqHKTnu65CZrg8gC32DnKjHQx3hjatWfw8hrHUHTbtInBocYQbZ68XMP7wck_EnVHMCivc7XKep0KTbCoyTj40-xsPUze-OwJHUGVr_N9g1dN6cph02aeqYiqeDEyqEhAHz3ehC22HS3lE27nO_-6qGhR9afMgQ6e8bZDO09MKZ_olmv1GzA\",\n" +
                "               \"width\" : 1920\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJtQLhPI4thEcRWejgEfsPUYg\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2X32+74 Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2X32+74\"\n" +
                "         },\n" +
                "         \"rating\" : 4.8,\n" +
                "         \"reference\" : \"ChIJtQLhPI4thEcRWejgEfsPUYg\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"jewelry_store\", \"point_of_interest\", \"store\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 13,\n" +
                "         \"vicinity\" : \"Via Nassa 5, Lugano\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"geometry\" : {\n" +
                "            \"location\" : {\n" +
                "               \"lat\" : 46.00154200000001,\n" +
                "               \"lng\" : 8.9496532\n" +
                "            },\n" +
                "            \"viewport\" : {\n" +
                "               \"northeast\" : {\n" +
                "                  \"lat\" : 46.00275638029149,\n" +
                "                  \"lng\" : 8.950996380291501\n" +
                "               },\n" +
                "               \"southwest\" : {\n" +
                "                  \"lat\" : 46.00005841970849,\n" +
                "                  \"lng\" : 8.948298419708497\n" +
                "               }\n" +
                "            }\n" +
                "         },\n" +
                "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\",\n" +
                "         \"id\" : \"0e7a9c0207c2ecb2475110e8c80365b07f482d57\",\n" +
                "         \"name\" : \"BELOTTI OtticaUdito\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false\n" +
                "         },\n" +
                "         \"photos\" : [\n" +
                "            {\n" +
                "               \"height\" : 4016,\n" +
                "               \"html_attributions\" : [\n" +
                "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/118248891452356849563\\\"\\u003eBELOTTI OtticaUdito\\u003c/a\\u003e\"\n" +
                "               ],\n" +
                "               \"photo_reference\" : \"CmRaAAAAt2cerX0EJlAC1Zocg4lfQ3GH9tDTdscBFYIXalVHNjP3BNdZfG7TsIsXAFGWxlGlXXxm_irWwQ3yXvpTt12TdNgbOqzUc2s9pZQ1_fskEWlfAjbrwuXJAWqVnr7XYbLXEhDVtuBLB7w36eo1sY9521mhGhR3AOjdmR14N6U2o1-aUu0PhF0x9A\",\n" +
                "               \"width\" : 6016\n" +
                "            }\n" +
                "         ],\n" +
                "         \"place_id\" : \"ChIJX4GaYY4thEcRCe5-QVnOR0o\",\n" +
                "         \"plus_code\" : {\n" +
                "            \"compound_code\" : \"2W2X+JV Lugano, Svizzera\",\n" +
                "            \"global_code\" : \"8FRC2W2X+JV\"\n" +
                "         },\n" +
                "         \"rating\" : 4.7,\n" +
                "         \"reference\" : \"ChIJX4GaYY4thEcRCe5-QVnOR0o\",\n" +
                "         \"scope\" : \"GOOGLE\",\n" +
                "         \"types\" : [ \"health\", \"point_of_interest\", \"store\", \"establishment\" ],\n" +
                "         \"user_ratings_total\" : 15,\n" +
                "         \"vicinity\" : \"Via Nassa 19, Lugano\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"status\" : \"OK\"\n" +
                "}\n";
    }
}
