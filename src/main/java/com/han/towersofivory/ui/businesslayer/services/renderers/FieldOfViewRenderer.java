package com.han.towersofivory.ui.businesslayer.services.renderers;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

/**
 * Service class for the ASCII world
 * A class that provides the ASCII representation of the world
 */
public class FieldOfViewRenderer implements IWorldRenderer {
 // private static final int DEFAULT_RENDER_RANGE = 12; // Default render range if config read fails

  private final int horizontalRange;
  private final int verticalRange;

  public FieldOfViewRenderer(int horizontalRange, int verticalRange) {
    this.horizontalRange = horizontalRange;
    this.verticalRange = verticalRange;
  }

  /**
   * Renders the world.
   * The world is rendered with the player in the center and a set range of tiles around the player.
   * Uses {@link ASCIIInterfaceCharacters} to render the different elements of the world.
   *
   * @return The ASCII representation of the world.
   */
  public String render(World world) {
    int z = world.getMyPlayer().getPosition().getZ();

    StringBuilder sb = new StringBuilder();
    Floor floor = world.getFloors().get(z);

    Position playerPos = world.getMyPlayer().getPosition();
    int minX = playerPos.getX() - horizontalRange;
    int maxX = playerPos.getX() + horizontalRange;
    int minY = playerPos.getY() - verticalRange;
    int maxY = playerPos.getY() + verticalRange;

    char[][] renderedFloor = initializeRenderedFloor();

    boolean[][] visible = calculateFOV(floor, playerPos, horizontalRange, verticalRange);

    fillRenderedFloor(renderedFloor, floor, visible, minX, maxX, minY, maxY);

    renderPlayers(world, z, renderedFloor, minX, minY, visible);

    convertRenderedFloorToString(sb, renderedFloor);

    return sb.toString();
  }

  /**
   * Initializes the 2D char array that will be used to render the floor
   * @return The initialized 2D char array
   */
  private char[][] initializeRenderedFloor() {
    char[][] renderedFloor = new char[verticalRange * 2][horizontalRange * 2];
    for (int y = 0; y < verticalRange * 2; y++) {
      for (int x = 0; x < horizontalRange * 2; x++) {
        renderedFloor[y][x] = ASCIIInterfaceCharacters.SPACE.getCharacter();
      }
    }
    return renderedFloor;
  }

  /**
   * Fills the 2D char array with the ASCII characters of the floor
   * @param renderedFloor The 2D char array to fill
   * @param floor The floor to render
   * @param visible The visibility map
   * @param minX The minimum x coordinate of the area around the player
   * @param maxX The maximum x coordinate of the area around the player
   * @param minY The minimum y coordinate of the area around the player
   * @param maxY  The maximum y coordinate of the area around the player
   */
  private void fillRenderedFloor(char[][] renderedFloor, Floor floor, boolean[][] visible, int minX, int maxX, int minY, int maxY) {
    for (int y = minY; y < maxY; y++) {
      for (int x = minX; x < maxX; x++) {
        if (x >= 0 && x < floor.getWidth() && y >= 0 && y < floor.getHeight()) {
          if (visible[x][y]) {
            renderedFloor[y - minY][x - minX] = floor.getAsciiCharacterOfTile(new Tile(x, y));
          } else {
            renderedFloor[y - minY][x - minX] = ASCIIInterfaceCharacters.SPACE.getCharacter();
          }
        }
      }
    }
  }

  /**
   * Converts the 2D char array to a string
   * @param sb The StringBuilder to append the string to
   * @param renderedFloor The 2D char array to convert
   */
  private void convertRenderedFloorToString(StringBuilder sb, char[][] renderedFloor) {
    for (char[] row : renderedFloor) {
      sb.append(row).append("\n");
    }
  }


