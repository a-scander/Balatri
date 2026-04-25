package domain;

public record Blind(String name, int targetScore) {

    @Override
    public String toString() {
        return name + " (cible : " + targetScore + ")";
    }
}