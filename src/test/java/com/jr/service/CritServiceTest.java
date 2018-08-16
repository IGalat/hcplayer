package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.structure.model.Crit;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void deleteAll() {
        List<Crit> crits = CritService.getAll();
        for (int i = crits.size(); i > 0; i--) {
            CritService.remove(crits.get(i - 1));
        }

        Assert.assertEquals(CritHardcode.STANDARD_CRITS_NAMES.length, crits.size());
    }

    @Test
    public void fillSome() {
        deleteAll(); //конвенции не соблюдаем!

        List<Crit> expectedCritList = new ArrayList<>();
        expectedCritList.add(CritHardcode.ratingCrit);
        expectedCritList.add(CritHardcode.noveltyCrit);
        expectedCritList.add(CritHardcode.weightCrit);
        expectedCritList.add(CritService.save("testCrit 1"));
        expectedCritList.add(CritService.save("testCrit 2", false));
        expectedCritList.add(CritService.save("testCrit 3", 0, 1));

        List<Crit> critList = CritService.getAll();

        Assert.assertEquals(expectedCritList.size(), critList.size());
        for (Crit expectedCrit : expectedCritList) {
            Crit crit = CritService.getOne(expectedCrit.getId());
            Assert.assertEquals(expectedCrit, crit);

            crit = CritService.getByName(expectedCrit.getName());
            Assert.assertEquals(expectedCrit, crit);
        }
    }

    @Test(expected = RuntimeException.class)
    public void minNotLessThanMax() {
        CritService.save("must fail - min equals max", 10, 10);
    }
}
