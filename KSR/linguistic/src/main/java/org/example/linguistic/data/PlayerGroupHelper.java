package org.example.linguistic.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerGroupHelper {
    public static Map<String, List<PlayerStats>> groupPlayersByPosition(List<PlayerStats> players) {
        return players.stream().collect(Collectors.groupingBy(PlayerStats::getPosition));
    }
}
