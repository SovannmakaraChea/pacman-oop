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

    public Pathfinder(String[] tileMap, int tileSize) {
        this.tileMap = tileMap;
        this.tileSize = tileSize;
    }

    public Direction findDirection(
            int startX,
            int startY,
            int targetX,
            int targetY
    ) {
        return findDirection(startX, startY, targetX, targetY, null);
    }

    // Same as above, but the first step may not go in the "avoid"
    // direction. Ghosts pass their reverse direction here so they
    // never U-turn on the spot.
    public Direction findDirection(
            int startX,
            int startY,
            int targetX,
            int targetY,
            Direction avoid
    ) {

        int startCol = startX / tileSize;
        int startRow = startY / tileSize;

        int targetCol = targetX / tileSize;
        int targetRow = targetY / tileSize;

        if (!isWalkable(startRow, startCol)) {
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

        // Same order as Direction.values(): UP, DOWN, LEFT, RIGHT
        int[][] directions = {
                {0, -1},   // UP
                {0, 1},    // DOWN
                {-1, 0},   // LEFT
                {1, 0}     // RIGHT
        };

        while (!queue.isEmpty()) {

            Point current = queue.poll();

            for (int i = 0; i < directions.length; i++) {

                int[] move = directions[i];

                if (current.equals(start)
                        && Direction.values()[i] == avoid) {
                    continue;
                }

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

        // Target may be off the board, inside a wall or unreachable
        // (Pinky and Inky aim ahead of Pac-Man), so head for the
        // reachable tile closest to it instead.
        if (!visited.contains(target)) {
            target = closestTo(visited, target);
        }

        if (target.equals(start)) {
            return null;
        }

        Point current = target;

        while (parent.containsKey(current)
                && !parent.get(current).equals(start)) {
            current = parent.get(current);
        }

        return getDirection(start, current);
    }

    private boolean isInside(int row, int col) {

        return row >= 0
                && row < tileMap.length
                && col >= 0
                && col < tileMap[row].length();
    }

    private boolean isWalkable(int row, int col) {

        return isInside(row, col)
                && tileMap[row].charAt(col) != 'X';
    }

    private Point closestTo(Set<Point> tiles, Point target) {

        Point best = null;
        double bestDistance = Double.MAX_VALUE;

        for (Point tile : tiles) {

            double distance = tile.distanceSq(target);

            if (distance < bestDistance) {
                bestDistance = distance;
                best = tile;
            }
        }

        return best;
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