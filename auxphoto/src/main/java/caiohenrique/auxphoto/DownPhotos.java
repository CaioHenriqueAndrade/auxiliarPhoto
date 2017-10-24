package caiohenrique.auxphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Caio on 20/10/2017.
 */

public class DownPhotos {
    private boolean atividade = true;
    List<String> mList;
    OnDownloadedCompleted inter;
    public String UrlBaseEvents;
    private int ListaTamanho = 0;

    private int timeEnterDownImages = 215;
    /********************************************
     * Quando true aguarda continuação do serviço
     */
    private boolean VERIFICAPAUSA = false;
    private DownloadImageTaskImage downImagesTask;

    public DownPhotos(List<String> mlist, OnDownloadedCompleted inter) {
        this.mList = mlist;
        this.ListaTamanho = mlist.size();
        this.inter = inter;
        comecaDown();
    }

    public boolean isStopped() {
        return VERIFICAPAUSA;
    }

    public void stop() {
        VERIFICAPAUSA = true;
    }

    public void reestart() {
        VERIFICAPAUSA = false;
    }

    public boolean verificaAtividade() {
        return atividade;
    }

    public void finishDownloads() {
        atividade = false;
    }

    public void setTimeEnterDownImages(int newTime) {
        timeEnterDownImages = newTime;
    }

    private void comecaDown() {

        downImagesTask = new DownloadImageTaskImage(mList.get(contagem), inter);
        downImagesTask.execute();
    }

    int contagem = 0;

    public void notifyAddMoreImages(List<String> moreDownloads) {
        if (atividade) {
            for (String pt : moreDownloads) {
                mList.add(pt);
            }
            ListaTamanho = mList.size();

        } else {
            atividade = true;
            mList = moreDownloads;
            ListaTamanho = mList.size();
            contagem = 0;
            comecaDown();
        }
    }

    public interface OnDownloadedCompleted {

        void onDownloadedCompleted(String path, Bitmap bitmap);
    }

    private void onDownloadedCompleted(String path, Bitmap bitmap) {
        if (atividade) {
            /*
            * Null faremos de novo, se não vamos para o próximo
             */
            if (bitmap != null) {
                contagem++;
                if (inter != null) {
                    inter.onDownloadedCompleted(path, bitmap);
                }
                if (contagem < ListaTamanho) {
                    comecaDown();
                } else {
                    atividade = false;
                }
            } else {
                comecaDown();
            }
        }
    }

    private class DownloadImageTaskImage extends AsyncTask<String, Void, Bitmap> {

        private OnDownloadedCompleted inter;
        private String path;

        public DownloadImageTaskImage(String path, OnDownloadedCompleted inter) {
            this.inter = inter;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            while (VERIFICAPAUSA) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!atividade) break;
            }
            try {
                InputStream in = new URL(path).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(timeEnterDownImages);


            } catch (Exception e) {
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            DownPhotos.this.onDownloadedCompleted(path, bitmap);
            bitmap = null;
        }
    }
}