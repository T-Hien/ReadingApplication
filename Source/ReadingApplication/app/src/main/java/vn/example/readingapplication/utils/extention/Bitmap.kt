package vn.example.readingapplication.utils.extention

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

fun Bitmap?.convertBitmapToBase64Quality(quality: Int): String {
    if (this == null) {
        return ""
    }
    try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

fun Bitmap?.convertBitmapToBase64Full(): String {
    if (this == null) {
        return ""
    }

//    val byteArrayOutputStream = ByteArrayOutputStream()
//    compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//    val byteArray = byteArrayOutputStream.toByteArray()
//    return Base64.encodeToString(byteArray, Base64.DEFAULT)

    try {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

fun Bitmap?.flipBitmapHorizontally(): Bitmap? {
    if (this == null) {
        return null
    }
    try {
        val matrix = Matrix().apply { preScale(-1f, 1f) }
        return Bitmap.createBitmap(
            this, 0, 0, this.width, this.height, matrix, true
        )
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Bitmap?.rotateBitmapIfRequired(selectedImage: Uri?): Bitmap? {
    if (this == null || selectedImage == null || TextUtils.isEmpty(selectedImage.path)) {
        return null
    }

    val exif = ExifInterface(selectedImage.path!!)
    val orientation: Int =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(270)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun Bitmap?.rotateBitmap(degree: Int): Bitmap? {
    if (this == null) {
        return null
    }

    try {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(
            this, 0, 0, this.width, this.height, matrix, true
        )
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Bitmap?.compressBySampleSize(
    maxWidth: Int,
    maxHeight: Int,
    recycle: Boolean
): Bitmap? {
    if (this == null) {
        return null
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    val byteArray = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
    val bytes = byteArray.toByteArray()
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    options.inSampleSize =
        calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    if (recycle && !this.isRecycled) {
        this.recycle()
    }
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun Bitmap?.compressAndResize(maxWidth: Int, maxHeight: Int, recycle: Boolean = false): Bitmap? {
    if (this == null) {
        return null
    }

    val width = this.width
    val height = this.height

    val aspectRatio = width.toFloat() / height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (width > height) {
        newWidth = maxWidth
        newHeight = (maxWidth / aspectRatio).toInt()
    } else {
        newHeight = maxHeight
        newWidth = (maxHeight * aspectRatio).toInt()
    }

    val resizedBitmap = Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    if (recycle && !this.isRecycled) {
        this.recycle()
    }

    return resizedBitmap
}

fun Bitmap?.compressToMaxSize(maxByteSize: Int = 1_000_000, recycle: Boolean = false): Bitmap? {
    if (this == null) {
        return null
    }

    val byteArrayOutputStream = ByteArrayOutputStream()
    var quality = 100
    this.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
    var byteArray = byteArrayOutputStream.toByteArray()

    if (byteArray.size <= maxByteSize) {
        return this
    }

    var scaledBitmap: Bitmap = this

    // Step 1: Scale down the bitmap if necessary
    while (byteArray.size > maxByteSize) {
        val width = (scaledBitmap.width * 0.9).toInt()
        val height = (scaledBitmap.height * 0.9).toInt()

        scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, width, height, true)
        byteArrayOutputStream.reset()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        byteArray = byteArrayOutputStream.toByteArray()
    }

    // Step 2: Compress the scaled bitmap to meet the size requirement
    while (byteArray.size > maxByteSize && quality > 10) {
        byteArrayOutputStream.reset()
        quality -= 5
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        byteArray = byteArrayOutputStream.toByteArray()
    }

    if (recycle && !this.isRecycled) {
        this.recycle()
    }

    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun Bitmap?.getBitmapSizeInBytes(): Int {
    if (this == null) {
        return 0
    }
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray().size
}

fun logBitmapSize(bitmap: Bitmap?, tag: String = "BitmapSize") {
    if (bitmap == null) {
        return
    }
    val sizeInBytes = bitmap.getBitmapSizeInBytes()
    val sizeInKB = sizeInBytes / 1024
    val sizeInMB = sizeInKB / 1024.0
    Log.d(tag, "Bitmap size: $sizeInBytes bytes ($sizeInKB KB, $sizeInMB MB)")
}
