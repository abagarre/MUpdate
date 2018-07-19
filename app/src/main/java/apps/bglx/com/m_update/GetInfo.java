package apps.bglx.com.m_update;

import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        String inputLine;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            return stringBuilder.toString();

        } catch (Exception e) {
            System.out.println("toString Error");
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
            System.out.println(e);
            System.out.println("Search Error");
        }
        return artistSearchInfos;
    }

    public List<String> getLastAlbum(String artistID) throws Exception {
        List<String> lastInfos = new ArrayList<>();
        String url = "https://api.deezer.com/artist/" + artistID + "/albums?limit=200";
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String code = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();

        String strNb = code.substring(code.indexOf("\"total\"") + 8);
        int nb = Integer.parseInt(strNb.substring(0,strNb.indexOf('}')));

        String date = code.substring(code.indexOf("\"release_date\"")+16);
        date = date.substring(0,date.indexOf("\""));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateDate = format.parse(date);
        String title = code.substring(code.indexOf("\"title\"")+9);
        title = title.substring(0,title.indexOf("\","));
        String picURL = code.substring(code.indexOf("\"cover_medium\"")+16);
        picURL = picURL.substring(0,picURL.indexOf("\""));
        String albumId = code.substring(code.indexOf("\"id\"") + 5);
        albumId = albumId.substring(0,albumId.indexOf(","));

        code = code.substring(code.indexOf("\"type\"")+7);

        for (int i = 1; i < nb; i++) {
            String newDate = code.substring(code.indexOf("\"release_date\"")+16);
            newDate = newDate.substring(0,newDate.indexOf("\""));
            Date newDateDate = format.parse(newDate);
            if (dateDate.compareTo(newDateDate) <= 0) {
                dateDate = newDateDate;
                title = code.substring(code.indexOf("\"title\"")+9);
                title = title.substring(0,title.indexOf("\","));
                picURL = code.substring(code.indexOf("\"cover_medium\"")+16);
                picURL = picURL.substring(0,picURL.indexOf("\""));
                albumId = code.substring(code.indexOf("\"id\"") + 5);
                albumId = albumId.substring(0,albumId.indexOf(","));
            }
            code = code.substring(code.indexOf("\"type\"")+7);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.format(dateDate);

        if (!picURL.contains("http")) {
            try {
                String coverCode = getURLSource("https://www.deezer.com/search/" + formatUnicode(title) + "/album");
                coverCode = coverCode.substring(coverCode.indexOf("QUERY"));
                picURL = coverCode.substring(coverCode.indexOf("\"ALB_PICTURE\"") + 15);
                picURL = picURL.substring(0,picURL.indexOf("\""));
                picURL = "https://e-cdns-images.dzcdn.net/images/cover/" + picURL + "/250x250.jpg";
            } catch (Exception e) {
                picURL = "https://e-cdns-images.dzcdn.net/images/cover/250x250.jpg";
            }
        }

        lastInfos.add(formatUnicode(title));
        lastInfos.add(date);
        lastInfos.add(picURL.replaceAll("\\\\/","/"));
        lastInfos.add(albumId);
        return lastInfos;
    }

    public List<String> getArtistPage (String id) throws Exception {
        List<String> artistInfos = new ArrayList<>();
        String code = getURLToString("https://api.deezer.com/artist/" + id);
        String picURL = code.substring(code.indexOf("\"picture_big\"") + 15);
        picURL = picURL.substring(0,picURL.indexOf("\""));
        picURL = picURL.replaceAll("\\\\/","/");
        String name = code.substring(code.indexOf("\"name\"") + 8);
        name = name.substring(0,name.indexOf("\""));
        String nbAlbum = code.substring(code.indexOf("\"nb_album\"") + 11);
        nbAlbum = nbAlbum.substring(0,nbAlbum.indexOf(","));
        artistInfos.add(picURL);
        artistInfos.add(formatUnicode(name));
        artistInfos.add(nbAlbum);
        return artistInfos;
    }


    public List<List> getAlbumInfo (String id) throws Exception {
        List<List> albumDatas = new ArrayList<>();
        List<String> albumInfos = new ArrayList<>();
        List<String> tracks = new ArrayList<>();
        String code = getURLToString("https://api.deezer.com/album/" + id);
        String title = code.substring(code.indexOf("\"title\"") + 9);
        title = title.substring(0,title.indexOf("\","));
        String picURL = code.substring(code.indexOf("\"cover_big\"") + 13);
        picURL = picURL.substring(0,picURL.indexOf("\""));
        picURL = picURL.replaceAll("\\\\/","/");
        String nbtracks = code.substring(code.indexOf("\"nb_tracks\"") + 12);
        nbtracks = nbtracks.substring(0, nbtracks.indexOf(","));
        int nbTracks = Integer.parseInt(nbtracks);
        String date = code.substring(code.indexOf("\"release_date\"")+16);
        date = date.substring(0,date.indexOf("\""));

        String trackName;
        String trackDuration;

        code = code.substring(code.indexOf("\"tracks\""));

        for (int i = 1 ; i <= nbTracks ; i++) {
            trackName = code.substring(code.indexOf("\"title\"") + 9);
            trackName = trackName.substring(0,trackName.indexOf("\","));
            trackDuration = code.substring(code.indexOf("\"duration\"") + 11);
            trackDuration = trackDuration.substring(0,trackDuration.indexOf(","));
            int minutes = Integer.parseInt(trackDuration)/60;
            int secondes = Integer.parseInt(trackDuration) - 60 * minutes;
            if (secondes < 10) {
                trackDuration = Integer.toString(minutes) + ":0" + Integer.toString(secondes);
            } else {
                trackDuration = Integer.toString(minutes) + ":" + Integer.toString(secondes);
            }
            if (formatUnicode(trackName).length() > 25) {
                tracks.add(formatUnicode(trackName).substring(0,24) + "...");
            } else {
                tracks.add(formatUnicode(trackName));
            }
            tracks.add(trackDuration);
            code = code.substring(code.indexOf("\"track\"") + 9);
        }

        albumInfos.add(picURL);
        albumInfos.add(formatUnicode(title));
        albumInfos.add(date);
        albumInfos.add(Integer.toString(nbTracks));

        albumDatas.add(albumInfos);
        albumDatas.add(tracks);

        return albumDatas;
    }


    public static String formatUnicode(String escaped) {
        if(!escaped.contains("\\u"))
            return escaped.replaceAll("\\\\","");
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
        return processed.replaceAll("\\\\","");
    }

}
