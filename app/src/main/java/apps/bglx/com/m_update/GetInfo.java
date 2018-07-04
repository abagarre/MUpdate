package apps.bglx.com.m_update;

import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetInfo {

    public static String getURLSource(String url) throws Exception
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return toString(urlConnection.getInputStream());
    }

    private static String toString(InputStream inputStream) throws Exception {
        String inputLine = new String();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            return stringBuilder.toString();

        } catch (Exception e) {
            System.out.println("Erreur");
            inputLine = "Error";
            return inputLine;
        }
    }

    public static String getURLToString(String url) throws Exception
    {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String code = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        System.out.println(code);
        return code;
    }


    public List<List<String>> artistSearch(String artistName) {
        String  artistName1, artistId1, artistImage1;
        List<String> artistIds = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();
        List<String> artistImages = new ArrayList<>();
        List<List<String>> artistSearchInfos = new ArrayList<>();
        String url = "https://api.deezer.com/search/artist/?q=" + artistName + "&index=0&limit=3&output=json";
        try {
            String code = getURLToString(url);
            String nb = code.substring(code.indexOf("total") + 7);
            if (!nb.contains(",")) {
                nb = nb.substring(0,nb.indexOf("}"));
            } else {
                nb = nb.substring(0,nb.indexOf(","));
            }
            int nbMax = Integer.parseInt(nb);
            if (nbMax > 3) { nbMax = 3; }
            for (int i = 0; i < nbMax; i++) {
                artistId1 = code.substring(code.indexOf("id") + 4);
                artistIds.add(artistId1.substring(0,artistId1.indexOf(",")));
                artistName1 = formatUnicode(code.substring(code.indexOf("name") + 7));
                artistNames.add(artistName1.substring(0,artistName1.indexOf("\"")));
                artistImage1 = code.substring(code.indexOf("picture_medium") + 17);
                artistImages.add(artistImage1.substring(0,artistImage1.indexOf("\""))
                        .replaceAll("\\\\/","/"));
                code = code.substring(code.indexOf("type") + 6);
            }
            artistSearchInfos.add(artistIds);
            artistSearchInfos.add(artistNames);
            artistSearchInfos.add(artistImages);
        } catch (Exception e) {
            System.out.println("Search Error");
        }
        return artistSearchInfos;
    }


    public String albumName(String url) {
        String albumInfo, albumName;
        try {
            String code =  getURLSource(url);
            albumInfo = code.substring(code.indexOf("\"LAST_ALBUM\":"));
            albumName = albumInfo.substring(albumInfo.indexOf("\"ALB_TITLE\":") + 13);
            albumName = albumName.substring(0, albumName.indexOf('"'));
        } catch (Exception e) {
            albumName = "";
            System.out.println("Album Name Error");
            System.out.println(e.toString());
        }
        return formatUnicode(albumName);
    }

    public String albumDate(String url) {
        String albumInfo, albumDate;
        try {
            String code =  getURLSource(url);
            albumInfo = code.substring(code.indexOf("\"LAST_ALBUM\":"));
            albumDate = albumInfo.substring(albumInfo.indexOf("\"DIGITAL_RELEASE_DATE\":") + 24);
            albumDate = albumDate.substring(0, albumDate.indexOf('"'));
        } catch (Exception e) {
            albumDate = "";
            System.out.println("Album Date Error");
        }
        return albumDate;
    }

    public String albumPicture(String url) {
        String albumInfo, albumPicture;
        try {
            String code =  getURLSource(url);
            albumInfo = code.substring(code.indexOf("\"LAST_ALBUM\":"));
            albumPicture = albumInfo.substring(albumInfo.indexOf("\"ALB_PICTURE\":") + 15);
            albumPicture = albumPicture.substring(0, albumPicture.indexOf('"'));
            albumPicture = "https://e-cdns-images.dzcdn.net/images/cover/" + albumPicture + "/200x200-000000-80-0-0.jpg";
        } catch (Exception e) {
            albumPicture = "";
            System.out.println("Album Picture Error");
        }
        return albumPicture;
    }

    public static String formatUnicode(String escaped) {
        if(!escaped.contains("\\u"))
            return escaped;
        String processed="";
        int position = escaped.indexOf("\\u");
        while(position!=-1) {
            if(position!=0)
                processed += escaped.substring(0,position);
            String token=escaped.substring(position + 2,position + 6);
            escaped=escaped.substring(position+6);
            processed += (char)Integer.parseInt(token,16);
            position=escaped.indexOf("\\u");
        }
        processed += escaped;
        return processed;
    }

}
