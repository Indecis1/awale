package fr.univ_cote_azur.ai_game_programming;

public enum Color {
    RED{
        @Override
        public String toString() {
            return "R";
        }
    }, BLUE{
        @Override
        public String toString() {
            return "B";
        }
    }, TRANSPARENT_RED{
        @Override
        public String toString() {
            return "TR";
        }
    }, TRANSPARENT_BLUE{
        @Override
        public String toString() {
            return "TB";
        }
    };

    public static Color parse(String str){
        if(str.equalsIgnoreCase("R")){
            return Color.RED;
        } else if (str.equalsIgnoreCase("B")) {
            return Color.BLUE;
        } else if (str.equalsIgnoreCase("TR")) {
            return Color.TRANSPARENT_RED;
        } else if (str.equalsIgnoreCase("TB")) {
            return Color.TRANSPARENT_BLUE;
        }
        return null;
    }
}
