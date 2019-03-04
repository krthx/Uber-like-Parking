package com.parking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parking.helpers.RecyclerItemOnClickListener;

public class NavigationDrawerFragment extends Fragment {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter Adaptador;
    private RecyclerView.LayoutManager ManejadorLayout;
    private FragmentDrawerListener myDrawerListener;
    private View contenedor;
    //ManejadorSQLite manejador;

    private int Iconos[] = {R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera};
    private String Titulos[] = {"", "", "", ""};
    private String nombre;
    private String correo;
    private int picture = R.mipmap.ic_launcher;

    public NavigationDrawerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //manejador= new ManejadorSQLite(getActivity());

        /*nombre = manejador.getAutenticado().getNombre().toString();
        correo = manejador.getAutenticado().getCorreo().toString();*/

        mRecyclerView = (RecyclerView) vista.findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        ManejadorLayout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(ManejadorLayout);

        Adaptador = new NavigationDrawerAdapter(Titulos, Iconos, nombre, correo, picture);

        mRecyclerView.setAdapter(Adaptador);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemOnClickListener(getActivity(), new RecyclerItemOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myDrawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(contenedor);
            }
        }));

        return vista;
    }

    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar, int fragmentId) {
        this.mDrawerLayout = drawerLayout;
        this.mToolbar = toolbar;
        this.contenedor = getActivity().findViewById(fragmentId);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mToolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
    public void setDrawerListener(FragmentDrawerListener drawerListener){
        this.myDrawerListener =  drawerListener;
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
