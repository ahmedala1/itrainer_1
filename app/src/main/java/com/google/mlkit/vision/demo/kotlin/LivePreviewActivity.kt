

package com.google.mlkit.vision.demo.kotlin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.annotation.KeepName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.mlkit.vision.demo.CameraSource
import com.google.mlkit.vision.demo.CameraSourcePreview
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.posedetector.*
import com.google.mlkit.vision.demo.preference.PreferenceUtils
import org.w3c.dom.Text
import java.io.IOException
import java.util.*

/** Live preview demo for ML Kit APIs.  */
@KeepName
class LivePreviewActivity :
  AppCompatActivity(),
  ActivityCompat.OnRequestPermissionsResultCallback,
  OnItemSelectedListener,
  CompoundButton.OnCheckedChangeListener {
  var database: FirebaseDatabase? = null
  var firebaseAuth: FirebaseAuth? = null
  var useriid: String? = null
  var mYear: String? = null
  var mMonth:kotlin.String? = null
   var mDay:kotlin.String? = null

var s:kotlin.Int?=null
  var databaseReference: DatabaseReference? = null
  private var cameraSource: CameraSource? = null
  private var preview: CameraSourcePreview? = null
  private var graphicOverlay: GraphicOverlay? = null
  private var selectedModel = OBJECT_DETECTION

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    setContentView(R.layout.activity_vision_live_preview)
    firebaseAuth = FirebaseAuth.getInstance()
    useriid = firebaseAuth!!.uid
   val save=findViewById(R.id.buttonSaveData) as TextView
    val c = Calendar.getInstance()

    save.setOnClickListener {

val hour=c[Calendar.HOUR_OF_DAY].toString()
      mYear = c[Calendar.YEAR].toString()
      mMonth = c[Calendar.MONTH].toString()
      mDay = c[Calendar.DAY_OF_MONTH].toString()
      database = FirebaseDatabase.getInstance()
      databaseReference = database!!.reference.child("tasks").child(useriid!!)
      val id: String = databaseReference!!.push().getKey().toString()

      Toast.makeText(this, "data added", Toast.LENGTH_LONG).show()
      if(s==1)
      {
        val contact = model(useriid, mDay, mMonth, mYear,hour,PoseGraphic.upCountbiceps,PoseGraphic.type)
        databaseReference!!.child(id).setValue(contact)
      }
      if(s== 2)
      {
        val contact = model(useriid, mDay, mMonth, mYear,hour,PoseGraphic.upCountbiceps,PoseGraphic2.type)
        databaseReference!!.child(id).setValue(contact)
      }
      if(s== 3)
      {
        val contact = model(useriid, mDay, mMonth, mYear,hour,PoseGraphic.upCountbiceps,triceps.type)
        databaseReference!!.child(id).setValue(contact)
      }
      if(s== 4)
      {
        val contact = model(useriid, mDay, mMonth, mYear,hour,PoseGraphic.upCountbiceps,shoulder.type)
        databaseReference!!.child(id).setValue(contact)
      }
    }

    // Get Current Date


    preview = findViewById(R.id.preview_view)
    if (preview == null) {
      Log.d(TAG, "Preview is null")
    }

    graphicOverlay = findViewById(R.id.graphic_overlay)
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null")
    }

    val spinner = findViewById<Spinner>(R.id.spinner)
    val options: MutableList<String> = ArrayList()

    options.add(POSE_DETECTION)
    options.add(POSE_DETECTION2)
    options.add(POSE_DETECTION3)
    options.add(POSE_DETECTION4)

    // Creating adapter for spinner
    val dataAdapter =
      ArrayAdapter(this, R.layout.spinner_style, options)

    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    // attaching data adapter to spinner
    spinner.adapter = dataAdapter
    spinner.onItemSelectedListener = this

    val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
    facingSwitch.setOnCheckedChangeListener(this)


    if (allPermissionsGranted()) {
      createCameraSource(selectedModel)
    } else {
      runtimePermissions
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.live_preview_menu, menu)
    return true
  }



  @Synchronized
  override fun onItemSelected(
    parent: AdapterView<*>?,
    view: View?,
    pos: Int,
    id: Long
  ) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent?.getItemAtPosition(pos).toString()
    Log.d(TAG, "Selected model: $selectedModel")
    preview?.stop()
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel)
      startCameraSource()
    } else {
      runtimePermissions
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    // Do nothing.
  }

  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    Log.d(TAG, "Set facing")
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
      } else {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
      }
    }
    preview?.stop()
    startCameraSource()
  }

  private fun createCameraSource(model: String) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = CameraSource(this, graphicOverlay)
    }
    try {
      when (model) {

        POSE_DETECTION -> {
          s= 1
          val poseDetectorOptions =
            PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
          val shouldShowInFrameLikelihood =
            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
          Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
          cameraSource!!.setMachineLearningFrameProcessor(
            PoseDetectorProcessor(this, poseDetectorOptions, shouldShowInFrameLikelihood)

          )
        }
        POSE_DETECTION2 -> {
          s= 2

          val poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
          val shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
          Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
          cameraSource!!.setMachineLearningFrameProcessor(
                  posedetectorprocessor2(this, poseDetectorOptions, shouldShowInFrameLikelihood)
          )
        }
        POSE_DETECTION3 -> {
          s= 3

          val poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
          val shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
          Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
          cameraSource!!.setMachineLearningFrameProcessor(
                  posedetectorprocessor3(this, poseDetectorOptions, shouldShowInFrameLikelihood)
          )
        }
        POSE_DETECTION4 -> {
          s=4
          val poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
          val shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
          Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
          cameraSource!!.setMachineLearningFrameProcessor(
                  pose4(this, poseDetectorOptions, shouldShowInFrameLikelihood)
          )
        }
        else -> Log.e(TAG, "Unknown model: $model")
      }
    } catch (e: Exception) {
      Log.e(TAG, "Can not create image processor: $model", e)
      Toast.makeText(
        applicationContext, "Can not create image processor: " + e.message,
        Toast.LENGTH_LONG
      ).show()
    }
  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private fun startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null")
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null")
        }
        preview!!.start(cameraSource, graphicOverlay)
      } catch (e: IOException) {
        Log.e(TAG, "Unable to start camera source.", e)
        cameraSource!!.release()
        cameraSource = null
      }
    }
  }

  public override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume")
    createCameraSource(selectedModel)
    startCameraSource()
  }

  /** Stops the camera.  */
  override fun onPause() {
    super.onPause()
    preview?.stop()
  }

  public override fun onDestroy() {
    super.onDestroy()
    if (cameraSource != null) {
      cameraSource?.release()
    }
  }

  private val requiredPermissions: Array<String?>
    get() = try {
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

  private fun allPermissionsGranted(): Boolean {
    for (permission in requiredPermissions) {
      if (!isPermissionGranted(this, permission)) {
        return false
      }
    }
    return true
  }

  private val runtimePermissions: Unit
    get() {
      val allNeededPermissions: MutableList<String?> = ArrayList()
      for (permission in requiredPermissions) {
        if (!isPermissionGranted(this, permission)) {
          allNeededPermissions.add(permission)
        }
      }
      if (allNeededPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(
          this,
          allNeededPermissions.toTypedArray(),
          PERMISSION_REQUESTS
        )
      }
    }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    Log.i(TAG, "Permission granted!")
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel)
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  companion object {
    private const val OBJECT_DETECTION = "Object Detection"

    private const val POSE_DETECTION = "Biceps"
    private const val POSE_DETECTION2 = "squads"
    private const val POSE_DETECTION3 = "triceps"
    private const val POSE_DETECTION4 = "Shoulder"

    private const val TAG = "LivePreviewActivity"
    private const val PERMISSION_REQUESTS = 1
    private fun isPermissionGranted(
      context: Context,
      permission: String?
    ): Boolean {
      if (ContextCompat.checkSelfPermission(context, permission!!)
        == PackageManager.PERMISSION_GRANTED
      ) {
        Log.i(TAG, "Permission granted: $permission")
        return true
      }
      Log.i(TAG, "Permission NOT granted: $permission")
      return false
    }
  }
}
