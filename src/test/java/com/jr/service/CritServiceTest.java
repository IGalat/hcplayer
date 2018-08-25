package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.model.Crit;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jr.service.CritService.*;

/**
 * @author Galatyuk Ilya
 */
public class CritServiceTest {

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
    }

    public void deleteAll() {
        List<Crit> crits = getAll();
        for (int i = crits.size(); i > 0; i--) {
            remove(crits.get(i - 1));
        }

        //Assert.assertEquals(3, crits.size());
    }

    @Test
    public void fillSome() {
        deleteAll();

        List<Crit> expectedCritList = new ArrayList<>();
        expectedCritList.add(save("testCrit1"));
        expectedCritList.add(save("testCrit2", false));
        expectedCritList.add(save("testCrit3", 0, 1));

        critsAreMatchingFile(expectedCritList);
    }

    @Test
    public void mixedLevelChildren() {
        deleteAll();

        List<Crit> expectedCritList = new ArrayList<>();
        expectedCritList.add(save("testCrit1"));
        expectedCritList.add(save("testCrit2"));
        expectedCritList.add(save("testCrit3"));
        expectedCritList.add(save("testCrit4"));
        expectedCritList.add(save("testCrit5"));

        addChild(getByName("testCrit1"), getByName("testCrit2"));
        addChild(getByName("testCrit2"), getByName("testCrit3"));
        addChild(getByName("testCrit1"), getByName("testCrit4"));
        addChild(getByName("testCrit3"), getByName("testCrit4"));

        List<Crit> testcritList = new ArrayList<>();
        testcritList.add(getByName("testcrit1"));
        testcritList.add(getByName("testcrit2"));
        Crit crit007 = save("testCrit007", 1, 10, DEFAULT_IS_WHITELIST, testcritList);
        addChild(getByName("testcrit2"), crit007); //2 times on purpose - it shouldn't duplicate
        addChild(getByName("testcrit3"), crit007);
        addChild(getByName("testcrit4"), crit007);
        removeChild(getByName("testcrit1"), crit007);

        expectedCritList.add(crit007);

        critsAreMatchingFile(expectedCritList);
    }

    private void critsAreMatchingFile(List<Crit> expectedCritList) {
        expectedCritList.add(CritHardcode.ratingCrit);
        expectedCritList.add(CritHardcode.noveltyCrit);
        expectedCritList.add(CritHardcode.weightCrit);

        List<Crit> critList = getAll();

        Assert.assertEquals(expectedCritList.size(), critList.size());
        for (Crit expectedCrit : expectedCritList) {
            Crit crit = getOne(expectedCrit.getId());
            Assert.assertEquals(expectedCrit, crit);

            crit = getByName(expectedCrit.getName());
            Assert.assertEquals(expectedCrit, crit);
        }
    }

    @Test(expected = RuntimeException.class)
    public void cyclicDependency() {
        deleteAll();

        save("testCrit1");
        save("testCrit2");
        save("testCrit3");

        addChild(getByName("testCrit1"), getByName("testCrit2"));
        addChild(getByName("testCrit2"), getByName("testCrit3"));
        addChild(getByName("testCrit3"), getByName("testCrit1"));
    }

    @Test(expected = RuntimeException.class)
    public void minMoreThanMax() {
        save("must fail - min more than max", 20, 10);
    }
}
