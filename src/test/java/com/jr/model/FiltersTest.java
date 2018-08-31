package com.jr.model;

/**
 * @author Galatyuk Ilya
 */
public class FiltersTest {

    /*@Test
    public void basicOps() {
        Filter filters = new Filter();

        FilterOne filter0 = filters.addNewFilter();
        filter0.addComparison(CritHardcode.ratingCrit, ComparisonOption.MoreOrEquals, 7);
        filter0.addComparison(CritHardcode.weightCrit, ComparisonOption.MoreOrEquals, 700);

        FilterOne filter1 = filters.addNewFilter();
        filter1.addComparison(CritHardcode.ratingCrit, ComparisonOption.Equals, null);
        filter1.addComparison(CritHardcode.noveltyCrit, ComparisonOption.MoreOrEquals, 900);
        filters.remove(filter1, CritHardcode.ratingCrit);

        Assert.assertEquals(2, filters.getFilters().size());

        Assert.assertEquals(filters.getFilters().get(0), filter0);
        Assert.assertEquals(2, filters.getFilters().get(0).getFilter().length);

        Assert.assertEquals(1, filters.getFilters().get(1).getFilter().length);
        Comparison comparison = new Comparison(CritHardcode.noveltyCrit, ComparisonOption.MoreOrEquals, 900);
        Assert.assertEquals(comparison, filters.getFilters().get(1).getFilter()[0]);
    }

    @Test(expected = RuntimeException.class)
    public void moreThanMaxValue() {
        Filter filters = new Filter();

        FilterOne filter0 = filters.addNewFilter();
        filter0.addComparison(CritHardcode.ratingCrit, ComparisonOption.MoreOrEquals, 9999);
    }*/

    //todo after filters actually filter, test with preparation method(del all, fill some crits and songs), that tests hierarchy too

}
