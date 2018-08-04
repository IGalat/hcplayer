package com;

import com.jr.structure.model.Song;
import com.jr.structure.service.SongService;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Galatyuk Ilya
 */
public class SomeExecution {
    public static void main(String[] args) {
        //это обязательно для инициализации javafx медиа плеера зачем-то. похер, просто раз в начале строку хуячь
        final JFXPanel fxPanel = new JFXPanel();

        Song song = SongService.getAll().get(6);

        System.out.println(song.getPath().toUri().toString());
        System.out.println(song.getPath().toString());

        // .toUri - обязательный шаг. надо будет сделать обертки удобные в Song...
        play(song.getPath().toUri().toString());
    }

    private static void addSongs() {
        SongService.save("src/main/resources/songs/Avicii - wake me up.mp3", null);
        SongService.save("src/main/resources/songs/BrunuhVille - Spirit of the Wild.mp3", null);
        SongService.save("src/main/resources/songs/Misc - Leprechauns Dance.mp3", null);
        SongService.save("src/main/resources/songs/Pakito - Living On Video.mp3", null);
        SongService.save("src/main/resources/songs/Pyramid - Wolf.mp3", null);
        SongService.save("src/main/resources/songs/Shad Manning - Crusade of Crannhyr.mp3", null);
        SongService.save("C:\\_my\\Музыка\\Scorpions\\Scorpions und die Berliner Phi - Hurricane 2000.mp3", null);
    }

    private static void play(String name){
        Media media = new Media(name);
        MediaPlayer mp = new MediaPlayer(media);
        mp.setVolume(0.5);
        mp.play();
    }
}
