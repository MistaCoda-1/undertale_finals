package db;

/** One row of the leaderboard: where they placed, who they are, how far they got. */
public class LeaderboardEntry {

    private final int rank;
    private final String username;
    private final int highestLevel;

    public LeaderboardEntry(int rank, String username, int highestLevel) {
        this.rank = rank;
        this.username = username;
        this.highestLevel = highestLevel;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getHighestLevel() {
        return highestLevel;
    }
}