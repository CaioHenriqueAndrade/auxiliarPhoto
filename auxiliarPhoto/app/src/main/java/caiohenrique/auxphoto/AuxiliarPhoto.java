package caiohenrique.auxphoto;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Caio on 20/10/2017.
 */

public class AuxiliarPhoto {

    private String PATH = "";
    private OnDownloadedListener onDownListener;

    /**
     * the listener of download of photos
     *
     */
    public interface OnDownloadedListener {

        /**
         * called when the download was completed
         *
         * @param bitmap
         * @param nameImage
         */
        void onDownloadCompleted(Bitmap bitmap, String nameImage);

        /**
         * called when the download was completed but
         * yet be in thread work
         * @param bt
         * @param nameImage
         * @return Bitmap - you can make something
         * with the bitmap and return
         */
        @WorkerThread
        Bitmap onDownloadCompletedInBackGround(Bitmap bt, String nameImage);
    }

    /**
     * set the listener of download
     * call after download the photo,
     * first time it call in Worker Thread
     * and after it call in Main Thread
     *
     * @param onDownListener
     */
    public void setOnDownloadedListener(OnDownloadedListener onDownListener) {
        this.onDownListener = onDownListener;
    }

    private Context context;

    /**
     * Create the instance
     *
     * @param context
     */
    public AuxiliarPhoto(Context context) {
        this.context = context;
        this.PATH = context.getCacheDir().getAbsolutePath();
        PATH = PATH.replace("cache", "app_");
    }

    /**
     * get the path of internal storage
     *
     * @return path
     */
    public String getPath() {
        return this.PATH;
    }

