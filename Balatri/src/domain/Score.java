package domain;

public record Score(int chips, int mult) {
    public int total(){
        return chips * mult;
    }
}
