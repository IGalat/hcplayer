package com.jr;

import com.jr.logic.CritHardcode;
import com.jr.logic.FlavorLogic;
import com.jr.model.Crit;
import com.jr.model.Flavor;
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
    public static Crit wordless;
    public static Crit nostalgic;

    public static Crit genre;
    public static Crit metal;
    public static Crit folk;
    public static Crit folkMetal;
    public static Crit powerMetal;
    public static Crit symphonicPowerMetal;
    public static Crit classic;
    public static Crit renaissance;

    public static Crit mood;
    public static Crit cheerful;
    public static Crit calm;
    public static Crit sad;
    public static Crit energetic;
    public static Crit epic;
    public static Crit singsong;
    public static Crit club;

    public static Song vivaldi_TangoOfDeath;
    public static Song mozart_RondoAllaTurka;
    public static Song wow_AntechmberOfUlduar;
    public static Song dragonforce_StrikeOfTheNinja;
    public static Song eluveitie_Lvgvs;
    public static Song nightwish_FeelForYou;
    public static Song nightwish_Stargazers;
    public static Song rhapsodyOfFire_ReignOfTerror;
    public static Song scooter_AiiiShotTheDj;
    public static Song soil_BreakingMeDown;
    public static Song therion_SummernightCity;
    public static Song omnia_FeeRaHuri_Live;
    public static Song pain_SameOldSong;
    public static Song powerwolf_DieDieCrucified;

    public static NormalPlaylist epicNormalPlaylist;
    public static NormalPlaylist metalNormalPlaylist;

    public static void setStandardTestData() {
        setStandardCrits();
        setStandardSongs();
        setStandardNormalPlaylists();
    }

    private static void setStandardCrits() {
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

    private static void setStandardSongs() {
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

    private static void setStandardNormalPlaylists() {
        new NormalPlaylistServiceTest().deleteAll();
        FlavorLogic.saveDefaultFlavor((Flavor) FlavorLogic.DEFAULT_DEFAULT_FLAVOR.clone());

        Flavor epicFlavor = new Flavor();
        epicFlavor
                .putCritFlavor(CritHardcode.ratingCrit, 10)
                .putCritFlavor(CritHardcode.weightCrit, 10000)
                .putCritFlavor(wordless, 100)
                .putCritFlavor(energetic, 10)
                .putCritFlavor(powerMetal, 80)
                .putCritFlavor(symphonicPowerMetal, 1000);

        epicNormalPlaylist = NormalPlaylistService.save("Epic", epicFlavor
                , wow_AntechmberOfUlduar
                , nightwish_Stargazers
                , therion_SummernightCity
                , powerwolf_DieDieCrucified);

        Flavor metalFlavor = new Flavor();
        metalFlavor
                .putCritFlavor(sad, 1000)
                .putCritFlavor(epic, -100)
                .putCritFlavor(symphonicPowerMetal, 10)
                .putCritFlavor(singsong, 10000);

        metalNormalPlaylist = NormalPlaylistService.save("Metal", metalFlavor
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
