import Exceptions.SongNotFoundException;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;


public class Operations { //change name to smth better?


    public class Duration{
        private long hours, minutes, seconds;


        public String getDuration(AudioFile s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
            File audioFile = new File("src/songs" + File.separator + s.getFilePath());

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            long duration = clip.getMicrosecondLength() / 1_000_000;
            seconds = duration % 60;
            minutes = (duration / 60) % 60;
            hours = duration / 3600;

            return hours + ":" + minutes + ":" + seconds;

        }
    }









    public static class SongPlayer {
        private String folderPath;
        private List<Song> songs;



        public SongPlayer(String folderPath) {
            this.folderPath = folderPath;
            this.songs = loadSongsFromDatabase();

        }



        public List<Song> loadSongsFromDatabase() {
            List<Song> songs = new ArrayList<>();
            try (Scanner scanner = new Scanner(new File("database.txt"))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String creator = parts[2];
                  //  int duration = Integer.parseInt(parts[3]);
                    Song.Genre genre = Song.Genre.valueOf(parts[3]);
                    String filePath = parts[4];
                    songs.add(new Song(id, name, creator, genre, filePath));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return songs;

        }

        public void listSongs() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
            System.out.println("Available songs:");
            System.out.println("=====================");
            for (Song song : songs) {
                System.out.println(song);
            }

            System.out.println("=====================");
        }

        public void playSong(int id) throws SongNotFoundException {
            Song selectedSong = findSongById(id);
            if (selectedSong != null) {
                File songFile = new File(folderPath + File.separator + selectedSong.getFilePath());
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);

             /*       long duration = clip.getMicrosecondLength() / 1_000_000;
                    long seconds = duration % 60;
                    long minutes = (duration / 60) % 60;
                    long hours = duration / 3600;

                    System.out.println(hours + " : " + minutes + " : " + seconds);

              */
                    Scanner sc = new Scanner(System.in);
                    String response = "";


                    while(!response.equals("Q")){
                        System.out.println("P = play, S = stop, R = reset, Q = quit");
                        System.out.println("Enter your choice: ");
                        response = sc.next().toUpperCase();

                        switch(response){
                            case("P"): clip.start();break;
                            case("S"): clip.stop(); break;
                            case("R"): clip.setMicrosecondPosition(0); break;
                            case("Q"): clip.close(); break;

                        }
                    }

                    audioInputStream.close();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Song with ID " + id + " not found.");
            }
        }

        public Song findSongById(int id) {
            for (Song song : songs) {
                if (song.getId() == id) {
                    return song;
                }
            }
            return null;
        }

        public void deleteSong(int id) throws SongNotFoundException {
            Song songToDelete = findSongById(id);
            if (songToDelete != null) {
                songs.remove(songToDelete);
                updateDatabase();
                System.out.println("Song with ID " + id + " has been deleted.");
            } else {
                throw new SongNotFoundException("Song with ID " + id + " not found.");
            }
        }

        public void updateDatabase() {
            try (PrintWriter writer = new PrintWriter(new FileWriter("database.txt"))) {
                for (Song song : songs) {
                    writer.println(song.getId() + "," + song.getName() + "," + song.getCreator() +
                            ","  + song.getGenre() + "," + song.getFilePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void createPlaylistByGenre() {
            for (Song.Genre genre : Song.Genre.values()) {
                String genreFileName = genre.toString() + ".txt";
                try (PrintWriter writer = new PrintWriter(new FileWriter(genreFileName))) {
                    for (Song song : songs) {
                        if (song.getGenre() == genre) {
                            writer.println(song.getId() + "," + song.getName() + "," + song.getCreator() +
                                    "," + song.getGenre() + "," + song.getFilePath());
                        }
                    }
                    System.out.println(genre.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        // boolean flag for shuffle, repeat

        public void loopPlaylist(String response) throws SongNotFoundException{

        }



    }
}