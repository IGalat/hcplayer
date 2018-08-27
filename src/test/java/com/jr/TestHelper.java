package com.jr;

import com.jr.logic.CritHardcode;
import com.jr.model.Crit;
import com.jr.model.NormalPlaylist;
import com.jr.model.Song;
import com.jr.service.*;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import javafx.util.Pair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Galatyuk Ilya
 */
public class TestHelper {
    public Crit wordless;
    public Crit nostalgic;

    public Crit genre;
    public Crit metal;
    public Crit folk;
    public Crit folkMetal;
    public Crit powerMetal;
    public Crit symphonicPowerMetal;
    public Crit classic;
    public Crit renaissance;

    public Crit mood;
    public Crit cheerful;
    public Crit calm;
    public Crit sad;
    public Crit energetic;
    public Crit epic;
    public Crit singsong;
    public Crit club;

    public Song vivaldi_TangoOfDeath;
    public Song mozart_RondoAllaTurka;
    public Song wow_AntechmberOfUlduar;
    public Song dragonforce_StrikeOfTheNinja;
    public Song eluveitie_Lvgvs;
    public Song nightwish_FeelForYou;
    public Song nightwish_Stargazers;
    public Song rhapsodyOfFire_ReignOfTerror;
    public Song scooter_AiiiShotTheDj;
    public Song soil_BreakingMeDown;
    public Song therion_SummernightCity;
    public Song omnia_FeeRaHuri_Live;
    public Song pain_SameOldSong;
    public Song powerwolf_DieDieCrucified;

    public NormalPlaylist epicNormalPlaylist;
    public NormalPlaylist metalNormalPlaylist;

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
    }

    @Test
    public void setStandardTestData() {
        setStandardCrits();
        setStandardSongs();
        setStandardNormalPlaylists();
    }

    private void setStandardCrits() {
        new CritServiceTest().deleteAll();

        wordless = CritService.save("wordless", 1, 1);

        nostalgic = CritService.save("nostalgic");

        genre = CritService.save("genre");
        metal = CritService.save("metal", genre);
        folk = CritService.save("folk", genre);
        folkMetal = CritService.save("folk metal", folk, metal);
        powerMetal = CritService.save("power metal", metal);
        symphonicPowerMetal = CritService.save("symphonic power metal", powerMetal);
        classic = CritService.save("classic", genre);
        renaissance = CritService.save("renaissance", classic);

        mood = CritService.save("mood");
        cheerful = CritService.save("cheerful", mood);
        calm = CritService.save("calm", mood);
        sad = CritService.save("sad", mood);
        energetic = CritService.save("energetic", mood);
        epic = CritService.save("epic", mood);
        singsong = CritService.save("singsong", mood);
        club = CritService.save("club", mood);
    }

    private void setStandardSongs() {
        new SongServiceTest().deleteAll();

        vivaldi_TangoOfDeath = SongService.save("Antonio Vivaldi - Tango of Death.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(classic, 10), new Pair<>(energetic, 7), new Pair<>(wordless, 1));
        mozart_RondoAllaTurka = SongService.save("Wolfgang Amadeus Mozart - Rondo Alla Turka.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 4), new Pair<>(classic, 10), new Pair<>(energetic, 8), new Pair<>(cheerful, 9), new Pair<>(wordless, 1));
        wow_AntechmberOfUlduar = SongService.save("Wow - Antechamber of Ulduar.mp3"
                , new Pair<>(classic, 7), new Pair<>(epic, 8), new Pair<>(wordless, 1));
        dragonforce_StrikeOfTheNinja = SongService.save("Dragonforce - Strike Of The Ninja.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 5), new Pair<>(powerMetal, 10), new Pair<>(energetic, 10));
        eluveitie_Lvgvs = SongService.save("Eluveitie - Lvgvs.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(folkMetal, 7), new Pair<>(energetic, 6), new Pair<>(singsong, 7));
        nightwish_FeelForYou = SongService.save("Nightwish - Feel For You.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 6), new Pair<>(symphonicPowerMetal, 7), new Pair<>(calm, 5), new Pair<>(energetic, 5));
        nightwish_Stargazers = SongService.save("Nightwish - Stargazers.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 5), new Pair<>(symphonicPowerMetal, 9), new Pair<>(epic, 6));
        rhapsodyOfFire_ReignOfTerror = SongService.save("Rhapsody of Fire - Reign Of Terror.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(powerMetal, 9), new Pair<>(energetic, 10));
        scooter_AiiiShotTheDj = SongService.save("Scooter - Aiii Shot The Dj.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(energetic, 10), new Pair<>(club, 10), new Pair<>(singsong, 6), new Pair<>(cheerful, 8));
        soil_BreakingMeDown = SongService.save("Soil - Breaking Me Down.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 6), new Pair<>(energetic, 8), new Pair<>(metal, 8));
        therion_SummernightCity = SongService.save("Therion - Summernight City.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(epic, 8), new Pair<>(energetic, 6));
        omnia_FeeRaHuri_Live = SongService.save("OMNIA (Official) - Fee Ra Huri.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 10), new Pair<>(folk, 7), new Pair<>(cheerful, 8), new Pair<>(energetic, 9));
        pain_SameOldSong = SongService.save("Pain - Same Old Song(Industrial Metal).mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(metal, 9), new Pair<>(sad, 6));
        powerwolf_DieDieCrucified = SongService.save("Powerwolf - Die, Die, Crucified.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(powerMetal, 10), new Pair<>(epic, 8), new Pair<>(energetic, 8));
    }

    private void setStandardNormalPlaylists() {
        new NormalPlaylistServiceTest().deleteAll();
        
        epicNormalPlaylist = NormalPlaylistService.save("Epic", null
                , wow_AntechmberOfUlduar
                , nightwish_Stargazers
                , therion_SummernightCity
                , powerwolf_DieDieCrucified);

        metalNormalPlaylist = NormalPlaylistService.save("Metal", null
                , dragonforce_StrikeOfTheNinja
                , eluveitie_Lvgvs
                , nightwish_FeelForYou
                , nightwish_Stargazers
                , rhapsodyOfFire_ReignOfTerror
                , soil_BreakingMeDown
                , pain_SameOldSong
                , powerwolf_DieDieCrucified);
    }
}
