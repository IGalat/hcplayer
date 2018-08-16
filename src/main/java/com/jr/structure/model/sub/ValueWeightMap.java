package com.jr.structure.model.sub;

import lombok.Data;

/**
 * warning: not a real Map!
 *
 * @author Galatyuk Ilya
 */

@Data
public class ValueWeightMap {
    private int[][] map; //array of pairs value-weight

    //this -> logic
    public int calcWeightByValue(int value) {
        int weight = map[0][1];

        for (int[] pair : map) {
            if (value >= pair[0]) weight = value;
            else break;
        }
        return weight;
    }
}
