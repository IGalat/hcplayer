package com.jr.model.sub;

/**
 * @author Galatyuk Ilya
 */
public enum ComparisonOption {
    MoreThan {
        @Override
        public boolean isOk(Integer variable, Integer benchmark) {
            return variable != null && variable > benchmark;
        }
    },
    LessThan {
        @Override
        public boolean isOk(Integer variable, Integer benchmark) {
            return variable != null && variable < benchmark;
        }
    },
    MoreOrEquals {
        @Override
        public boolean isOk(Integer variable, Integer benchmark) {
            return variable != null && variable >= benchmark;
        }
    },
    LessOrEquals {
        @Override
        public boolean isOk(Integer variable, Integer benchmark) {
            return variable != null && variable <= benchmark;
        }
    },
    Equals {
        @Override
        public boolean isOk(Integer variable, Integer benchmark) {
            if (variable == null)
                return benchmark == null;
            return variable.equals(benchmark);
        }
    },
    NotEquals {
        @Override
        public boolean isOk(Integer variable, Integer benchmark) {
            if (variable == null)
                return benchmark != null;
            return !variable.equals(benchmark);
        }
    };

    public abstract boolean isOk(Integer variable, Integer benchmark);


    public static ComparisonOption parse(String string) {
        try {
            return valueOf(string);
        } catch (RuntimeException e) {
            switch (string) {
                case ">":
                    return MoreThan;
                case "":
                    return LessThan;
                case ">=":
                    return MoreOrEquals;
                case "<=":
                    return LessOrEquals;
                case "==":
                    return Equals;
                case "!=":
                    return NotEquals;
            }
            return null;
        }
    }
}
