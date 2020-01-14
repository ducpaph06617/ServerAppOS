package com.dev.serverappos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.serverappos.adapter.Adapter;
import com.dev.serverappos.adapter.ColorAdapter;
import com.dev.serverappos.adapter.ColorAdapter2;
import com.dev.serverappos.adapter.ImageAdapter;
import com.dev.serverappos.adapter.ImageAdapter2;
import com.dev.serverappos.model.ColorModel;
import com.dev.serverappos.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class UpdateProActivity extends AppCompatActivity {

    private ScrollView layout;
    private EditText edtTenshop;
    private RecyclerView recyclerviewimg;
    private EditText edtTensp;
    private EditText edtGia;
    private Spinner spinner;
    private EditText edtSoluong;
    private Button btnChonAnh;
    private Button btnChonmau;
    private Button btnTinhtrang;
    private Button btnTrangthai;
    private EditText edtMota;
    private Button btnUpdate;
    private GifImageView loading;
    private String id = "";
    private static final int REQUEST_LIST_CODE = 0;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Adapter adapter;
    private int i = 0;
    private ArrayList<String> uri = new ArrayList<>();

    private ArrayList<String> path = new ArrayList<>();

    private List<ColorModel> models = new ArrayList<>();

    private List<String> listcolor = new ArrayList<>();
    private String data = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pro);
        mapped();
        edtTenshop.setEnabled(false);
        showProduct();
        onclick();
        updatePro();
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }
    private void mapped() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://onlinestore2-676d4.appspot.com");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        layout = findViewById(R.id.layout);
        edtTenshop = findViewById(R.id.edt_tenshop);
        recyclerviewimg = findViewById(R.id.recyclerviewimg);
        edtTensp = findViewById(R.id.edt_tensp);
        edtGia = findViewById(R.id.edt_gia);
        spinner = findViewById(R.id.spinner);
        edtSoluong = findViewById(R.id.edt_soluong);
        btnChonmau = findViewById(R.id.btn_chonmau);
        btnTinhtrang = findViewById(R.id.btn_tinhtrang);
        btnTrangthai = findViewById(R.id.btn_trangthai);
        edtMota = findViewById(R.id.edt_mota);
        btnUpdate = findViewById(R.id.btn_capnhat);
        loading = findViewById(R.id.loading);
        btnChonAnh = findViewById(R.id.btn_themanh);

    }
    private void showProduct(){

        mapped();
        LinearLayoutManager imglayout;
        Intent intent = getIntent();
        String idsp = intent.getStringExtra("idpro");
        imglayout = new LinearLayoutManager(UpdateProActivity.this, LinearLayoutManager.HORIZONTAL, false);

        uri.clear();
        ImageAdapter2 imageAdapter = new ImageAdapter2(UpdateProActivity.this, uri, R.layout.item_image);
        recyclerviewimg.setLayoutManager(imglayout);
        recyclerviewimg.setAdapter(imageAdapter);


        mDatabase.child("id").child("User").child("sp").child(idsp).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Object o = dataSnapshot.getValue();
                uri.add(o.toString());
                Log.e("TAG", uri.size() + "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageAdapter.notifyDataSetChanged();

        mDatabase.child("id").child(idsp).child("product").child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User.Product product = dataSnapshot.getValue(User.Product.class);
                edtTensp.setText(product.getNameproduct());
                edtTenshop.setText(product.getNameshop());
                edtGia.setText( product.getPriceproduct());
                edtMota.setText(product.getDescribe());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void updatePro(){
        final ArrayList<String> listLoai = new ArrayList<>();
        listLoai.add("Chọn loại");
        listLoai.add("Thời trang");
        listLoai.add("Đồ điện tử");
        listLoai.add("Thực phẩm");
        listLoai.add("Khác");



        ArrayAdapter adapter = new ArrayAdapter(UpdateProActivity.this, android.R.layout.simple_spinner_item, listLoai);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, final long in) {
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        id = intent.getStringExtra("id");
                        String idU = id;
                        String nameshop = edtTenshop.getText().toString().trim();
                        String nameproduc = edtTensp.getText().toString().trim();
                        String price = edtGia.getText().toString().trim();
                        String des = edtMota.getText().toString().trim();

                        final String neww = sharedPreferences.getString("new", "");
                        String statuss = sharedPreferences.getString("status", "");
                        if (listLoai.get(position).equalsIgnoreCase("Chọn loại")) {
                            Toast.makeText(UpdateProActivity.this, "Chọn Loại!!!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (nameshop.equals("")) {
                            return;
                        }
                        if (nameproduc.equals("")) {
                            edtTensp.setError("Chưa nhập tên!!!");
                            return;
                        }
                        if (price.equals("")) {
                            edtGia.setError("Chưa nhập giá!!!");
                            return;
                        }

                        if (des.equals("")) {
                            edtMota.setError("Chưa nhập mô tả!!!");
                            return;
                        }
                        if (uri.isEmpty()) {
                            Toast.makeText(UpdateProActivity.this, "Chọn Ảnh!!!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String idsp = intent.getStringExtra("idpro");
                        Calendar calendar = Calendar.getInstance();
                        User.Product product = new User.Product(idU,nameshop, nameproduc, price, data, neww, statuss, des, idsp,
                                uri.get(0), listLoai.get(position), String.valueOf(calendar.getTimeInMillis()));

                        List<String> sp = new ArrayList<>();
                        sp.clear();
                        List<User.Id> ids = new ArrayList<>();
                        ids.add(new User.Id(idsp));
                        sp.add(idsp);

                        mDatabase.child("id").child("User").child("sp").child(sp.get(0)).setValue(uri);
                        mDatabase.child("id").child(idsp).child("product").child("product").setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdateProActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                System.exit(0);
                                startActivity(new Intent(UpdateProActivity.this, HomeActivity.class));
                                finish();

                            }
                        });

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void onclick() {


        btnChonmau.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnChonmau.setText("Chọn màu");
                btnChonmau.setTextColor(R.color.greenDark);
                btnChonmau.setTextSize(10f);
                addlistcolor();
            }
        });

        btnTinhtrang.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnTinhtrang.setText("Chọn trạng thái");
                btnTinhtrang.setTextColor(R.color.greenDark);
                btnTinhtrang.setTextSize(10f);
                qualitysp();
            }
        });

        btnTrangthai.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnTrangthai.setText("Chọn tình trạng");
                btnTrangthai.setTextColor(R.color.greenDark);
                btnTrangthai.setTextSize(10f);
                status();
            }
        });
    }
    private void addlistcolor() {
        models.clear();
        listcolor.clear();
        models.add(new ColorModel("Xanh"));
        models.add(new ColorModel("Đỏ"));
        models.add(new ColorModel("Tím"));
        models.add(new ColorModel("Vàng"));
        models.add(new ColorModel("Lục"));
        models.add(new ColorModel("Lam"));
        models.add(new ColorModel("Chàm"));
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_color);
        dialog.setCanceledOnTouchOutside(false);
        GridView girdlayout;
        girdlayout = dialog.findViewById(R.id.girdlayout);
        final ColorAdapter2 colorAdapter = new ColorAdapter2(UpdateProActivity.this, models);
        girdlayout.setAdapter(colorAdapter);
        final Button btnthemmau;
        final Button btnThem;
        final EditText edtthemmau;
        btnthemmau = dialog.findViewById(R.id.txtThem);
        btnThem = dialog.findViewById(R.id.btn_them);
        edtthemmau = dialog.findViewById(R.id.edtthemmau);

        btnthemmau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtthemmau.getText().toString().trim().equals("")) {
                    return;
                }
                boolean b = false;
                for (int i = 0; i < models.size(); i++) {
                    if (edtthemmau.getText().toString().trim().equalsIgnoreCase(models.get(i).getColor())) {
                        b = true;
                        break;
                    }
                }
                if (b == false) {
                    models.add(new ColorModel(edtthemmau.getText().toString().trim()));
                    colorAdapter.notifyDataSetChanged();
                }
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (listcolor == null) {
                    return;
                } else {

                    for (int i = 0; i < listcolor.size(); i++) {
                        data += listcolor.get(i) + ",";
                        btnChonmau.setText("Màu:" + data);
                        btnChonmau.setTextColor(R.color.greenDark);
                        btnChonmau.setTextSize(10f);
                    }
                    dialog.dismiss();
                }
            }
        });


        dialog.show();
    }

    public void addcolor(int position) {
        boolean a = listcolor.add(models.get(position).getColor());
        Log.e("TAG", a + "");
        Log.e("TAG", listcolor.size() + "");
    }

    public void deletecolor(String color) {

        for (int i = 0; i < listcolor.size(); i++) {
            if (color.equals(listcolor.get(i))) {
                listcolor.remove(i);
            }
        }
        Log.e("TAG", listcolor.size() + "");
    }

    private void qualitysp() {
        editor = sharedPreferences.edit();
        PopupMenu popupMenu = new PopupMenu(this, btnTinhtrang);
        popupMenu.getMenuInflater().inflate(R.menu.qualitysp, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new100:
                        btnTinhtrang.setTextColor(R.color.greenDark);
                        btnTinhtrang.setTextSize(10f);
                        btnTinhtrang.setText("Mới 100%");
                        editor.putString("new", "Mới 100%");
                        editor.commit();
                        break;

                    case R.id.new90:
                        btnTinhtrang.setTextColor(R.color.greenDark);
                        btnTinhtrang.setTextSize(10f);
                        btnTinhtrang.setText("Mới 90%");
                        editor.putString("new", "Mới 90%");
                        editor.commit();
                        break;

                    case R.id.new80:
                        btnTinhtrang.setTextColor(R.color.greenDark);
                        btnTinhtrang.setTextSize(10f);
                        btnTinhtrang.setText("Mới 80%");
                        editor.putString("new", "Mới 80%");
                        editor.commit();
                        break;

                    case R.id.new70:
                        btnTinhtrang.setTextColor(R.color.greenDark);
                        btnTinhtrang.setTextSize(10f);
                        btnTinhtrang.setText("Mới 70%");
                        editor.putString("new", "Mới 70%");
                        editor.commit();
                        break;

                    case R.id.new50:
                        btnTinhtrang.setTextColor(R.color.greenDark);
                        btnTinhtrang.setTextSize(10f);
                        btnTinhtrang.setText("Mới 50%");
                        editor.putString("new", "Mới 50%");
                        editor.commit();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void status() {
        editor = sharedPreferences.edit();
        PopupMenu popupMenu = new PopupMenu(this, btnTrangthai);
        popupMenu.getMenuInflater().inflate(R.menu.status, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.available:
                        btnTrangthai.setText(R.string.available);
                        btnTrangthai.setTextSize(10f);
                        btnTrangthai.setTextColor(R.color.greenDark);
                        editor.putString("status", "Có sẵn");
                        editor.commit();
                        break;
                    case R.id.oder:
                        btnTrangthai.setText(R.string.oder);
                        btnTrangthai.setTextSize(10f);
                        btnTrangthai.setTextColor(R.color.greenDark);
                        editor.putString("status", "Oder");
                        editor.commit();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> pathList = data.getStringArrayListExtra("result");
            uri.clear();
            path.clear();
            i = 0;
            path.addAll(pathList);
            uploadImage();
            adapter.notifyDataSetChanged();
        }

    }
    public void imgselector(View view) {
        Toast.makeText(this, "Đang Phát Triển", Toast.LENGTH_SHORT).show();
//        ISListConfig config = new ISListConfig.Builder()
//                .multiSelect(true)
//
//                .rememberSelected(false)
//
//                .statusBarColor(Color.parseColor("#3F51B5")).build();
//        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }
    private void uploadImage() {

        if (path != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Tải ảnh lên...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Calendar calendar = Calendar.getInstance();
            final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + "");

            mountainsRef.putFile(Uri.fromFile(new File(path.get(i))))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            Log.e("URI", downloadUrl + "");
                            boolean b = uri.add(String.valueOf(downloadUrl));
                            Log.e("B", b + "");
                            Log.e("C", uri.size() + "");
                            i++;
                            uploadImage();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.e("Failed", e.getMessage());
                            return;
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Đang tải" + "(" + (i + 1) + "/" + (path.size()) + ")" + (int) progress + "%");

                            if (i == path.size() - 1 && progress == 100) {
                                {

                                    Toast.makeText(UpdateProActivity.this, "Tải thành công", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });


        }

    }
}
