package caiohenrique.auxiliarphoto;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Caio on 20/10/2017.
 */

class MyViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagem;
    public MyViewHolder(View v) {
        super(v);
        imagem = (ImageView) v.findViewById(R.id.imageview);
    }

    public ImageView getImage() {
        return imagem;
    }
}
