package quickutils.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.view.Display;


@SuppressLint("NewApi") public class ImageUtils {

	ImageUtils() {}

	/**
	 * Call {@link BitmapFactory#decodeFile(String, android.graphics.BitmapFactory.Options)}, retrying up to 4 times with an
	 * increased {@link android.graphics.BitmapFactory.Options#inSampleSize} if an {@link OutOfMemoryError} occurs.<br/>
	 * If after trying 4 times the file still could not be decoded, {@code null} is returned.
	 *
	 * @param imageFile
	 *            The file to be decoded.
	 * @param options
	 *            The Options object passed to {@link BitmapFactory#decodeFile(String, android.graphics.BitmapFactory.Options)}
	 *            (can be {@code null}).
	 * @return The decoded bitmap, or {@code null} if it could not be decoded.
	 */
	public static Bitmap tryDecodeFile(File imageFile, BitmapFactory.Options options) {
		QUFactory.QLog.debug("tryDecodeFile imageFile=" + imageFile);
		int trials = 0;
		while (trials < 4) {
			try {
				Bitmap res = BitmapFactory.decodeFile(imageFile.getPath(), options);
				if (res == null) {
					QUFactory.QLog.debug("tryDecodeFile res=null");
				} else {
					QUFactory.QLog.debug("tryDecodeFile res width=" + res.getWidth() + " height=" + res.getHeight());
				}
				return res;
			}
			catch (OutOfMemoryError e) {
				if (options == null) {
					options = new BitmapFactory.Options();
					options.inSampleSize = 1;
				}
				QUFactory.QLog.debug("tryDecodeFile Could not decode file with inSampleSize=" + options.inSampleSize + ", try with inSampleSize=" + (options.inSampleSize + 1), e);
				options.inSampleSize++;
				trials++;
			}
		}
		QUFactory.QLog.debug("tryDecodeFile Could not decode the file after " + trials + " trials, returning null");
		return null;
	}

