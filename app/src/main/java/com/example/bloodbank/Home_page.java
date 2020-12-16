package com.example.bloodbank;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth firebaseAuth;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    View hview;
    DatabaseReference database;
    int[] arr_images = {R.drawable.ap2,R.drawable.an2,R.drawable.bp2,R.drawable.bn2,R.drawable.op2,R.drawable.on2,R.drawable.abp2,R.drawable.abn2};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")) { //if gps is disabled
            showSettingAlert();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hview=navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        database = FirebaseDatabase.getInstance().getReference().child("Data").child("Users");
        navigationView.setNavigationItemSelectedListener(this);
        TextView textView=(TextView)hview.findViewById(R.id.TextUsername);
        final ImageView imageView = (ImageView)hview.findViewById(R.id.profile_pic);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dc : dataSnapshot.getChildren()) {
                    User current = dc.getValue(User.class);
                    String email = current.getEmail();
                    if (email.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        switch (current.getBlood()) {
                            case "A+":
                                imageView.setImageResource(R.drawable.ap2);
                                break;
                            case "A-":
                                imageView.setImageResource(R.drawable.an2);
                                break;
                            case "B+":
                                imageView.setImageResource(R.drawable.bp2);
                                break;
                            case "B-":
                                imageView.setImageResource(R.drawable.bn2);
                                break;
                            case "O+":
                                imageView.setImageResource(R.drawable.op2);
                                break;
                            case "0-":
                                imageView.setImageResource(R.drawable.on2);
                                break;
                            case "AB+":
                                imageView.setImageResource(R.drawable.abp2);
                                break;
                            case "AB-":
                                imageView.setImageResource(R.drawable.abn2);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        textView.setText(firebaseAuth.getCurrentUser().getEmail());
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        ft.replace(R.id.home_content,new UserList());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_page,menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.view_profile) {
            fragment = new FragmentProfile();
        } else if (id == R.id.chats) {
           fragment = new RecentChatList();
        } else if (id == R.id.become_donor) {
            fragment = new LocationFragment();
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {

        } else if(id==R.id.home_content){
            fragment = new UserList();
        }else if(id==R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            Intent ob=new Intent(this,MainActivity.class);
            startActivity(ob);
            finish();
        }else if (id ==R.id.search_blood){
            fragment = new SearchFragment();
        }
        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.home_content,fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS setting!");
        alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                System.exit(0);
            }
        });
        alertDialog.show();
    }

}
