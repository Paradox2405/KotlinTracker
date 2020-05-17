package com.example.kotlintracker.scanning

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.kotlintracker.R
import kotlinx.android.synthetic.main.activity_barcode_scan.*


class BarcodeScan : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    val MY_CAMERA_PERMISSION_REQUEST=1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan)

        codeScanner= CodeScanner(this, scannerView)
        codeScanner.camera=CodeScanner.CAMERA_BACK
        codeScanner.formats=CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode=AutoFocusMode.SAFE
        codeScanner.scanMode=ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled=true
        codeScanner.isFlashEnabled=false

        codeScanner.decodeCallback= DecodeCallback {
            runOnUiThread {
                Toast.makeText(this,"scan result: ${it.text}",Toast.LENGTH_LONG).show()
            }
        }

        codeScanner.errorCallback= ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera error: ${it.message}",Toast.LENGTH_LONG).show()
            }
        }
        checkpermission()
    }
    fun checkpermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),MY_CAMERA_PERMISSION_REQUEST)

        }else{
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==MY_CAMERA_PERMISSION_REQUEST&&grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            codeScanner.startPreview()
        }else{
            Toast.makeText(this,"cannot scan until permission is given",Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