	/**
	 * Returns an immutable version of the given bitmap.<br/>
	 * The given bitmap is recycled. A temporary file is used (using {@link File#createTempFile(String, String)}) to avoid
	 * allocating twice the needed memory.
	 */
	public static Bitmap asImmutable(Bitmap bitmap) throws IOException {
		// This is the file going to use temporally to dump the bitmap bytes
		File tmpFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), null);
		QUFactory.QLog.debug("getImmutable tmpFile=" + tmpFile);
		// Open it as an RandomAccessFile
		RandomAccessFile randomAccessFile = new RandomAccessFile(tmpFile, "rw");
		// Get the width and height of the source bitmap
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// Dump the bytes to the file.
		// This assumes the source bitmap is loaded using options.inPreferredConfig = Config.ARGB_8888 (hence the value of 4 bytes
		// per pixel)
		FileChannel channel = randomAccessFile.getChannel();
		MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, width * height * 4);
		bitmap.copyPixelsToBuffer(buffer);
		// Recycle the source bitmap, this will be no longer used
		bitmap.recycle();
		// Create a new mutable bitmap to load the bitmap from the file
		bitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
		// Load it back from the temporary buffer
		buffer.position(0);
		bitmap.copyPixelsFromBuffer(buffer);
		// Cleanup
		channel.close();
		randomAccessFile.close();
		tmpFile.delete();
		return bitmap;
	}

	/**
	 * List of EXIF tags used by {@link #copyExifTags(File, File)}.
	 */
	// @formatter:off
	@SuppressLint("InlinedApi") private static final String[]	EXIF_TAGS	= new String[] { ExifInterface.TAG_APERTURE, ExifInterface.TAG_DATETIME, ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_FLASH, ExifInterface.TAG_FOCAL_LENGTH, ExifInterface.TAG_GPS_ALTITUDE, ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_DATESTAMP, ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_GPS_PROCESSING_METHOD, ExifInterface.TAG_GPS_TIMESTAMP, ExifInterface.TAG_ISO, ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL, ExifInterface.TAG_WHITE_BALANCE, };

	// @formatter:on
	/**
	 * Copy the EXIF tags from the source image file to the destination image file.
	 *
	 * @param sourceFile
	 *            The existing source JPEG file.
	 * @param destFile
	 *            The existing destination JPEG file.
	 * @throws IOException
	 *             If EXIF information could not be read or written.
	 */
	public static void copyExifTags(File sourceFile, File destFile) throws IOException {
		QUFactory.QLog.debug("copyExifTags sourceFile=" + sourceFile + " destFile=" + destFile);
		ExifInterface sourceExifInterface = new ExifInterface(sourceFile.getPath());
		ExifInterface destExifInterface = new ExifInterface(destFile.getPath());
		boolean atLeastOne = false;
		for (String exifTag : EXIF_TAGS) {
			String value = sourceExifInterface.getAttribute(exifTag);
			if (value != null) {
				atLeastOne = true;
				destExifInterface.setAttribute(exifTag, value);
			}
		}
		if (atLeastOne) destExifInterface.saveAttributes();
	}

	/**
	 * Retrieves the dimensions of the bitmap in the given file.
	 *
	 * @param bitmapFile
	 *            The file containing the bitmap to measure.
	 * @return A {@code Point} containing the width in {@code x} and the height in {@code y}.
	 */
	public static Point getDimensions(File bitmapFile) {
		QUFactory.QLog.debug("getDimensions bitmapFile=" + bitmapFile);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bitmapFile.getPath(), options);
		int width = options.outWidth;
		int height = options.outHeight;
		Point res = new Point(width, height);
		QUFactory.QLog.debug("getDimensions res=" + res);
		return res;
	}

	/**
	 * Retrieves the rotation in the EXIF tags of the given file.
	 *
	 * @param bitmapFile
	 *            The file from which to retrieve the info.
	 * @return The rotation in degrees, or {@code 0} if there was no EXIF tags in the given file, or it could not be read.
	 */
	public static int getExifRotation(File bitmapFile) {
		QUFactory.QLog.debug("getExifRotation bitmapFile=" + bitmapFile);
		ExifInterface exifInterface;
		try {
			exifInterface = new ExifInterface(bitmapFile.getPath());
		}
		catch (IOException e) {
			QUFactory.QLog.debug("getExifRotation Could not read exif info: returning 0", e);
			return 0;
		}
		int exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
		QUFactory.QLog.debug("getExifRotation orientation=" + exifOrientation);
		int res = 0;
		switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				res = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				res = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				res = 270;
				break;
		}
		QUFactory.QLog.debug("getExifRotation res=" + res);
		return res;
	}

	/**
	 * Creates a small version of the bitmap inside the given file, using the given max dimensions.<br/>
	 * The resulting bitmap's dimensions will always be smaller than the given max dimensions.<br/>
	 * The rotation EXIF tag of the given file, if present, is used to return a thumbnail that won't be rotated.
	 *
	 * @param bitmapFile
	 *            The file containing the bitmap to create a thumbnail from.
	 * @param maxWidth
	 *            The wanted maximum width of the resulting thumbnail.
	 * @param maxHeight
	 *            The wanted maximum height of the resulting thumbnail.
	 * @return A small version of the bitmap, or (@code null} if the given bitmap could not be decoded.
	 */
	public static Bitmap createThumbnail(File bitmapFile, int maxWidth, int maxHeight) {
		QUFactory.QLog.debug("createThumbnail imageFile=" + bitmapFile + " maxWidth=" + maxWidth + " maxHeight=" + maxHeight);
		// Get exif rotation
		int rotation = getExifRotation(bitmapFile);
		// Determine optimal inSampleSize
		Point originalDimensions = getDimensions(bitmapFile);
		int width = originalDimensions.x;
		int height = originalDimensions.y;
		int inSampleSize = 1;
		if (rotation == 90 || rotation == 270) {
			// In these 2 cases we invert the measured dimensions because the bitmap is rotated
			width = originalDimensions.y;
			height = originalDimensions.x;
		}
		int widthRatio = width / maxWidth;
		int heightRatio = height / maxHeight;
		// Take the max, because we don't care if one of the returned thumbnail's side is smaller
		// than the specified maxWidth/maxHeight.
		inSampleSize = Math.max(widthRatio, heightRatio);
		QUFactory.QLog.debug("createThumbnail using inSampleSize=" + inSampleSize);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		Bitmap res = tryDecodeFile(bitmapFile, options);
		if (res == null) {
			QUFactory.QLog.debug("createThumbnail Could not decode file, returning null");
			return null;
		}
		// Rotate if necessary
		if (rotation != 0) {
			QUFactory.QLog.debug("createThumbnail rotating thumbnail");
			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			Bitmap rotatedBitmap = null;
			try {
				rotatedBitmap = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, false);
				res.recycle();
				res = rotatedBitmap;
			}
			catch (OutOfMemoryError exception) {
				QUFactory.QLog.debug("createThumbnail Could not rotate bitmap, keeping original orientation", exception);
			}
		}
		QUFactory.QLog.debug("createThumbnail res width=" + res.getWidth() + " height=" + res.getHeight());
		return res;
	}

	/**
	 * Convert drawable resource to bitmap
	 * 
	 * @param context
	 *            Application context
	 * @param drawable
	 *            drawable resource to be converted
	 * @return a bitmap
	 */
	public static Bitmap convertImageResourceToBitmap(Context context, int drawable) {
		return BitmapFactory.decodeResource(context.getResources(), drawable);
	}

	/**
	 * Convert drawable to bitmap
	 * 
	 * @param drawable
	 *            drawable to be converted
	 * @return a bitmap
	 */
	public static Bitmap convertDrawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		int width = drawable.getIntrinsicWidth();
		width = width > 0 ? width : 1;
		int height = drawable.getIntrinsicHeight();
		height = height > 0 ? height : 1;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * @param bitmap
	 * @param filename
	 * @param path
	 * @param recycle
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static File saveBitmapDownscaled(Bitmap bitmap, String filename, String path, boolean recycle, int maxWidth, int maxHeight) {
		float heightScaleFactor = 1;
		float widthScaleFactor = 1;
		float scaleFactor = 1;

		if (bitmap.getHeight() > maxHeight) {
			heightScaleFactor = (float) maxHeight / (float) bitmap.getHeight();
		}

		if (bitmap.getWidth() > maxWidth) {
			widthScaleFactor = (float) maxWidth / (float) bitmap.getWidth();
		}
		if (heightScaleFactor < 1 || widthScaleFactor < 1) {
			scaleFactor = MathUtils.min(heightScaleFactor, widthScaleFactor);
		}
		bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scaleFactor), (int) (bitmap.getHeight() * scaleFactor), true);
		return saveBitmap(bitmap, filename, path, recycle);
	}

	/**
	 * @param bitmap
	 * @param filename
	 * @param path
	 * @param recycle
	 * @return
	 */
	public static File saveBitmap(Bitmap bitmap, String filename, String path, boolean recycle) {
		FileOutputStream out = null;
		try {
			File f = new File(path, filename);
			if (!f.exists()) {
				f.createNewFile();
			}
			out = new FileOutputStream(f);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
				return f;
			}
		}
		catch (Exception e) {
			QUFactory.QLog.error("Could not save bitmap", e);
		} finally {
			try {
				out.close();
			}
			catch (Throwable ignore) {}
			if (recycle) {
				bitmap.recycle();
			}
		}
		return null;
	}

	/**
	 * @param src
	 * @return
	 */
	public static Bitmap flip(Bitmap src) {
		Matrix m = new Matrix();
		m.preScale(-1, 1);
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
	}

	/**
	 * @param bmp
	 *            input bitmap
	 * @param contrast
	 *            0..10 1 is default
	 * @param brightness
	 *            -255..255 0 is default
	 * @return new bitmap
	 */
	public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
		ColorMatrix cm = new ColorMatrix(new float[] { contrast, 0, 0, 0, brightness, 0, contrast, 0, 0, brightness, 0, 0, contrast, 0, brightness, 0, 0, 0, 1, 0 });

		Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

		Canvas canvas = new Canvas(ret);

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bmp, 0, 0, paint);

		return ret;
	}

	/**
	 * Stores an image on the storage
	 * 
	 * @param image
	 *            the image to store.
	 * @param pictureFile
	 *            the file in which it must be stored
	 */
	public static void storeImage(Bitmap image, File pictureFile) {
		if (pictureFile == null) {
			QUFactory.QLog.debug("Error creating media file, check storage permissions: ");
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		}
		catch (FileNotFoundException e) {
			QUFactory.QLog.debug("File not found: " + e.getMessage());
		}
		catch (IOException e) {
			QUFactory.QLog.debug("Error accessing file: " + e.getMessage());
		}
	}

	/**
	 * Get the screen height.
	 * 
	 * @param context
	 * @return the screen height
	 */
	@SuppressWarnings("deprecation") @SuppressLint("NewApi") public static int getScreenHeight(Activity context) {

		Display display = context.getWindowManager().getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			return size.y;
		}
		return display.getHeight();
	}

	/**
	 * Get the screen width.
	 * 
	 * @param context
	 * @return the screen width
	 */
	@SuppressWarnings("deprecation") @SuppressLint("NewApi") public static int getScreenWidth(Activity context) {

		Display display = context.getWindowManager().getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			return size.x;
		}
		return display.getWidth();
	}
}