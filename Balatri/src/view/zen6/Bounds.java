package view.zen6;

public record Bounds(int x, int y, int width, int height) {
  public boolean contains(Point point) {
    return point.x() >= x && point.x() <= x + width &&
           point.y() >= y && point.y() <= y + height;
  }
}
