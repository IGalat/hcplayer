package com.jr.structure.model;

import com.jr.logic.CritHardcode;
import com.jr.structure.model.Filters;
import com.jr.structure.model.sub.Comparison;
import com.jr.structure.model.sub.ComparisonOption;
import com.jr.structure.model.sub.Filter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Galatyuk Ilya
 */
public class FiltersTest {

    @Test
    public void basicOps() {
        Filters filters = new Filters();

        Filter filter0 = filters.getNewFilter();
        filters.put(filter0, CritHardcode.ratingCrit, ComparisonOption.MoreOrEquals, 7);
        filters.put(filter0, CritHardcode.weightCrit, ComparisonOption.MoreOrEquals, 700);

        Filter filter1 = filters.getNewFilter();
        filters.put(filter1, CritHardcode.ratingCrit, ComparisonOption.Equals, null);
        filters.put(filter1, CritHardcode.noveltyCrit, ComparisonOption.MoreOrEquals, 900);
        filters.remove(filter1, CritHardcode.ratingCrit);


        Assert.assertEquals(2, filters.getFilters().size());

        Assert.assertEquals(filters.getFilters().get(0), filter0);
        Assert.assertEquals(2, filters.getFilters().get(0).getFilter().size());

        Assert.assertEquals(1, filters.getFilters().get(1).getFilter().size());
        Comparison comparison = new Comparison(ComparisonOption.MoreOrEquals, 900);
        Assert.assertEquals(comparison, filters.getFilters().get(1).getFilter().get(CritHardcode.noveltyCrit));
    }

    @Test(expected = RuntimeException.class)
    public void moreThanMaxValue() {
        Filters filters = new Filters();

        Filter filter0 = filters.getNewFilter();
        filters.put(filter0, CritHardcode.ratingCrit, ComparisonOption.MoreOrEquals, 9999);
    }

    //todo after filters actually filter, test with preparation method(del all, fill some crits and songs), that tests hierarchy too

}
