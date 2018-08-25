package com.jr.service;

import com.jr.TestHelper;
import com.jr.structure.model.Crit;
import com.jr.structure.model.NormalPlaylist;
import com.jr.structure.model.Song;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;


/**
 * @author Galatyuk Ilya
 */
public class NormalPlaylistServiceTest { //todo
    private final static TestHelper testHelper = new TestHelper();

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
        testHelper.setStandardTestData();
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
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
        NormalPlaylist empty = NormalPlaylistService.save("Empty");

        NormalPlaylistService.save("Epic", null
                , testHelper.wow_AntechmberOfUlduar
                , testHelper.nightwish_Stargazers
                , testHelper.therion_SummernightCity
                , testHelper.powerwolf_DieDieCrucified);

        NormalPlaylist epic = NormalPlaylistService.getByName("Epic");

        NormalPlaylist metal = NormalPlaylistService.save("Metal heaven", null
                , testHelper.dragonforce_StrikeOfTheNinja
                , testHelper.eluveitie_Lvgvs
                , testHelper.nightwish_FeelForYou
                , testHelper.nightwish_Stargazers
                , testHelper.rhapsodyOfFire_ReignOfTerror
                , testHelper.soil_BreakingMeDown
                , testHelper.pain_SameOldSong
                , testHelper.powerwolf_DieDieCrucified);

        NormalPlaylistService.rename(metal, "Metal");

        NormalPlaylistService.remove(empty);

        Assert.assertEquals(2, NormalPlaylistService.getAll().size());

        for (Song song : epic.getSongs())
            Assert.assertTrue(isCritPresent(song, testHelper.epic));

        for (Song song : metal.getSongs())
            Assert.assertTrue(isCritPresent(song, testHelper.metal));
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
