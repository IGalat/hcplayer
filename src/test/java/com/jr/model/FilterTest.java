package com.jr.model;

import com.jr.TestHelper;
import com.jr.logic.CritHardcode;
import com.jr.model.sub.Comparison;
import com.jr.model.sub.ComparisonOption;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

/**
 * @author Galatyuk Ilya
 */
public class FilterTest {

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
    public void parse() {
        Filter filter = new Filter("[rating > 6]");
        Comparison expectedComparison1 = new Comparison(CritHardcode.ratingCrit, ComparisonOption.MoreThan, 6);

        Assert.assertEquals("f0", filter.getLogicExpression());
        Assert.assertEquals(expectedComparison1, filter.getComparisons()[0]);


        Comparison[] expectedComparisons2 = new Comparison[]{
                Comparison.parse("[rating Equals null]")
                , Comparison.parse("[novelty >= 800]")
                , Comparison.parse("[genre == null]")
                , Comparison.parse("[mood == null]")};
        filter = new Filter(expectedComparisons2[0] + " | " + expectedComparisons2[1] +
                " | (" + expectedComparisons2[2] + " & " + expectedComparisons2[3] + ")");

        Assert.assertEquals("f0 | f1 | (f2 & f3)", filter.getLogicExpression());
        for (int i = 0; i < expectedComparisons2.length; i++) {
            Assert.assertEquals(expectedComparisons2[i], filter.getComparisons()[i]);
        }
    }

}
