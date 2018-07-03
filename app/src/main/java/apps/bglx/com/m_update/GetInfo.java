package apps.bglx.com.m_update;

import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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

    public List<List<String>> artistSearch(String artistName) {
        String queryInfo,
                artistInfo1, artistInfo2, artistInfo3,
                artistName1, artistName2, artistName3,
                artistId1, artistId2,artistId3,
                artistImage1, artistImage2, artistImage3;
        List<String> artistIds = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();
        List<String> artistImages = new ArrayList<>();
        List<List<String>> artistSearchInfos = new ArrayList<>();
        String url = "https://www.deezer.com/search/" + artistName + "/artist";
        try {
            String code =  getURLSource(url);
            queryInfo = code.substring(code.indexOf("\"QUERY\":"));
            artistInfo1 = queryInfo.substring(queryInfo.indexOf("\"ART_ID\":") + 10);
            artistInfo2 = artistInfo1.substring(artistInfo1.indexOf("\"ART_ID\":") + 10);
            artistInfo3 = artistInfo2.substring(artistInfo2.indexOf("\"ART_ID\":") + 10);

            artistId1 = artistInfo1.substring(0,artistInfo1.indexOf('"'));
            artistName1 = artistInfo1.substring(artistInfo1.indexOf("\"ART_NAME\":") + 12);
            artistName1 = artistName1.substring(0, artistName1.indexOf('"'));
            artistImage1 = artistInfo1.substring(artistInfo1.indexOf("\"ART_PICTURE\":") + 15);
            artistImage1 = artistImage1.substring(0, artistImage1.indexOf('"'));

            artistId2 = artistInfo2.substring(0,artistInfo2.indexOf('"'));
            artistName2 = artistInfo2.substring(artistInfo2.indexOf("\"ART_NAME\":") + 12);
            artistName2 = artistName2.substring(0, artistName2.indexOf('"'));
            artistImage2 = artistInfo2.substring(artistInfo2.indexOf("\"ART_PICTURE\":") + 15);
            artistImage2 = artistImage2.substring(0, artistImage2.indexOf('"'));

            artistId3 = artistInfo3.substring(0,artistInfo3.indexOf('"'));
            artistName3 = artistInfo3.substring(artistInfo3.indexOf("\"ART_NAME\":") + 12);
            artistName3 = artistName3.substring(0, artistName3.indexOf('"'));
            artistImage3 = artistInfo3.substring(artistInfo3.indexOf("\"ART_PICTURE\":") + 15);
            artistImage3 = artistImage3.substring(0, artistImage3.indexOf('"'));

            artistIds.add(artistId1);
            artistIds.add(artistId2);
            artistIds.add(artistId3);
            artistNames.add(artistName1.replace("\\u00e9","é"));
            artistNames.add(artistName2.replace("\\u00e9","é"));
            artistNames.add(artistName3.replace("\\u00e9","é"));
            artistImages.add(artistImage1);
            artistImages.add(artistImage2);
            artistImages.add(artistImage3);
            artistSearchInfos.add(artistIds);
            artistSearchInfos.add(artistNames);
            artistSearchInfos.add(artistImages);

        } catch (Exception e) {

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
