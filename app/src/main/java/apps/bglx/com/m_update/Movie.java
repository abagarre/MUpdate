package apps.bglx.com.m_update;

public class Movie {
    private String title, genre, year, cover;
    private int ID;


    public Movie(String title, String genre, String year, String cover, int ID) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.cover = cover;
        this.ID = ID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
