package apps.bglx.com.m_update;

public class ArtistsData {

    private int idArtist;
    private String name;
    private int idDeezer;
    private int idSpotify;
    private int idYoutube;

    public ArtistsData(int idArtist, String name, int idDeezer, int idSpotify, int idYoutube) {
        this.idArtist = idArtist;
        this.name = name;
        this.idDeezer = idDeezer;
        this.idSpotify = idSpotify;
        this.idYoutube = idYoutube;
    }

    public int getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(int idArtist) {
        this.idArtist = idArtist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdDeezer() {
        return idDeezer;
    }

    public void setIdDeezer(int idDeezer) {
        this.idDeezer = idDeezer;
    }

    public int getIdSpotify() {
        return idSpotify;
    }

    public void setIdSpotify(int idSpotify) {
        this.idSpotify = idSpotify;
    }

    public int getIdYoutube() {
        return idYoutube;
    }

    public void setIdYoutube(int idYoutube) {
        this.idYoutube = idYoutube;
    }

    @Override
    public String toString() {
        return name + "," + idDeezer;
    }
}
