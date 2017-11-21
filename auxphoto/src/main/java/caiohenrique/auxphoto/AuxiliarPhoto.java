package caiohenrique.auxphoto;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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

    public interface OnDownloadedListener {

        void onDownloadCompleted(Bitmap bitmap, String nameImage);

        Bitmap onDownloadCompletedInBackGround(Bitmap bt, String nameImage);
    }

    public void setOnDownloadedListener(OnDownloadedListener onDownListener) {
        this.onDownListener = onDownListener;
    }

    private Context context;

    public AuxiliarPhoto(Context context) {
        this.context = context;
        this.PATH = context.getCacheDir().getAbsolutePath();
        PATH = PATH.replace("cache", "app_");
    }


    /*****************************************************
     * ********* Baixa a imagem e salva ou apenas seta-a.
     *
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

    public boolean fileExists(String directory, String nameImage) {
        boolean retorno = false;
        String PathLocal = PATH + directory;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                retorno = true;
            } else retorno = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    /**************************************************************************************
     *  Baixa a imagem e repassa o bitmap pela Interface
     */
    public boolean saveToInternalStorage(String dir, Bitmap bitmapImage, String nameImage, int qualidade) {
        boolean retorno = false;
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory;
        directory = cw.getDir(dir, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, nameImage);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            if (qualidade < 1) qualidade = 100;
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, qualidade, fos);
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

    public Context getContext() {
        return context;
    }

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

    /*
    public Bitmap renameFile(String dir, String nameImage, String newName) {
        Bitmap b = null;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            File arq = new File(PathLocal, newName);
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
                f.renameTo(arq);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return b;
    }
*/

    public File getFile(String directory, String nameImage) {
        return new File(PATH + directory, nameImage);
    }

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

    public Bitmap returnBitmapFile(String dir, String nameImage) {
        Bitmap b = null;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return b;
    }

    public Bitmap loadImageFromStorageOrDownload(String dir, String nameImage, String UrlDownload, OnDownloadedListener onDown) {
        if (onDown != null) onDownListener = onDown;
        Bitmap b = null;
        String PathLocal = PATH + dir;
        try {
            File f = new File(PathLocal, nameImage);
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            } else {
                new DownloadImageTask(UrlDownload, dir, nameImage).execute();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }


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


    public Bitmap modifySizeOfBitmap(
            /* O nome do método já diz tudo */     Bitmap bmpOriginal,
            int newWidth) {

        if (bmpOriginal.getWidth() <= newWidth) return bmpOriginal;

        int newHeight = newWidth * 100 / bmpOriginal.getWidth();
        newHeight = newHeight * bmpOriginal.getHeight() / 100;

        Bitmap novoBmp = null;


        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        float densityFactor = getContext().getResources().getDisplayMetrics().density;
        float novoW = newWidth;
        float novoH = newHeight;


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


    // Decodifica a imagem e escala para a redução do consumo de memória
    //escala padrão
    public Bitmap modifyScaleOfFile(File f, int escala) {
        try {
            // Decodifica o tamanho da imagem
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // O novo tamanho que queremos
            final int REQUIRED_SIZE = escala;

            // Achar o valor correto para a escala
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decodifica com o inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    /*


    public Bitmap recriarBitmap(String imagePath) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
        /*Reduzindo a qualidade da imagem para preservar memoria.
        * Aqui você pode testar a redução que melhor atende sua necessidade

            options.inSampleSize = 2;

            return BitmapFactory.decodeStream(new FileInputStream(imagePath), null, options);
        } catch (FileNotFoundException e) {

        }
        return null;

    }
        */

    private static Bitmap rotationImage(Bitmap bitmap, int angulo) {
        // Abre o bitmap a partir do caminho da foto

        // Prepara a operação de rotação com o ângulo escolhido
        Matrix matrix = new Matrix();
        matrix.postRotate(angulo);

        // Cria um novo bitmap a partir do original já com a rotação aplicada
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private String pathDown;
        private String nameImage;
        private String dir;

        public DownloadImageTask(String PathDownload, String dir, String nameImage) {
            pathDown = PathDownload;
            this.nameImage = nameImage;
            this.dir = dir;

        }

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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (onDownListener != null) {
                onDownListener.onDownloadCompleted(bitmap, nameImage);
                bitmap = null;
            }
        }

    }


    public static boolean isConnected(Context context) {
        boolean conectado = false;
        try {
            ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conectivtyManager.getActiveNetworkInfo() != null
                    && conectivtyManager.getActiveNetworkInfo().isAvailable()
                    && conectivtyManager.getActiveNetworkInfo().isConnected()) {
                conectado = true;
            } else {
                conectado = false;
            }
        } catch (Exception e) {
            return false;
        }
        return conectado;
    }


}
