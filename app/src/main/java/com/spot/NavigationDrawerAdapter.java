package com.spot;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class NavigationDrawerAdapter  extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private static final int THEADER =0;
    private static final int TITEM =1;

    private String Titulos[];
    private int Iconos[], profile;

    private String name, email;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int HolderId;
        TextView txtView, Name, Email;
        ImageView imageView, profile;

        public ViewHolder(View itemView, int tipo){
            super(itemView);
            if(tipo == TITEM){
                txtView = (TextView) itemView.findViewById(R.id.RowText);
                imageView = (ImageView) itemView.findViewById(R.id.RowIcon);
                HolderId=1;
            }else{
                Name = (TextView) itemView.findViewById(R.id.name);
                Email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                HolderId = 0;
            }
        }

    }

    public NavigationDrawerAdapter(String Titles[], int Icons[], String Name, String Email, int Profile){
        Titulos = Titles;
        Iconos = Icons;
        name = Name;
        email = Email;
        profile = Profile;
    }
    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_item_row, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        }else if(viewType == THEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_header, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder holder, int position) {
        if(holder.HolderId == 1){
            holder.txtView.setText(Titulos[position - 1]);
            holder.imageView.setImageResource(Iconos[position - 1]);
        }
        else{
            holder.profile.setImageResource(profile);
            holder.Name.setText(name);
            holder.Email.setText(email);


            if(SSOSessionActivity.userLogged != null && SSOSessionActivity.userLogged.getEmail() != null) {
                holder.Name.setText(SSOSessionActivity.userLogged.getDisplayName());
                holder.Email.setText(SSOSessionActivity.userLogged.getEmail());

                Picasso.get()
                        .load(SSOSessionActivity.userLogged.getPhotoUrl())
                        .resize(100, 100)
                        .centerCrop()
                        .into(holder.profile);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Titulos.length + 1;
    }

    private boolean isPosiotionHeader(int position){
        return position == 0;
    }

    @Override
    public int getItemViewType(int position){
        if(isPosiotionHeader(position)){
            return THEADER;
        }
        return TITEM;
    }
}
