package com.bnpparibas.grp.ledletter.fontcreator;

import java.awt.Rectangle;

/**
 * @author morinb.
 */
public class Cell {
   protected int x, y, width, height, row, col;
   protected boolean selected;

   public Cell() {
   }

   public Cell(int x, int y, int width, int height, int row, int col, boolean selected) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.row = row;
      this.col = col;
      this.selected = selected;
   }

   public boolean contains(int x, int y) {
      //System.out.println(String.format("Is (%d;%d) in [(%d, %d);(%d, %d)]", x, y, this.x, this.x+this.width, this.y, this.y+this.height));
      return getRectangle().contains(x, y);
   }

   public Rectangle getRectangle() {
      return new Rectangle(this.x, this.y, width, height);
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getWidth() {
      return width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int getRow() {
      return row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getCol() {
      return col;
   }

   public void setCol(int col) {
      this.col = col;
   }

   public boolean isSelected() {
      return selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   @Override
   public String toString() {
      return "Cell{" +
            "x=" + x +
            ", y=" + y +
            ", width=" + width +
            ", height=" + height +
            ", row=" + row +
            ", col=" + col +
            ", selected=" + selected +
            '}';
   }
}
