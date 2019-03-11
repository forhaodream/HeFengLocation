package com.lh.ch.hefenglocation.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lh.ch.hefenglocation.R;
import com.lh.ch.hefenglocation.base.BaseActivity;
import com.lh.ch.hefenglocation.model.PhotoModel;
import com.lh.ch.hefenglocation.model.TestModel;
import com.lh.ch.hefenglocation.net.HttpManager;
import com.lh.ch.hefenglocation.net.MyHttpCallback;
import com.lh.ch.hefenglocation.photo.FullPhotoView;
import com.lh.ch.hefenglocation.premission.EasyPermission;
import com.lh.ch.hefenglocation.premission.PermissionResultCallback;
import com.lh.ch.hefenglocation.util.Url;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CH on 2017/6/1.
 */

public class TakePhotoActivity extends Activity implements View.OnClickListener {
    private FullPhotoView mPhotoView;
    private ImageView chooseImg, putImg;
        private Uri imageUri;
    private static final int TAKE_PHOTO_REQUEST_TWO = 444;
    private static final int TAKE_PHOTO_REQUEST_THREE = 555;
    // 上传图片
    private String imgLiu;
    private String userName, xm;
    private int userId;
    private String te1;
    private String phoneNumber;
    private HttpManager mHttpManager;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private double lati;
    private double longa;
    private DateFormat df;
    private PhotoModel mPhotoModel;
    private Handler mHandler;
    private EasyPermission easyPermission = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo);
        mHandler = new Handler();
        initLocation();
        mLocationClient.start();
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        te1 = tm.getLine1Number();
//        int st = tm.getSimState();
//        if (st == 1) {
//            Toast.makeText(this, "没插卡", Toast.LENGTH_SHORT).show();
//        }
        mPhotoView = (FullPhotoView) findViewById(R.id.home_detail_img);
        mPhotoView.setCustomView(R.layout.popup_default, R.id.popup_default_iv, this);
        chooseImg = (ImageView) findViewById(R.id.home_detail_choose);
        chooseImg.setOnClickListener(this);
        putImg = (ImageView) findViewById(R.id.take_photo_put);
        putImg.setOnClickListener(this);
        // 获取用户名
        SharedPreferences readInfo = this.getSharedPreferences("user_npt", MODE_PRIVATE);
        userName = readInfo.getString("userName", "");
        xm = readInfo.getString("xm", "");
        userId = readInfo.getInt("userId", 1);
        Log.d("-1-1-", userName);
        Log.d("-1-1-", String.valueOf(userId));
        Log.d("-1-1-", xm);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            lati = bdLocation.getLatitude();
            longa = bdLocation.getLongitude();
            Log.d("lati", String.valueOf(lati));
            Log.d("longa", String.valueOf(longa));
            int i = bdLocation.getLocType();
            Log.d("longa", String.valueOf(i));
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span = 1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 20) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap compressScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 512f;
        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

        //return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case 222:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(TakePhotoActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Uri imageUri = data.getData();
