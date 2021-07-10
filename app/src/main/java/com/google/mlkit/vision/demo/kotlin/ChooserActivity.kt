
package com.google.mlkit.vision.demo.kotlin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.demo.R
import java.io.IOException
import java.util.*

/**
 * Demo app chooser which takes care of runtime permission requesting and allow you pick from all
 * available testing Activities.
 */
private var mMediaProjectionCallback: ChooserActivity.MediaProjectionCallback? = null
var mToggleButton: ToggleButton? = null
var mMediaRecorder: MediaRecorder? = null
private var mVirtualDisplay: VirtualDisplay? = null

private var mMediaProjection: MediaProjection? = null
class ChooserActivity :
        AppCompatActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback{
  private val REQUEST_CODE = 1000
  private var mScreenDensity = 0
  private var mProjectionManager: MediaProjectionManager? = null
  private val DISPLAY_WIDTH = 720
  private val DISPLAY_HEIGHT = 1280
  private var mMediaProjection: MediaProjection? = null

  private var mVirtualDisplay: VirtualDisplay? = null
  private var mMediaProjectionCallback: MediaProjectionCallback? = null
  var mToggleButton: ToggleButton? = null
  var mMediaRecorder: MediaRecorder? = null
  private val ORIENTATIONS = SparseIntArray()
  private val REQUEST_PERMISSIONS = 10

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    setContentView(R.layout.activity_chooser)

    // Set up ListView and Adapter
    val button =
            findViewById<Button>(R.id.next)

    button.setOnClickListener(object : View.OnClickListener {
      override fun onClick(view: View?) {
        // Do some work here
        val intent: Intent = Intent(this@ChooserActivity, LivePreviewActivity::class.java);
        this@ChooserActivity.startActivity(intent)
      }

    });
    if (!allPermissionsGranted()) {
      getRuntimePermissions()
    }
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    mScreenDensity = metrics.densityDpi

    mMediaRecorder = MediaRecorder()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    mToggleButton = findViewById<View>(R.id.toggle) as ToggleButton
    mToggleButton!!.setOnClickListener(View.OnClickListener { v ->
      if (ContextCompat.checkSelfPermission(this,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                      .checkSelfPermission(this,
                              Manifest.permission.RECORD_AUDIO)
              != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
          mToggleButton!!.setChecked(false)
          Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
                  Snackbar.LENGTH_INDEFINITE).setAction("ENABLE"
          ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO),
                    REQUEST_PERMISSIONS)
          }.show()
        } else {
          ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO),
                  REQUEST_PERMISSIONS)
        }
      } else {
        onToggleScreenShare(v)
      }
    })
  }
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode !=REQUEST_CODE) {
      Log.e(TAG, "Unknown request code: $requestCode")
      return
    }
    if (resultCode != Activity.RESULT_OK) {
      Toast.makeText(this,
              "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show()
      mToggleButton!!.isChecked = false
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mMediaProjectionCallback = MediaProjectionCallback()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mMediaProjection = mProjectionManager!!.getMediaProjection(resultCode, data!!)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mMediaProjection!!.registerCallback(mMediaProjectionCallback, null)
    }
    mVirtualDisplay = createVirtualDisplay()
    mMediaRecorder!!.start()
  }
  fun onToggleScreenShare(view: View) {
    if ((view as ToggleButton).isChecked) {
      initRecorder()
      shareScreen()
    } else {
      mMediaRecorder!!.stop()
      mMediaRecorder!!.reset()
      Log.v(TAG, "Stopping Recording")
      stopScreenSharing()
    }
  }
  private fun shareScreen() {
    if (mMediaProjection == null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        startActivityForResult(mProjectionManager!!.createScreenCaptureIntent(),REQUEST_CODE)
      }
      return
    }
    mVirtualDisplay = createVirtualDisplay()
    mMediaRecorder!!.start()
  }

  private fun createVirtualDisplay(): VirtualDisplay {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mMediaProjection!!.createVirtualDisplay("MainActivity",
              DISPLAY_WIDTH,DISPLAY_HEIGHT, mScreenDensity,
              DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
              mMediaRecorder!!.surface, null /*Callbacks*/, null /*Handler*/
      )
    } else {
      TODO("VERSION.SDK_INT < LOLLIPOP")
    }
  }

  private fun initRecorder() {
    try {
      mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
      mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
      mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
      mMediaRecorder!!.setOutputFile(Environment
              .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/video" +u + ".mp4")
      u++
      mMediaRecorder!!.setVideoSize(DISPLAY_WIDTH,DISPLAY_HEIGHT)
      mMediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
      mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
      mMediaRecorder!!.setVideoEncodingBitRate(512 * 1000)
      mMediaRecorder!!.setVideoFrameRate(30)
      val rotation = windowManager.defaultDisplay.rotation
      val orientation: Int =ORIENTATIONS.get(rotation + 90)
      mMediaRecorder!!.setOrientationHint(orientation)
      mMediaRecorder!!.prepare()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  class MediaProjectionCallback : MediaProjection.Callback() {
    override fun onStop() {
      if (mToggleButton!!.isChecked()) {
        mToggleButton!!.setChecked(false)
        mMediaRecorder!!.stop()
        mMediaRecorder!!.reset()
        Log.v(TAG, "Recording Stopped")
      }
      mMediaProjection = null
      if (mVirtualDisplay == null) {
        return
      }
      mVirtualDisplay!!.release()
      //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
      // be reused again
      //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
      // be reused again
    }
  }

  fun stopScreenSharing() {
    if (mVirtualDisplay == null) {
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mVirtualDisplay!!.release()
    }
    //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
    // be reused again
    destroyMediaProjection()
  }

  override fun onDestroy() {
    super.onDestroy()
    destroyMediaProjection()
  }

  private fun destroyMediaProjection() {
    if (mMediaProjection != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mMediaProjection!!.unregisterCallback(mMediaProjectionCallback)
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mMediaProjection!!.stop()
      }
      mMediaProjection = null
    }
    Log.i(TAG, "MediaProjection Stopped")
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String?>,
                                          grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      REQUEST_PERMISSIONS -> {
        if (grantResults.size > 0 && grantResults[0] +
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          onToggleScreenShare(mToggleButton!!)
        } else {
          mToggleButton!!.isChecked = false
          Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
                  Snackbar.LENGTH_INDEFINITE).setAction("ENABLE"
          ) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(intent)
          }.show()
        }
        return
      }
    }

  }


  private fun getRequiredPermissions(): Array<String?> {
    return try {
      val info = this.packageManager
              .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
      val ps = info.requestedPermissions
      if (ps != null && ps.isNotEmpty()) {
        ps
      } else {
        arrayOfNulls(0)
      }
    } catch (e: Exception) {
      arrayOfNulls(0)
    }
  }

  private fun allPermissionsGranted(): Boolean {
    for (permission in getRequiredPermissions()) {
      permission?.let {
        if (!isPermissionGranted(this, it)) {
          return false
        }
      }
    }
    return true
  }

  private fun getRuntimePermissions() {
    val allNeededPermissions = ArrayList<String>()
    for (permission in getRequiredPermissions()) {
      permission?.let {
        if (!isPermissionGranted(this, it)) {
          allNeededPermissions.add(permission)
        }
      }
    }

    if (allNeededPermissions.isNotEmpty()) {
      ActivityCompat.requestPermissions(
              this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
      )
    }
  }

  private fun isPermissionGranted(context: Context, permission: String): Boolean {
    if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
    ) {
      Log.i(TAG, "Permission granted: $permission")
      return true
    }
    Log.i(TAG, "Permission NOT granted: $permission")
    return false
  }

  private class MyArrayAdapter(
          private val ctx: Context,
          resource: Int,
          private val classes: Array<Class<*>>
  ) : ArrayAdapter<Class<*>>(ctx, resource, classes) {
    private var descriptionIds: IntArray? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      var view = convertView

      if (convertView == null) {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(android.R.layout.simple_list_item_2, null)
      }

      (view!!.findViewById<View>(android.R.id.text1) as TextView).text =
              classes[position].simpleName
      descriptionIds?.let {
        (view.findViewById<View>(android.R.id.text2) as TextView).setText(it[position])
      }

      return view
    }

    fun setDescriptionIds(descriptionIds: IntArray) {
      this.descriptionIds = descriptionIds
    }
  }

  companion object {
    private const val TAG = "ChooserActivity"
    private var u = 1

    private const val PERMISSION_REQUESTS = 1
    private val CLASSES = arrayOf<Class<*>>(
            LivePreviewActivity::class.java
    )

  }
}
