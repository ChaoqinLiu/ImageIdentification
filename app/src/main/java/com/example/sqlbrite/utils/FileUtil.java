package com.example.sqlbrite.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.safframework.tony.common.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    /**
     * 根据图片路径，将图片转换为byte数组
     * @param path 图片路径
     * @return byte[]
     */
    public static byte[] readFileByBytes(String path) {
        FileInputStream fi;
        byte[] bytes = null;
        try {
            fi = new FileInputStream(new File(path));
            bytes = new byte[fi.available()];

            //将文件内容写入字节数组
            fi.read(bytes);
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //将图片Uri转换为图片String的真实路径
    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File rootDataDir = context.getFilesDir();
            File copyFile = new File( rootDataDir + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

