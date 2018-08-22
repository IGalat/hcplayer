package com.jr.structure.model.sub;

import com.jr.structure.model.Crit;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Filter {
    Map<Crit, Comparison> filter = new HashMap<>();
}
