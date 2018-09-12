package com.jr.logic;

import com.jr.TestHelper;
import com.jr.model.Crit;
import com.jr.model.Filter;
import com.jr.model.Song;
import com.jr.model.sub.Comparison;
import com.jr.service.CritService;
import com.jr.service.SongService;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Galatyuk Ilya
 */
public class FilterLogicTest {

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
    public void emptyFilters() {
        Filter filter = new Filter(null);
        Assert.assertEquals(SongService.getAll(), FilterLogic.getSongList(filter, null));

        filter = new Filter("");
        Assert.assertEquals(SongService.getAll(), FilterLogic.getSongList(filter, null));

        filter = new Filter("", new Comparison[0]);
        Assert.assertEquals(SongService.getAll(), FilterLogic.getSongList(filter, null));

        filter = new Filter("", new ArrayList<>());
        Assert.assertEquals(SongService.getAll(), FilterLogic.getSongList(filter, null));
    }

    @Test
    public void blacklist() {
        Filter filter = new Filter(null);
        List<Song> blacklist = new ArrayList<>();
        blacklist.add(SongService.getAll().get(0));
        blacklist.add(SongService.getAll().get(2));
        List<Song> expected = new ArrayList<>(SongService.getAll());
        expected.removeAll(blacklist);

        Assert.assertEquals(expected, FilterLogic.getSongList(filter, blacklist));
    }

    @Test
    public void simpleFilters() {
        Filter filter = new Filter("[rating > 3]");

        List<Song> expected = SongService.getAll().stream()
                .filter(song -> song.getCrits().get(CritHardcode.ratingCrit) != null
                        && song.getCrits().get(CritHardcode.ratingCrit) > 3)
                .collect(Collectors.toList());
        Assert.assertEquals(expected, FilterLogic.getSongList(filter, null));

        filter = new Filter("[wordless == null]");
        expected = SongService.getAll().stream()
                .filter(song -> song.getCrits().get(TestHelper.wordless) == null)
                .collect(Collectors.toList());
        Assert.assertEquals(expected, FilterLogic.getSongList(filter, null));

        filter = new Filter("[wordless != null]");
        expected = SongService.getAll().stream()
                .filter(song -> song.getCrits().get(TestHelper.wordless) == null)
                .collect(Collectors.toList());
        Assert.assertEquals(expected, FilterLogic.getSongList(filter, null));
    }

    @Test
    public void multiComparison() {
        Filter filter = new Filter("([wordless == null] | [epic >= 6]) & [energetic > 5]");
        List<Song> expected = SongService.getAll().stream()
                .filter(song -> (song.getCrits().get(TestHelper.wordless) == null
                        || song.getCrits().get(TestHelper.epic) != null && song.getCrits().get(TestHelper.epic) >= 6)
                        && song.getCrits().get(TestHelper.energetic) != null && song.getCrits().get(TestHelper.energetic) > 5)
                .collect(Collectors.toList());
        Assert.assertEquals(expected, FilterLogic.getSongList(filter, null));

        filter = new Filter("[rating == null] | [rating >= 9]");
        expected = SongService.getAll().stream()
                .filter(song -> song.getCrits().get(CritHardcode.ratingCrit) == null
                        || song.getCrits().get(CritHardcode.ratingCrit) >= 9)
                .collect(Collectors.toList());
        Assert.assertEquals(expected, FilterLogic.getSongList(filter, null));
    }

    @Test
    public void hierarchy() {
        Filter filter = new Filter("[metal == null] | [metal > 7]");
        Set<Crit> metalHierarchy = CritService.getAllHierarchyDown(TestHelper.metal);
        List<Song> expected = SongService.getAll().stream()
                .filter(song -> {
                    boolean containsMetal = false;
                    for (Map.Entry<Crit, Integer> entry : song.getCrits().entrySet()) {
                        if (metalHierarchy.contains(entry.getKey())) {
                            if (entry.getValue() > 7)
                                return true;
                            containsMetal = true;
                        }
                    }
                    return !containsMetal;
                })
                .collect(Collectors.toList());
        Assert.assertEquals(expected, FilterLogic.getSongList(filter, null));
    }

    @Ignore
    @Test
    public void complicated() {
        Filter filter = new Filter("([rating == null] | [mood == null] | [genre == null]) | [cheerful >= 8]");
        List<Song> blacklist = Collections.singletonList(TestHelper.avicii_WakeMeUp);
        List<Song> expected = Arrays.asList(
                TestHelper.mozart_RondoAllaTurka
                , TestHelper.wow_AntechmberOfUlduar
                , TestHelper.nightwish_Stargazers
                , TestHelper.scooter_AiiiShotTheDj
                , TestHelper.therion_SummernightCity
                , TestHelper.omnia_FeeRaHuri_Live
                , TestHelper.brunuhVille_SpiritOfTheWind
                , TestHelper.misc_LeprechaunsDance
                , TestHelper.pakito_LivingOnVideo
                , TestHelper.pyramid_Wolf
                , TestHelper.shadManning_CrusadeOfCannyr
                , TestHelper.scorpions_hurricane2000
                , TestHelper.halo_theGlitteringBand);

        List<Song> result = FilterLogic.getSongList(filter, blacklist);
        Assert.assertEquals(expected, result);
    }
}
