package caiohenrique.auxiliarphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import caiohenrique.auxphoto.AuxiliarPhoto;
import caiohenrique.auxphoto.DownPhotos;

public class ActivityMostImages extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<Bitmap> list = null;
    Adapter adapter;
    DownPhotos downPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_images);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new Adapter(list,this);

        mRecyclerView.setAdapter(adapter);

        comecaDownMostImages();

    }

    private void comecaDownMostImages() {

        final List<String> listUrls = new ArrayList<>();
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");
        listUrls.add("https://www.anda.jor.br/wp-content/uploads/2013/12/Vida-Selvagem.jpg");
        listUrls.add("http://res.cloudinary.com/db79cecgq/image/upload/c_crop,h_811,w_1437,x_0,y_44/c_fill/v1407165051/a-vida-selvagem-na-africa-02.jpg");
        listUrls.add("http://cdn.videosvirais.com.br/production/medias/2015/9/animal-25-fotos-da-vida-selvagem-que-nem-parecem-reais-a-16-e-a-minha-favorita-e0abf81e.jpg");

        final AuxiliarPhoto auxPhoto = new AuxiliarPhoto(this);
        downPhotos = new DownPhotos(listUrls, new DownPhotos.OnDownloadedCompleted() {
            @Override
            public void onDownloadedCompleted(String path, Bitmap bitmap) {
                /*********
                ** When downloaded image and be in thread main
                 * he download photo by photo, if error in downloaded photo he try again
                 */
                adapter.addListItem(bitmap);
                bitmap = null;
            }
            @Override
            public Bitmap onDownloadedCompletedInBackground(String s, Bitmap bitmap) {
                // When downloaded image and be in other thread
                return auxPhoto.modifySizeOfBitmap(bitmap,150);
            }
        });
    }


    public static class Adapter extends RecyclerView.Adapter {

        List<Bitmap> urls;
        Context context;
        public Adapter(List<Bitmap> urls, Context context) {
            this.urls = urls;
            this.context = context;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_images,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            /*******************
            ** Just a example
             * delete of memory the bitmap after setImageBitmap with " bitmap = null; "
             * Preference save imagem in phone
             */
            ((MyViewHolder) holder).getImage().setImageBitmap(urls.get(position));

        }

        @Override
        public int getItemCount() {
            return urls.size();
        }


        public void addListItem(Bitmap c) {
            urls.add(c);
            notifyItemInserted(urls.size() - 1);
        }

    }


    @Override
    protected void onResume() {
        downPhotos.reestart();
        super.onResume();
    }

    @Override
    protected void onStop() {
        downPhotos.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        /***
        ** Need for stop process
         */
        downPhotos.finishDownloads();
        super.onDestroy();
    }
}
