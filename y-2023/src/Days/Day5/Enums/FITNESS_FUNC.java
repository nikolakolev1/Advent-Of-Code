package Days.Day5.Enums;

public enum FITNESS_FUNC {
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