  /**
   * Calculates the field of view using a modified Bresenham's Line Algorithm
   *
   * @param floor The floor being rendered
   * @param playerPos The player's position
   * @param horizontalRange The horizontal range of the rendering
   * @param verticalRange The horizontal range of the rendering
   * @return A 2D boolean array indicating visible tiles
   */
  private boolean[][] calculateFOV(Floor floor, Position playerPos, int horizontalRange, int verticalRange) {
    int width = floor.getWidth();
    int height = floor.getHeight();
    boolean[][] visible = new boolean[width][height];

    for (int y = -verticalRange; y <= verticalRange; y++) {
      for (int x = -horizontalRange; x <= horizontalRange; x++) {
        int dx = playerPos.getX() + x;
        int dy = playerPos.getY() + y;
        if (dx >= 0 && dx < width && dy >= 0 && dy < height && isLineOfSightClear(floor, playerPos, new Position(dx, dy, playerPos.getZ()), visible)) {
          visible[dx][dy] = true;
        }
      }
    }
    return visible;
  }

  /**
   * Determines if the line of sight is clear between two points, marking visibility along the way
   *
   * @param floor The floor being rendered
   * @param start The starting position
   * @param end The ending position
   * @param visible The visibility map
   * @return True if the line of sight is clear, false otherwise
   */
  private boolean isLineOfSightClear(Floor floor, Position start, Position end, boolean[][] visible) {
    int x1 = start.getX();
    int y1 = start.getY();
    int x2 = end.getX();
    int y2 = end.getY();

    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);
    int sx = x1 < x2 ? 1 : -1;
    int sy = y1 < y2 ? 1 : -1;
    int err = dx - dy;

    while (true) {
      visible[x1][y1] = true;
      if (!floor.isWalkable(x1, y1)) {
        return false;
      }
      if (x1 == x2 && y1 == y2) {
        break;
      }
      int e2 = 2 * err;
      if (e2 > -dy) {
        err -= dy;
        x1 += sx;
      }
      if (e2 < dx) {
        err += dx;
        y1 += sy;
      }
    }
    return true;
  }

  /**
   * Renders the players on the floor.
   * Uses the {@link ASCIIInterfaceCharacters} to render the player characters.
   *
   * @param z The z coordinate of the floor
   * @param renderedFloor The floor to render the players on
   * @param minX The minimum x coordinate of the area around the player
   * @param minY The minimum y coordinate of the area around the player
   * @param visible The visibility map
   */
  private void renderPlayers(World world, int z, char[][] renderedFloor, int minX, int minY, boolean[][] visible) {
    for (Player player : world.getOtherPlayers()) {
      Position position = player.getPosition();
      if (position.getZ() == z) {
        int x = position.getX() - minX;
        int y = position.getY() - minY;
        if (x >= 0 && x < renderedFloor[0].length && y >= 0 && y < renderedFloor.length && visible[position.getX()][position.getY()]) {
          renderedFloor[y][x] = ASCIIInterfaceCharacters.OTHERPLAYER.getCharacter();
        }
      }
    }

    // Render the current player on the floor
    Position myPosition = world.getMyPlayer().getPosition();
    if (myPosition.getZ() == z) {
      int x = myPosition.getX() - minX;
      int y = myPosition.getY() - minY;
      if (x >= 0 && x < renderedFloor[0].length && y >= 0 && y < renderedFloor.length && visible[myPosition.getX()][myPosition.getY()]) {
        renderedFloor[y][x] = ASCIIInterfaceCharacters.PLAYER.getCharacter();
      }
    }
  }

//  private int getRenderRangeFromProperties() {
//    Properties prop = new Properties();
//    try (InputStream input = new FileInputStream("src/main/resources/configuration.properties")) {
//      prop.load(input);
//      return Integer.parseInt(prop.getProperty("UI_RENDER_RANGE"));
//    } catch (IOException ex) {
//      LOGGER.error("Could not read config file", ex);
//      return DEFAULT_RENDER_RANGE;
//    }
//  }
}
