package ai;

import utils.Direction;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Pathfinder {

    private final String[] tileMap;
    private final int tileSize;
    private final int rowCount;
    private final int columnCount;

    public Pathfinder(String[] tileMap, int tileSize) {
        this.tileMap = tileMap;
        this.tileSize = tileSize;
        this.rowCount = tileMap.length;
        this.columnCount = tileMap[0].length();
    }

    public Direction findDirection(
            int startX,
            int startY,
            int targetX,
            int targetY
    ) {

        // Convert pixel positions to tile positions
        int startCol = startX / tileSize;
        int startRow = startY / tileSize;

        int targetCol = targetX / tileSize;
        int targetRow = targetY / tileSize;

        // Invalid positions
        if (!isWalkable(startRow, startCol)
                || !isWalkable(targetRow, targetCol)) {
            return null;
        }

        Point start = new Point(startCol, startRow);
        Point target = new Point(targetCol, targetRow);

        if (start.equals(target)) {
            return null;
        }

        Queue<Point> queue = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();
        HashMap<Point, Point> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);

        int[][] directions = {
                {0, -1},   // UP
                {0, 1},    // DOWN
                {-1, 0},   // LEFT
                {1, 0}     // RIGHT
        };

        while (!queue.isEmpty()) {

            Point current = queue.poll();

            if (current.equals(target)) {
                break;
            }

            for (int[] move : directions) {

                int nextCol = current.x + move[0];
                int nextRow = current.y + move[1];

                if (!isWalkable(nextRow, nextCol)) {
                    continue;
                }

                Point next = new Point(nextCol, nextRow);

                if (visited.contains(next)) {
                    continue;
                }

                visited.add(next);
                parent.put(next, current);
                queue.add(next);
            }
        }

        // No path found
        if (!parent.containsKey(target)) {
            return null;
        }

        // Walk backwards from target until we reach the start
        Point current = target;

        while (!parent.get(current).equals(start)) {
            current = parent.get(current);
        }

        return getDirection(start, current);
    }

    private boolean isWalkable(int row, int col) {

        if (row < 0 || row >= rowCount
                || col < 0 || col >= columnCount) {
            return false;
        }

        return tileMap[row].charAt(col) != 'X';
    }

    private Direction getDirection(Point start, Point next) {

        if (next.x > start.x) {
            return Direction.RIGHT;
        }

        if (next.x < start.x) {
            return Direction.LEFT;
        }

        if (next.y > start.y) {
            return Direction.DOWN;
        }

        if (next.y < start.y) {
            return Direction.UP;
        }

        return null;
    }
}