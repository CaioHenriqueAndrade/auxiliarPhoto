package caiohenrique.auxphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.WorkerThread;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 *
 * make the download of a lot of images
 * one by one
 *
 * Created by Caio on 20/10/2017.
 */
public class DownPhotos {
    /**
     * if is download the photos yet
     */
    private boolean activity = true;

    /**
     * save the list of urls
     */
    List<String> mList;

    /**
     * listener of download
     */
    OnDownloadedCompleted inter;

    /**
     * the time between downloads
     */

    private int timeBetweenDownImages = 215;

    /**
     * when true the system stop
     * the download
     * but not stop de process
     *
     * if you need stop de process
     * turn activity = false
     *
     */
    private boolean verifyPause = false;

    /**
     * the listener
     */
    private DownloadImageTaskImage downImagesTask;

    /**
     * initialize the class and start the downloads
     *
     * @param mlist
     * @param inter
     */
    public DownPhotos(List<String> mlist, OnDownloadedCompleted inter) {
        this.mList = mlist;
        this.inter = inter;
        startDown();
    }

    /**
     * return if is stopped
     *
     * @return if stopped
     */
    public boolean isStopped() {
        return verifyPause;
    }

    /**
     * stop de service
     */
    public void stop() {
        verifyPause = true;
    }

    /**
     * return the download when is stop
     */
    public void reestart() {
        verifyPause = false;
    }

    /**
     * return if is active
     *
     * @return if active
     */
    public boolean isActive() {
        return activity;
    }

    /**
     * finish the activity of downloads
     */
    public void finishDownloads() {
        activity = false;
    }

    /**
     * change the time between downloads
     *
     * @param newTime
     */
    public void setTimebeteweenDownImages(int newTime) {
        timeBetweenDownImages = newTime;
    }

    /**
     * start the download
     */
    private void startDown() {

        downImagesTask = new DownloadImageTaskImage(mList.get(count), inter);
        downImagesTask.execute();
    }

    /**
     * save what number of list stopped
     */
    int count = 0;

    /**
     * notify when need make more downloads
     *
     * @param moreDownloads
     */
    public void notifyAddMoreImages(List<String> moreDownloads) {
        if (activity) {
            for (String pt : moreDownloads) {
                mList.add(pt);
            }

        } else {
            activity = true;
            mList = moreDownloads;
            count = 0;
            startDown();
        }
    }

    /**
     * the listener of download
     */
    public interface OnDownloadedCompleted {
        /**
         * called when the download was completed
         *
         * @param path - path of download
         * @param bitmap - bitmap
         */
        void onDownloadedCompleted(String path, Bitmap bitmap);

        /**
         * called when the download is completed yet in work thread
         * the user can return the bitmap modified
         * @param path
         * @param bitmap
         */
        @WorkerThread
        Bitmap onDownloadedCompletedInBackground(String path, Bitmap bitmap);

    }

    /**
     * called always that complet one cycle of donwload
     * @param path
     * @param bitmap
     */
    private void onDownloadedCompleted(String path, Bitmap bitmap) {
        if (activity) {
            /*
             * Null faremos de novo, se não vamos para o próximo
             */
            if (bitmap != null) {
                count++;
                if (inter != null) {
                    inter.onDownloadedCompleted(path, bitmap);
                }
                if (count < getSizeOfList() ) {
                    startDown();
                } else {
                    activity = false;
                }
            } else {
                startDown();
            }
        }
    }

    /**
     * get the size of list
     *
     * @return size
     */
    public int getSizeOfList() {
        return mList.size();
    }


    /**
     * the class that to do photos download
     */
    private class DownloadImageTaskImage extends AsyncTask<String, Void, Bitmap> {

        /**
         * the listener
         */
        private OnDownloadedCompleted inter;
        /*
        path of download
         */
        private String path;

        /**
         * initialize the class
         *
         * @param path
         * @param inter
         */
        public DownloadImageTaskImage(String path, OnDownloadedCompleted inter) {
            this.inter = inter;
            this.path = path;
        }

        /**
         * make the download of bitmap
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            while (verifyPause) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!activity) break;
            }
            try {
                InputStream in = new URL(path).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(timeBetweenDownImages);
            } catch (Exception e) { }

            if (bitmap != null && inter != null)
                return inter.onDownloadedCompletedInBackground(path, bitmap);

            return bitmap;
        }

        /**
         * after try make the download
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            DownPhotos.this.onDownloadedCompleted(path, bitmap);
            bitmap = null;
        }
    }

}