package com.numeron.test;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.numeron.gr11th.EliminateResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import org.jetbrains.anko.ToastsKt;

import java.io.File;
import java.util.List;

public class JavaActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.button)).append("　Java");
    }

    @Override
    public void onClick(View v) {
        EliminateResultKt.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, new Function0<Unit>() {
                    @Override
                    //申请权限成功时的回调
                    public Unit invoke() {
                        //把照片保存到不需要权限就能使用的缓存目录下
                        File photoPath = getExternalCacheDir();
                        if (photoPath == null) {
                            photoPath = getCacheDir();
                        }
                        if (!photoPath.exists()) photoPath.mkdirs();
                        //分配一个尽量不重复的名称
                        File photoFile = new File(photoPath, System.currentTimeMillis() + ".jpg");
                        if (photoFile.exists()) photoFile.delete();
                        //转换为uri
                        final Uri outputUri = toUri(photoFile);
                        //装载Intent
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                .putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                        //启动Activity并接收结果
                        EliminateResultKt.startActivityForResult(JavaActivity.this,
                                intent, null, new Function1<Intent, Unit>() {
                                    @Override
                                    public Unit invoke(Intent intent) {
                                        //成功时，通过Uri来显示图片
                                        ((ImageView) findViewById(R.id.imageView)).setImageURI(outputUri);
                                        return Unit.INSTANCE;
                                    }
                                })
                                .setOnCanceledCallback(new Function0<Unit>() {
                                    @Override
                                    //取消拍照时，弹出Toast提示
                                    public Unit invoke() {
                                        //使用Kotlin Anko Support的工具来弹出Toast提示
                                        ToastsKt.toast(JavaActivity.this, "取消了拍照");
                                        return Unit.INSTANCE;
                                    }
                                });
                        return Unit.INSTANCE;
                    }
                })
                .setOnDeniedCallback(new Function1<List<String>, Unit>() {
                    @Override
                    //申请失败时的回调
                    public Unit invoke(List<String> strings) {
                        new AlertDialog.Builder(JavaActivity.this)
                                .setTitle("提示")
                                .setCancelable(false)
                                .setMessage("相册权限被拒绝，无法调用相机功能。")
                                .setPositiveButton("我知道了", (d, i) -> d.dismiss())
                                .setNegativeButton("再次申请", (d, i) -> {
                                    onClick(null);
                                    d.dismiss();
                                })
                                .show();
                        return Unit.INSTANCE;
                    }
                });
    }

    private Uri toUri(File file) {
        if (Build.VERSION.SDK_INT > 23) {
            return FileProvider.getUriForFile(this, getPackageName() + ".FileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

}
