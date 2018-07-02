package apps.bglx.com.m_update;

import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

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

    public String artistID(String artistName) {
        String queryInfo, artistId;
        String url = "https://www.deezer.com/search/" + artistName + "/artist";
        try {
            String code =  getURLSource(url);
            queryInfo = code.substring(code.indexOf("\"QUERY\":"));
            artistId = queryInfo.substring(queryInfo.indexOf("\"ART_ID\":") + 10);
            artistId = artistId.substring(0, artistId.indexOf('"'));
        } catch (Exception e) {
            artistId = "";
        }
        return artistId;
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
        return albumName;
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


}
