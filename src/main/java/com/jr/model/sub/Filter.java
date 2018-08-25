package com.jr.model.sub;

import com.jr.model.Crit;
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
