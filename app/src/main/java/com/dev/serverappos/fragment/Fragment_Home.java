package com.dev.serverappos.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev.serverappos.R;

import com.dev.serverappos.UpdateProActivity;
import com.dev.serverappos.adapter.ColorAdapter2;
import com.dev.serverappos.adapter.ImageAdapter;
import com.dev.serverappos.adapter.ProductuserAdapter;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends BaseFragment {

    private RecyclerView recyclerviewproduct;
    private String id = "";
    private DatabaseReference mDatabase;
    private ArrayList<User.Product> products = new ArrayList<>();
    private ProductuserAdapter productuserAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView txtthongbao;
    private ArrayList<String> uri = new ArrayList<>();

    private ArrayList<User.BillDeltail> deltails = new ArrayList<>();
    private ArrayList<String> bills = new ArrayList<>();


    private ArrayList<User.cartsp> cartsps = new ArrayList<>();
    private ArrayList<String> carts = new ArrayList<>();
    private String data = "";
    private static final int REQUEST_LIST_CODE = 0;
    private TextView txtProduct;
    private RecyclerView recyclerviewimg;
    private TextView txProduct;
    private TextView txtprice;
    private TextView txtnameshop;
    private TextView txtSanPham;
    private TextView txtdate;
    private TextView txtloai;
    private TextView txttinhtrang;
    private TextView txttrangthai;
    private TextView txtsoluong;
    private TextView txtmota;
    private ImageView left;
    private TextView intgio;
    private TextView themvaogio;
    private TextView muangay;
    private ImageView giohang;
    private LinearLayout layout;
    private EditText edtTenshop;
    private EditText edtTensp;
    private EditText edtGia;
    private Spinner spinner;
    private EditText edtSoluong;
    private Button btnChonAnh;
    private Button btnChonmau;
    private Button btnTinhtrang;
    private Button btnTrangthai;
    private EditText edtMota;
    private Button btnDangsp;
    private GifImageView loading;
    private int i = 0;
    APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerviewproduct = view.findViewById(R.id.recyclerviewproduct);
        txtthongbao = view.findViewById(R.id.txtthongbao);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        productuserAdapter = new ProductuserAdapter(this, products);
        recyclerviewproduct.setLayoutManager(linearLayoutManager);
        recyclerviewproduct.setHasFixedSize(true);
        recyclerviewproduct.setAdapter(productuserAdapter);
        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("id");
        Log.e("IDUS", "onCreateView: " + id);
//        Toast.makeText(getActivity(), "Home Fragment", Toast.LENGTH_SHORT).show();
        if (id != null) {
            getproductuser();
        } else {
            Toast.makeText(getActivity(), "chưa đăng nhập!!!", Toast.LENGTH_SHORT).show();
        }


        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        return view;
    }


    public void deletesp(final User.Product product, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa sản phẩm\t" + product.getNameproduct() + "\tkhông?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity(), "Xóa không thành công", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Xóa Thành công"+product.getNameproduct(), Toast.LENGTH_SHORT).show();
                mDatabase.child("id").child("User").child(id).child("bill").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User.BillDeltail deltail = dataSnapshot.getValue(User.BillDeltail.class);
                        if (dataSnapshot.getKey() != null) {
                            Log.e("CHECK1", dataSnapshot.getKey());
                            bills.add(0, dataSnapshot.getKey());
                            Log.e("CHECK3", bills.get(0));
                        }
                            deltails.add(0, deltail);


                            for (int j = 0; j < deltails.size(); j++) {
                                if (deltails.get(j).getIdsp().equalsIgnoreCase(product.getIdsp())) {



                                    mDatabase.child("id").child("User").child(id).child("bill").child(deltails.get(j).getIdbilll()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    mDatabase.child("id").child("User").child(deltails.get(j).getIdMua()).child("bill").child(deltails.get(j).getIdbilll()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });

                                    mDatabase.child("id").child("User").child(deltails.get(j).getIdMua()).child("cart").child(deltails.get(j).getIdsp()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Sản phẩm đang có người mua", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    mDatabase.child("id").child("User").child(id).child("cart").child(deltails.get(j).getIdsp()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Sản phẩm đang có người mua 2", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }

                            }

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
                mDatabase.child("id").child(product.getIdsp()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

                mDatabase.child("id").child("User").child(id).child("user").child("idsp").child(product.getIdsp()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                mDatabase.child("id").child("User").child("sp").child(product.getIdsp()).removeValue();

                getproductuser();
                productuserAdapter.notifyDataSetChanged();
                if (i == 0) {
                    txtthongbao.setVisibility(View.VISIBLE);
                }


                productuserAdapter.notifyDataSetChanged();
            }


        });
        builder.show();
    }

    private void getproductuser() {
        products.clear();
        mDatabase.child("id").child("User").child(id).child("user").child("idsp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Object o = dataSnapshot.getValue();
                Log.e("O", o.toString());

                mDatabase.child("id").child(o.toString()).child("product").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User.Product product = dataSnapshot.getValue(User.Product.class);
                        products.add(0, product);
                        txtthongbao.setVisibility(View.GONE);
                        productuserAdapter.notifyDataSetChanged();
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
    }

    public void clickprodut(final User.Product product) {


        final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_click);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);


        muangay = dialog.findViewById(R.id.muangay);

        layout = dialog.findViewById(R.id.layout);


        final Calendar calendar1 = Calendar.getInstance();

        muangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProActivity.class);
                intent.putExtra("idpro", product.getIdsp());
                intent.putExtra("id", id);
                startActivity(intent);
                //Log.e("IDSP", "onClick: "+product.getIdsp());
            }
        });

        txtloai = dialog.findViewById(R.id.txtloai);
        txttinhtrang = dialog.findViewById(R.id.txttinhtrang);
        txttrangthai = dialog.findViewById(R.id.txttrangthai);
        txtmota = dialog.findViewById(R.id.txtmota);

        LinearLayoutManager imglayout;
        txtProduct = dialog.findViewById(R.id.txtProduct);
        recyclerviewimg = dialog.findViewById(R.id.recyclerviewimg);
        txProduct = dialog.findViewById(R.id.txProduct);
        txtprice = dialog.findViewById(R.id.txtprice);

        txtdate = dialog.findViewById(R.id.txtdate);

        imglayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        uri.clear();
        ImageAdapter imageAdapter = new ImageAdapter(Fragment_Home.this, uri, R.layout.item_image);
        recyclerviewimg.setLayoutManager(imglayout);
        recyclerviewimg.setAdapter(imageAdapter);
        String idsp = product.getIdsp();
        mDatabase.child("id").child("User").child("sp").child(idsp).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Object o = dataSnapshot.getValue();
                uri.add(o.toString());
                Log.e("TAG", uri.size() + "");
                Log.e("IMG", "onChildAdded: " + o.toString());
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
//        txProduct.setText(product.getNameproduct());
        txtProduct.setText(product.getNameproduct());
        txtprice.setText("đ" + product.getPriceproduct());
        txtloai.setText(product.getLoaisp());
        txttrangthai.setText(product.getStatus());
        txttinhtrang.setText(product.getLovestatus());
        txtmota.setText(product.getDescribe());


        String thoigia = product.getThoigian();
        Calendar calendar = Calendar.getInstance();
        int date = (int) ((calendar.getTimeInMillis() - Long.valueOf(thoigia)) / (1000 * 60 * 60 * 24));
        if (date == 0) {
            txtdate.setText("Hôm nay");
        } else if (date <= 30) {
            txtdate.setText(date + "ngày");
        } else {
            ///..........//

        }


        dialog.show();

    }

}
