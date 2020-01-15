package com.dev.serverappos;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.dev.serverappos.base.BaseActivity;

import com.dev.serverappos.fragment.Fragment_Cart;
import com.dev.serverappos.fragment.Fragment_Home;
import com.dev.serverappos.fragment.Fragment_Menu;
import com.dev.serverappos.fragment.Fragment_Notification;
import com.dev.serverappos.notification.ConnectionReceiver;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.OnTabSelectListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class HomeActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private DatabaseReference mDatabase;
    private String id = "";
    private FloatingSearchView floatingSearchView;
    List<String> strings = new ArrayList<>();
    FirebaseUser user;
    private ListView listView;
//    APIService apiService;
//   FirebaseUser fuser;
//    boolean notify = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        Bungee.zoom(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        strings.clear();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.dev.serverappos",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        mapped();
        onclickView();
       // checkInternetConnection();
//
    }
//

    //ánh xạ view
    private void mapped() {

        bottomBar = findViewById(R.id.bottomBar);
       nearby = bottomBar.getTabWithId(R.id.tab_cart);
        nearby1 = bottomBar.getTabWithId(R.id.tab_home);
        nearby2 = bottomBar.getTabWithId(R.id.tab_menu);
       nearby3 = bottomBar.getTabWithId(R.id.tab_notification);

    }

    //các sự kiện click
    private void checkInternetConnection() {

        boolean ret = ConnectionReceiver.isConnected();

        if (ret != true) {
            showNetworkSettingsAlert();
        }
    }

    public void showNetworkSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Internet Error!!!");
        alertDialog.setMessage("Turn on the internet?");
        alertDialog.setPositiveButton("Go setting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        HomeActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private void onclickView() {


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                fragment(tabId);
            }
        });
    }

    //bottom cilck chuyển đổi giữa các fragment

    public void fragment(int id) {

        switch (id) {
            case R.id.tab_home:


                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Cart()).commit();
                break;
            case R.id.tab_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Home()).commit();

                break;
            case R.id.tab_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Menu()).commit();

                break;
            case R.id.tab_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Notification()).commit();
                break;



        }
    }

    @Override
    public void onBackPressed() {
        HomeActivity.this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


}
