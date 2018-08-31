package com.jr.logic;

import com.jr.TestHelper;
import com.jr.model.Flavor;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class FlavorLogicTest {

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
    public void defaultFlavor() {
        Flavor defaultFlavor = new Flavor();
        defaultFlavor
                .putCritFlavor(TestHelper.singsong, 50)
                .putCritFlavor(CritHardcode.ratingCrit, 100)
                .putCritFlavor(CritHardcode.weightCrit, 10000)
                .putCritFlavor(TestHelper.cheerful, 10)
                .putCritFlavor(TestHelper.energetic, 3);
        FlavorLogic.saveDefaultFlavor(defaultFlavor);

        Flavor resultDefaultFlavor = FlavorLogic.getDefaultFlavor();

        Assert.assertEquals(defaultFlavor, resultDefaultFlavor);
    }

    @Test
    public void weightMapValues() {
        Map<Song, Integer> weightMap = FlavorLogic.getWeightMap(TestHelper.epicNormalPlaylist);

        //values are hand calculated
        isValueApproximately(42450, weightMap.get(TestHelper.wow_AntechmberOfUlduar));
        isValueApproximately(96398, weightMap.get(TestHelper.nightwish_Stargazers));
        isValueApproximately(5094, weightMap.get(TestHelper.therion_SummernightCity));
        isValueApproximately(144896, weightMap.get(TestHelper.powerwolf_DieDieCrucified));
    }

    private void isValueApproximately(Integer expected, Integer value) {
        Assert.assertTrue("Value is " + value + ", expected " + expected,
                value >= expected * 0.95 && value <= expected * 1.05);
    }

}
