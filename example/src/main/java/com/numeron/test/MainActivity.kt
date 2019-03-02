package com.numeron.test

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.numeron.gr11th.requestPermissions
import com.numeron.gr11th.startActivityForResult
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.append("　Kotlin")
    }

    override fun onClick(v: View?) {
        //先申请权限
        requestPermissions(Manifest.permission.CAMERA) {
            //把照片保存到不需要权限就能使用的缓存目录下
            val photoPath = externalCacheDir ?: cacheDir
            if (!photoPath.exists()) photoPath.mkdirs()
            //分配一个尽量不重复的名称
            val photoFile = File(photoPath, "${System.currentTimeMillis()}.jpg")
            if (photoFile.exists()) photoFile.delete()
            //转换为uri
            val outputUri = photoFile.toUri()
            //装载Intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            //启动Activity并接收结果
            startActivityForResult(intent) {
                //显示图片
                imageView.setImageURI(outputUri)
            }.setOnCanceledCallback {
                toast("取消了拍照")
            }
            //设置申请权限失败时的回调
        }.setOnDeniedCallback {
            AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setCancelable(false)
                    .setMessage("相册权限被拒绝，无法调用相机功能。")
                    .setPositiveButton("我知道了") { d, _ ->
                        d.dismiss()
                    }
                    .setNegativeButton("再次申请") { d, _ ->
                        //直接调用此方法来继续发起申请权限操作
                        onClick(null)
                        d.dismiss()
                    }
                    .show()
        }
    }

    private fun File.toUri(): Uri {
        return if (Build.VERSION.SDK_INT > 23) {
            FileProvider.getUriForFile(this@MainActivity, "$packageName.FileProvider", this)
        } else {
            Uri.fromFile(this)
        }
    }

}
