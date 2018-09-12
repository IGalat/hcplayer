package com.jr.service;

import com.jr.TestHelper;
import com.jr.model.Crit;
import com.jr.model.NormalPlaylist;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * @author Galatyuk Ilya
 */
public class NormalPlaylistServiceTest {

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @Before
    public void initMethod() {
        TestHelper.setStandardTestData();
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(Defaults.CONFIG_FOLDER);
    }

    @Test
    public void deleteAll() {
        List<NormalPlaylist> playlists = NormalPlaylistService.getAll();
        for (int i = playlists.size(); i > 0; i--) {
            NormalPlaylistService.remove(playlists.get(i - 1));
        }

        Assert.assertTrue(playlists.isEmpty());
    }

    @Test
    public void fillSome() {
        deleteAll();

        NormalPlaylist empty = NormalPlaylistService.save("Empty");

        NormalPlaylistService.save("Epic", null
                , TestHelper.wow_AntechmberOfUlduar
                , TestHelper.therion_SummernightCity
                , TestHelper.powerwolf_DieDieCrucified
                , TestHelper.scorpions_hurricane2000
                , TestHelper.halo_theGlitteringBand);

        NormalPlaylist epic = NormalPlaylistService.getByName("Epic");

        NormalPlaylist metal = NormalPlaylistService.save("Metal heaven");
        NormalPlaylistService.addSongs(metal
                , TestHelper.dragonforce_StrikeOfTheNinja
                , TestHelper.eluveitie_Lvgvs
                , TestHelper.nightwish_FeelForYou
                , TestHelper.nightwish_Stargazers
                , TestHelper.rhapsodyOfFire_ReignOfTerror
                , TestHelper.soil_BreakingMeDown
                , TestHelper.pain_SameOldSong
                , TestHelper.powerwolf_DieDieCrucified);

        NormalPlaylistService.rename(metal, "Metal");

        NormalPlaylistService.getByIds(Arrays.asList(epic.getId(), metal.getId()));

        NormalPlaylistService.remove(empty);

        Assert.assertEquals(2, NormalPlaylistService.getAll().size());

        for (Song song : epic.getSongs())
            Assert.assertTrue(isCritPresent(song, TestHelper.epic));

        for (Song song : metal.getSongs())
            Assert.assertTrue(isCritPresent(song, TestHelper.metal));
    }

    private boolean isCritPresent(Song song, Crit critToCheck) {
        Set<Crit> critHierarchy = CritService.getAllHierarchyDown(critToCheck);
        for (Crit crit : critHierarchy)
            if (song.getCrits().containsKey(crit)) return true;

        return false;
    }


    @Test(expected = RuntimeException.class)
    public void badName() {
        NormalPlaylistService.save("qwerty==+||][");
    }

}
