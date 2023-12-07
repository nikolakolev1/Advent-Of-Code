package Days.Day5.Enums;

public enum FITNESS_FUNC {
    MostBitsOn {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Max;
        }
    },

    LeastBitsOn {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Max;
        }
    },

    QuadEquationBoolArray {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Min;
        }
    },

    Tsp {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Min;
        }
    },

    FTTxNVP {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Max;
        }
    },

    SequentialCovering {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Max;
        }
    },

    AocDay5 {
        @Override
        public MIN_MAX getMinMax() {
            return MIN_MAX.Min;
        }
    };

    public MIN_MAX getMinMax() throws Exception {
        throw new Exception("Not implemented");
    }
}