package pt.ipbeja.sportsmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Bitmap Utils Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-07
 */
public class BitmapUtils {

    /**
     * Converts a Bitmap to a byte array
     *
     * @param bitmap The source Bitmap
     * @return A byte array representing the bitmap
     */
    public static byte[] toBytes(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }

    /**
     * Creates a Bitmap from a byte array
     *
     * @param bytes The source byte array
     * @return The Bitmap
     */
    public static Bitmap toBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
