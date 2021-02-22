/*
 * Copyright 2021 Hugo Silva @ IPBeja
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
