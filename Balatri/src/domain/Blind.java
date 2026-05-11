package domain;

/*TODO: faire une classe */
public enum Blind {
    SMALL_BLIND  ("Small Blind",   300),
    BIG_BLIND    ("Big Blind",     600),
    BOSS_BLIND   ("Boss Blind",   1200),
    MEGA_BLIND   ("Mega Blind",   2500),
    ULTRA_BLIND  ("Ultra Blind",  5000);

    private final String name;
    private final int targetScore;

    Blind(String name, int targetScore) {
        this.name = name;
        this.targetScore = targetScore;
    }

    public String getName()        { return name; }
    public int getTargetScore()    { return targetScore; }

    @Override
    public String toString() {
        return name + " (cible : " + targetScore + ")";
    }
}