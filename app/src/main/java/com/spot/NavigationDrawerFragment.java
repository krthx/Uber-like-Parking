package com.spot;

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
import android.widget.TextView;
import android.widget.Toast;

import com.spot.helpers.RecyclerItemOnClickListener;

public class NavigationDrawerFragment extends Fragment {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter Adaptador;
    private RecyclerView.LayoutManager ManejadorLayout;
    private FragmentDrawerListener myDrawerListener;
    private View contenedor;

    private int Iconos[] = { R.drawable.credit_card, R.drawable.credit_card, R.drawable.history, R.drawable.question, R.drawable.logoff };
    private String Titulos[] = {"Maps", "Pagos", "Historial", "Ayuda", "Cerrar Sesión"};

    private int picture = R.mipmap.ic_launcher;

    public NavigationDrawerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mRecyclerView = (RecyclerView) vista.findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        ManejadorLayout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(ManejadorLayout);

        Adaptador = new NavigationDrawerAdapter(Titulos, Iconos, "", "", picture);

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
