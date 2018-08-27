package com.jr.logic;

import com.jr.TestHelper;
import com.jr.model.Flavor;
import com.jr.model.Song;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class FlavorLogicTest {
    private final static TestHelper testHelper = new TestHelper();

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @Before
    public void initMethod() {
        testHelper.setStandardTestData();
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
    }

    @Test
    public void defaultFlavor() {
        Flavor defaultFlavor = new Flavor();
        defaultFlavor
                .putCritFlavor(testHelper.singsong, 50)
                .putCritFlavor(CritHardcode.ratingCrit, 100)
                .putCritFlavor(CritHardcode.weightCrit, 10000)
                .putCritFlavor(testHelper.cheerful, 10)
                .putCritFlavor(testHelper.energetic, 3);
        FlavorLogic.saveDefaultFlavor(defaultFlavor);

        Flavor resultDefaultFlavor = FlavorLogic.getDefaultFlavor();

        Assert.assertEquals(defaultFlavor, resultDefaultFlavor);
    }

    @Test
    public void weightMapValues() {
        Map<Song, Integer> weightMap = FlavorLogic.getWeightMap(testHelper.epicNormalPlaylist);

        //values are hand calculated
        isValueApproximately(96398, weightMap.get(testHelper.nightwish_Stargazers));
        isValueApproximately(42450, weightMap.get(testHelper.wow_AntechmberOfUlduar));
        isValueApproximately(5094, weightMap.get(testHelper.therion_SummernightCity));
        isValueApproximately(144896, weightMap.get(testHelper.powerwolf_DieDieCrucified));
    }

    private void isValueApproximately(Integer expected, Integer value) {
        Assert.assertTrue("Value is " + value + ", expected " + expected,
                value >= expected * 0.95 && value <= expected * 1.05);
    }

}
