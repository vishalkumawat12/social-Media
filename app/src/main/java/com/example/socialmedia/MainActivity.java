package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.socialmedia.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        binding.toolbar.setVisibility(View.GONE);
        transaction.replace(R.id.content,new HomeFragment());
        transaction.commit();
        binding.bottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        binding.toolbar.setVisibility(View.GONE);

                        transaction1.replace(R.id.content, new HomeFragment());
                        break;
                    case 1:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction1.replace(R.id.content, new SearchFragment() );
                        break;
                    case 2:binding.toolbar.setVisibility(View.GONE);
                            transaction1.replace(R.id.content,new AddFragment());
                        break;
                    case 3:binding.toolbar.setVisibility(View.GONE);
                        transaction1.replace(R.id.content, new NotiFragment());

                        break;
                    case 4:binding.toolbar.setVisibility(View.VISIBLE);
                        transaction1.replace(R.id.content, new ProfileFragment());
                        break;

                }
                transaction1.commit();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.setting:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);

    }
}