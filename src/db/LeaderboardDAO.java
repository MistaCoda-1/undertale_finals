package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardDAO {

    // Highest level first; alphabetical by username breaks ties so the order is stable.
    private static final String TOP_PLAYERS_SQL =
            "SELECT username, level FROM players "
            + "ORDER BY level DESC, username ASC LIMIT ?";

    // Only ever raises a player's record, never lowers it.
    private static final String UPDATE_HIGHEST_LEVEL_SQL =
            "UPDATE players SET level = ? "
            + "WHERE username = ? AND level < ?";

    /** Returns up to `limit` players, best first, with ranks 1..n filled in. */
    public List<LeaderboardEntry> getTopPlayers(int limit) throws SQLException {
        List<LeaderboardEntry> entries = new ArrayList<>();

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(TOP_PLAYERS_SQL)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                int rank = 1; // sequential; tied players still get distinct ranks
                while (rs.next()) {
                    entries.add(new LeaderboardEntry(
                            rank++,
                            rs.getString("username"),
                            rs.getInt("level")));
                }
            }
        }
        return entries;
    }

    /** Not called anywhere yet — for when a run ends and a new best should be saved. */
    public void updateHighestLevel(String username, int level) throws SQLException {
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_HIGHEST_LEVEL_SQL)) {

            stmt.setInt(1, level);
            stmt.setString(2, username);
            stmt.setInt(3, level);
            stmt.executeUpdate();
        }
    }
}