//                    String bitmap = getRealPathFromUri();
                    Log.d("imgUri1", getRealPathFromUri(TakePhotoActivity.this, imageUri));
                    mPhotoView.setImageURI(imageUri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TAKE_PHOTO_REQUEST_TWO:
                if (resultCode == RESULT_CANCELED) {
                    delteImageUri(TakePhotoActivity.this, imageUri);
                    return;
                }
                Bitmap photo = data.getParcelableExtra("data");
                mPhotoView.setImageBitmap(photo);

                break;
            case TAKE_PHOTO_REQUEST_THREE:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(TakePhotoActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //    Bitmap to = compressImage(bitmap);
                        Bitmap bit = compressScale(bitmap);
                        imgLiu = bitmapToBase64(bit);
                        mPhotoView.setImageBitmap(bit);
                        Log.d("3456", imgLiu);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                } else {
                    // Android 6.0及以下设备
                    Bitmap bitmap = getSmallBitmap(imageUri.getPath());
                    imgLiu = bitmapToString(imageUri.getPath());
                    mPhotoView.setImageBitmap(bitmap);
                    Log.d("345", imgLiu);

                }
                break;
        }
    }

    public static void delteImageUri(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);

    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = computeInitialSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //计算图片的缩放值
    public static int computeInitialSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    private void showPopueWindow() {
        final View popView = View.inflate(this, R.layout.popwindow_tiwen, null);
        //  Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 1 / 3;

        final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
        //   popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        imageUri = takePhoto(TakePhotoActivity.this, TAKE_PHOTO_REQUEST_THREE);
                        Log.d("imgUri", String.valueOf(imageUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

    }

    // 格式化图片地址
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onResume() {
        if (TextUtils.isEmpty(imgLiu)) {
//            Toast.makeText(this, "loading...", Toast.LENGTH_LONG).show();
            Log.d("dsa", "da");
        } else {
            putImg.setVisibility(View.VISIBLE);
        }
        super.onResume();

    }

    public static Uri takePhoto(Activity mActivity, int flag) throws IOException {

        //指定拍照intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = null;
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            String sdcardState = Environment.getExternalStorageState();
            File outputImage = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                outputImage = createImageFile(mActivity);
            } else {
                Toast.makeText(mActivity.getApplicationContext(), "内存异常", Toast.LENGTH_SHORT).show();
            }
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (outputImage != null) {
                if (Build.VERSION.SDK_INT >= 24) {

                    imageUri = FileProvider.getUriForFile(mActivity.getApplicationContext(), "com.lh.ch.hefenglocation.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);

                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                mActivity.startActivityForResult(takePictureIntent, flag);
            }
        }

        return imageUri;
    }

    public static File createImageFile(Activity mActivity) throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;//创建以时间命名的文件名称
        File storageDir = getOwnCacheDirectory(mActivity, "takephoto");//创建保存的路径
        File image = new File(storageDir.getPath(), imageFileName + ".jpg");
        if (!image.exists()) {
            try {
                //在指定的文件夹中创建文件
                image.createNewFile();
            } catch (Exception e) {
            }
        }

        return image;
    }

    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        //判断sd卡正常挂载并且拥有权限的时候创建文件
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }


    /**
     * 检查是否有权限
     *
     * @param context
     * @return
     */
    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_detail_choose:
                showPopueWindow();

                break;
            case R.id.take_photo_put:
                pushImg();
                break;
        }
    }

    private TestModel mTestModel;

    private void pushImg() {
        Log.d("--23s", String.valueOf(longa));
        Log.d("--23s", String.valueOf(lati));
        String testUrl = "http://www.kuxuel.com/app/addzaixiantiwen.aspx";
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("userid", String.valueOf(userId));
        formBuilder.add("sj", "");
        formBuilder.add("xm", xm);
        formBuilder.add("pic", imgLiu);
        formBuilder.add("jingdu", String.valueOf(longa));
        formBuilder.add("weidu", String.valueOf(lati));
        /**
         *  RequestBody body = FormBody.create(MediaType.parse("application/json"), requestMsg);
         *
         *             Request request = new Request.Builder()
         *                     .url(url)
         *                     .post(body)
         *                     .build();
         */
        Request request = new Request.Builder().url(Url.photoUrl).post(formBuilder.build()).build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("失败原因:", String.valueOf(e));

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
                String body = response.body().string();
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(body));
                mTestModel = gson.fromJson(reader, TestModel.class);
                //   mPhotoModel = gson.fromJson(reader, PhotoModel.class);
                mHandler.post(mRunnable);
//                } else {
//                    Looper.prepare();
//                    Toast.makeText(TakePhotoActivity.this, "内部原因,提交失败", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }


            }
        });


    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.d("dsa", mTestModel.getA());
            if (mTestModel.getA().length() == 1) {
                Toast.makeText(TakePhotoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(TakePhotoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
