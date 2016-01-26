package com.bnpparibas.grp.ledletter.fontcreator.utils;

/**
 * Adaptation of jfxtras MatrixPanel
 */
public class HexUtils {
   public static byte[] toBytes(String hexString) {
      return toBytes(hexString.split("\\s"));
   }

   public static byte[] toBytes(String[] bytes) {
      byte[] result = new byte[bytes.length];
      int i = 0;
      for (String bStr : bytes) {
         int n = 0;
         if (!(bStr.equals("0") || bStr.equals("00"))) {
            n = Byte.parseByte(bStr.substring(0, 1), 16);
            n = 16 * n + Byte.parseByte(bStr.substring(1), 16);
         }
         result[i++] = (byte) n;
      }
      return result;
   }
}