    /**
     * delete the file if exists
     *
     * @param dir the dir of file
     * @param nameImage - the name of file
     * @return if was deleted
     */
    public boolean deleteIfExists(String dir, String nameImage) {
        boolean retorno = false;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                f.delete();
                retorno = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            retorno = false;
        }
        return retorno;
    }

    /**
     * return if the file exists
     *
     * @param directory
     * @param nameImage
     * @return if exists
     */
    public boolean fileExists(String directory, String nameImage) {
        boolean ifExists = false;
        String PathLocal = PATH + directory;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                ifExists = true;
            } else ifExists = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifExists;
    }

    /**
     * save bitmap photo in internal storage
     *
     * @param dir
     * @param bitmapImage
     * @param nameImage
     * @param quality - 0
     * @return
     */
    public boolean saveToInternalStorage(String dir, Bitmap bitmapImage, String nameImage, int quality) {
        boolean retorno = false;
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory;
        directory = cw.getDir(dir, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File( directory, nameImage);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            if (quality < 1) quality = 100;
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            retorno = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cw = null;
        return retorno;
    }

    /**
     * get the context
     *
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * try load the imagem from storage based params
     *
     * @param dir
     * @param nameImage
     * @return bitmap if the photo was found
     */
    public Bitmap loadImageFromStorage(String dir, String nameImage) {
        Bitmap b = null;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            } else {
                //n existe este arquivo
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return b;
    }


    /**
     * create the instance of file based by params
     *
     * @param directory
     * @param nameImage
     * @return the file instance
     */
    public File getFile(String directory, String nameImage) {
        return new File(PATH + directory, nameImage);
    }

    /**
     * delete all files at directory
     * passed by param
     *
     * @param dir
     */
    public void deletAllFiles(String dir) {
        File f = new File(getPath() + dir);

        if(f.isDirectory()) {
            for (File file : f.listFiles()) {
                Log.i("fileDeleted","deletando file " + file.getName());
                file.delete();
            }
        }
    }

    /**
     * delete the file based by params
     * @param dir
     * @param nameImage
     */
    public void deleteFile(String dir, String nameImage) {
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get the bitmap based by params
     *
     * @param dir
     * @param namePhoto
     * @return the bitmap if found
     */
    public Bitmap returnBitmapFile(String dir, String namePhoto) {
        Bitmap b = null;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, namePhoto);
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return b;
    }

    /**
     * this function try to found the photo in
     * internal storage and if not to found it
     * will to do download
     *
     ** @return the photo bitmap
     */
    public Bitmap loadImageFromStorageOrDownload(String dir, String nameImage, String urlDownload, OnDownloadedListener onDown) {

        if (onDown != null) onDownListener = onDown;
        Bitmap b = null;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            } else {
                new DownloadImageTask(urlDownload, nameImage).execute();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }


    /**
     * modify the size of bitmap based by params
     *
     * @param bmpOriginal
     * @param newWidth
     * @return the new bitmap
     */
    public Bitmap modifySizeOfBitmap(
            /* O nome do método já diz tudo */     Bitmap bmpOriginal,
                                                   int newWidth, int newHeight) {


        if (bmpOriginal.getWidth() <= newWidth) return bmpOriginal;
        if (bmpOriginal.getHeight() <= newHeight) return bmpOriginal;

        Bitmap novoBmp = null;

        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        float densityFactor = context.getResources().getDisplayMetrics().density;
        float novoW = newWidth * densityFactor;
        float novoH = newHeight * densityFactor;


        //Escala percentual
        float scalaW = novoW / w;
        float ScalaH = novoH / h;

        Matrix matrix = new Matrix();
        //Denifindo a proporção
        matrix.postScale(scalaW, ScalaH);

        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);

        //img.setRotation;
        return novoBmp;
    }

    /**
     * modify the size of bitmap based by params
     *
     * @param bmpOriginal
     * @param newWidth
     * @return the new bitmap
     */
    public Bitmap modifySizeOfBitmap(
            /* O nome do método já diz tudo */     Bitmap bmpOriginal,
                                                   int newWidth) {

        if (bmpOriginal.getWidth() <= newWidth) return bmpOriginal;

        int newHeight = newWidth * 100 / bmpOriginal.getWidth();
        newHeight = newHeight * bmpOriginal.getHeight() / 100;

        Bitmap novoBmp = null;


        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        //float densityFactor = getContext().getResources().getDisplayMetrics().density;
        float novoW = newWidth;
        float novoH = newHeight;


        //Escala percentual
        float scalaW = novoW / w;
        float ScalaH = novoH / h;

        Matrix matrix = new Matrix();
        //Denifindo a proporção
        matrix.postScale(scalaW, ScalaH);

        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);

        return novoBmp;

    }

    /**
     * this method modify the scale of image
     * for um size better, in other words, a
     * size that don't use very memory
     *
     * @param f
     * @param scale
     * @return the new bitmap
     */
    public Bitmap modifyScaleOfFile(File f, int scale) {
        try {
            // found the better size of image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // the new size that you need
            final int REQUIRED_SIZE = scale;

            // found the better value of scale
            int scaleSystem = 1;
            while (o.outWidth / scaleSystem / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scaleSystem / 2 >= REQUIRED_SIZE) {
                scaleSystem *= 2;
            }

            // make the process
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scaleSystem;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * to rotate the image based by angle
     * 
     * @author Caio
     * @param bitmap
     * @param angle
     * @return
     */
    private static Bitmap rotationImage(Bitmap bitmap, int angle) {
        // Abre o bitmap a partir do caminho da foto

        // Prepara a operação de rotação com o ângulo escolhido
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // Cria um novo bitmap a partir do original já com a rotação aplicada
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }

    /**
     * the sample verification if the phone is connected
     * at internet. You need permission "state network" for
     * it work
     * 
     * @author Caio
     * 
     * @param context
     * @return if is connected
     */
    public static boolean isConnected(Context context) {
        boolean connected = false;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }


    /**
     * this class make the download of photo based by path
     *
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private String pathDown;
        private String nameImage;

        /**
         * constructor with the params nedded
         *
         * @param PathDownload - the url
         * @param nameImage
         */
        public DownloadImageTask(String PathDownload, String nameImage) {
            pathDown = PathDownload;
            this.nameImage = nameImage;

        }

        /**
         * make the download in word thead
         * @param params
         * @return the photo or null if not connected
         */
        @WorkerThread
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(pathDown).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (onDownListener != null)
                bitmap = onDownListener.onDownloadCompletedInBackGround(bitmap, nameImage);

            return bitmap;
        }

        /**
         * happen after execute in doinBackground
         * here's in main thread.
         *
         * @param bitmap
         */
        @Override
        protected void onPostExecute(@Nullable Bitmap bitmap) {
            if (onDownListener != null) {
                onDownListener.onDownloadCompleted(bitmap, nameImage);
            }
        }

    }



}
