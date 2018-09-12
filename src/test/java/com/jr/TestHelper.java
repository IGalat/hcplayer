package com.jr;

import com.jr.logic.CritHardcode;
import com.jr.logic.FlavorLogic;
import com.jr.model.*;
import com.jr.service.*;
import javafx.util.Pair;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Galatyuk Ilya
 */
public class TestHelper {
    public static Crit wordless;
    public static Crit nostalgic;
    public static Crit heavy;
    public static Crit background;

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
    public static Song avicii_WakeMeUp;
    public static Song brunuhVille_SpiritOfTheWind;
    public static Song misc_LeprechaunsDance;
    public static Song pakito_LivingOnVideo;
    public static Song pyramid_Wolf;
    public static Song shadManning_CrusadeOfCannyr;
    public static Song scorpions_hurricane2000;
    public static Song halo_theGlitteringBand;

    public static NormalPlaylist epicNormalPlaylist;
    public static NormalPlaylist metalNormalPlaylist;

    //convenience method: set up things in non-test resources
    @Ignore
    @Test
    public void setResources() {
        setStandardTestData();
    }

    public static void setStandardTestData() {
        setStandardCrits();
        setStandardSongs();
        setStandardNormalPlaylists();
        setStandardFilteredPlaylists();
    }

    private static void setStandardCrits() {
        new CritServiceTest().deleteAll();

        wordless = CritService.save("wordless", 1, 1);

        nostalgic = CritService.save("nostalgic");
        heavy = CritService.save("heavy");
        background = CritService.save("background");

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

        String prefix = "src\\main\\resources\\songs\\";

        vivaldi_TangoOfDeath = SongService.save(prefix + "Antonio Vivaldi - Tango of Death.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(classic, 10), new Pair<>(energetic, 7), new Pair<>(wordless, 1));
        mozart_RondoAllaTurka = SongService.save(prefix + "Wolfgang Amadeus Mozart - Rondo Alla Turka (Соната №11 - 3 часть).mp3"
                , new Pair<>(CritHardcode.ratingCrit, 4), new Pair<>(classic, 10), new Pair<>(energetic, 8), new Pair<>(cheerful, 9), new Pair<>(wordless, 1));
        wow_AntechmberOfUlduar = SongService.save(prefix + "WoW - Antechamber of Ulduar.mp3"
                , new Pair<>(classic, 7), new Pair<>(epic, 8), new Pair<>(wordless, 1));
        dragonforce_StrikeOfTheNinja = SongService.save(prefix + "Dragonforce - Strike Of The Ninja.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 5), new Pair<>(powerMetal, 10), new Pair<>(energetic, 10));
        eluveitie_Lvgvs = SongService.save(prefix + "Eluveitie - Lvgvs.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(metal, 4), new Pair<>(folkMetal, 8), new Pair<>(energetic, 6), new Pair<>(singsong, 7));
        nightwish_FeelForYou = SongService.save(prefix + "Nightwish - Feel For You.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 6), new Pair<>(symphonicPowerMetal, 7), new Pair<>(calm, 5), new Pair<>(energetic, 5));
        nightwish_Stargazers = SongService.save(prefix + "Nightwish - Stargazers.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 5), new Pair<>(symphonicPowerMetal, 9));
        rhapsodyOfFire_ReignOfTerror = SongService.save(prefix + "Rhapsody of Fire - Reign Of Terror.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(powerMetal, 9), new Pair<>(energetic, 10));
        scooter_AiiiShotTheDj = SongService.save(prefix + "Scooter - Aiii Shot The Dj.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(energetic, 10), new Pair<>(club, 10), new Pair<>(singsong, 6), new Pair<>(cheerful, 8));
        soil_BreakingMeDown = SongService.save(prefix + "Soil - Breaking Me Down.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 6), new Pair<>(energetic, 8), new Pair<>(metal, 8));
        therion_SummernightCity = SongService.save(prefix + "Therion - Summernight City.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(epic, 8), new Pair<>(energetic, 6));
        omnia_FeeRaHuri_Live = SongService.save(prefix + "OMNIA (Official) - Fee Ra Huri.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 10), new Pair<>(folk, 7), new Pair<>(cheerful, 8), new Pair<>(energetic, 9));
        pain_SameOldSong = SongService.save(prefix + "Pain - Same Old Song(Industrial Metal).mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(metal, 9), new Pair<>(sad, 6));
        powerwolf_DieDieCrucified = SongService.save(prefix + "Powerwolf - Die, Die, Crucified.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(powerMetal, 10), new Pair<>(epic, 8), new Pair<>(energetic, 8));
        avicii_WakeMeUp = SongService.save(prefix + "Avicii - wake me up.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(energetic, 7), new Pair<>(singsong, 8), new Pair<>(cheerful, 10));
        brunuhVille_SpiritOfTheWind = SongService.save(prefix + "BrunuhVille - Spirit of the Wild.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 5), new Pair<>(wordless, 1), new Pair<>(epic, 4), new Pair<>(calm, 5));
        misc_LeprechaunsDance = SongService.save(prefix + "Misc - Leprechauns Dance.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 7), new Pair<>(wordless, 1), new Pair<>(cheerful, 6));
        pakito_LivingOnVideo = SongService.save(prefix + "Pakito - Living On Video.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 4), new Pair<>(wordless, 1), new Pair<>(energetic, 9), new Pair<>(club, 8));
        pyramid_Wolf = SongService.save(prefix + "Pyramid - Wolf.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 6), new Pair<>(wordless, 1), new Pair<>(energetic, 6));
        shadManning_CrusadeOfCannyr = SongService.save(prefix + "Shad Manning - Crusade of Crannhyr.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(wordless, 1), new Pair<>(energetic, 4), new Pair<>(epic, 6));
        scorpions_hurricane2000 = SongService.save("C:\\_my\\Музыка\\Scorpions\\Scorpions und die Berliner Phi - Hurricane 2000.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(energetic, 6), new Pair<>(epic, 9));
        halo_theGlitteringBand = SongService.save("C:\\Users\\admin\\Desktop\\Halo 2 - This Glittering Band.mp3"
                , new Pair<>(CritHardcode.ratingCrit, 8), new Pair<>(wordless, 1), new Pair<>(epic, 6));
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
                , powerwolf_DieDieCrucified
                , brunuhVille_SpiritOfTheWind
                , shadManning_CrusadeOfCannyr
                , scorpions_hurricane2000
                , halo_theGlitteringBand);

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

    private static void setStandardFilteredPlaylists() {
        new FilteredPlaylistServiceTest().deleteAll();

        FilteredPlaylistService.save("New songs", new Filter("[rating == null] | [genre == null] | [mood == null] | [novelty > 800]"));
        FilteredPlaylistService.save("Somewhat calm", new Filter("[calm > 4] | [wordless != null] | [classic > 7]"));
        FilteredPlaylistService.save("Metal", new Filter("[metal >= 5]"));
    }
}
