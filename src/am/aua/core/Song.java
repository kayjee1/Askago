package am.aua.core;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Song extends AudioFile{
    public enum Genre{CLASSICAL, POP, ROCK, JAZZ};


    private Genre genre;
    private String duration;
    //private Duration d;

    public Song(int id,String name, String creator, Genre genre, String filePath){
        super(id,name,creator,filePath);
        this.genre = genre;
    }

    public Genre getGenre() {
        return this.genre;
    }


    public void setGenre(Genre genre) { // consider erasing
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        //d = new Duration();
        //this.duration = d.getDuration(this);
    }

 /*   public String toString() {
            return
                    "title: " + this.getName()  +
                    ", artist: " + this.getCreator() +
                    ", genre: " + this.getGenre();
    }

  */

    public String toString() {
        final int COLUMN_WIDTH = 40; // Define a width for each column

        // Format the title, artist, and genre values with padding
        String formattedTitle = String.format("%-" + COLUMN_WIDTH + "s", "Title: " + this.getName());
        String formattedArtist = String.format("%-" + COLUMN_WIDTH + "s", "Artist: " + this.getCreator());
        String formattedGenre = String.format("%-" + COLUMN_WIDTH + "s", "Genre: " + this.getGenre());

        // Concatenate the formatted values and return
        return formattedTitle + formattedArtist + formattedGenre;
    }

}