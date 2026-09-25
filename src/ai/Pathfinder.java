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

        int startCol = startX / tileSize;
        int startRow = startY / tileSize;

        int targetCol = targetX / tileSize;
        int targetRow = targetY / tileSize;

        if (!isInside(startRow, startCol)
                || !isInside(targetRow, targetCol)) {
            return null;
        }

        if (!isWalkable(startRow, startCol)) {
            return null;
        }

        // If target is inside a wall, find the nearest walkable tile.
        Point target = findNearestWalkable(targetRow, targetCol);

        if (target == null) {
            return null;
        }

        Point start = new Point(startCol, startRow);

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

        if (!visited.contains(target)) {
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

    private Point findNearestWalkable(int row, int col) {

        if (isWalkable(row, col)) {
            return new Point(col, row);
        }

        for (int radius = 1; radius <= 4; radius++) {

            for (int r = row - radius; r <= row + radius; r++) {
                for (int c = col - radius; c <= col + radius; c++) {

                    if (isWalkable(r, c)) {
                        return new Point(c, r);
                    }
                }
            }
        }

        return null;
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