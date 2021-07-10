
package com.google.mlkit.vision.demo.kotlin.posedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.media.RingtoneManager
import android.text.TextUtils
import android.widget.Toast
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.InferenceInfoGraphic
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import java.util.Locale
import kotlin.math.atan2

/** Draw the detected pose in preview.  */
class PoseGraphic internal constructor(
  overlay: GraphicOverlay,
  private val pose: Pose,
  private val showInFrameLikelihood: Boolean
) :
  GraphicOverlay.Graphic(overlay) {
  private val leftPaint: Paint
  private val rightPaint: Paint
    val notification2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val r2 = RingtoneManager.getRingtone(applicationContext, notification2)
  private val whitePaint: Paint
  private val tipPaint: Paint
  override fun draw(canvas: Canvas) {
    val landmarks =
      pose.allPoseLandmarks
    if (landmarks.isEmpty()) {
      return
    }
    // Draw all the points
    for (landmark in landmarks) {
      drawPoint(canvas, landmark.position, whitePaint)
      if (showInFrameLikelihood) {
        canvas.drawText(
          String.format(Locale.US, "%.2f", landmark.inFrameLikelihood),
          translateX(landmark.position.x),
          translateY(landmark.position.y),
          whitePaint
        )
      }
    }
    val leftShoulder =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_SHOULDER)
    val rightShoulder =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_SHOULDER)
    val leftElbow =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_ELBOW)
    val rightElbow =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ELBOW)
    val leftWrist =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_WRIST)
    val rightWrist =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_WRIST)
    val leftHip =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_HIP)
    val rightHip =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HIP)
    val leftKnee =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_KNEE)
    val rightKnee =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_KNEE)
    val leftAnkle =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_ANKLE)
    val rightAnkle =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_ANKLE)
    val leftPinky =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_PINKY)
    val rightPinky =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_PINKY)
    val leftIndex =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_INDEX)
    val rightIndex =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_INDEX)
    val leftThumb =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_THUMB)
    val rightThumb =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_THUMB)
    val leftHeel =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_HEEL)
    val rightHeel =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_HEEL)
    val leftFootIndex =
      pose.getPoseLandmark(PoseLandmark.Type.LEFT_FOOT_INDEX)
    val rightFootIndex =
      pose.getPoseLandmark(PoseLandmark.Type.RIGHT_FOOT_INDEX)
    /////////////////////

    val angle24_26_28 = getAngle(leftShoulder, leftElbow, leftWrist)
      val h2= leftShoulder!!.position.y
      lineOneText=h2.toString()
//bedaayaaaaa
if(angle24_26_28>=160 && angle24_26_28<=180)
{
    lineTwoText="Start UPPPPPP"
}



      val anglesabta=20
      val anglesabta2=32
//wna btl3
if(angle24_26_28<160&& angle24_26_28>=20)
{


    lineTwoText="Upiiinggggggggggg"
if(h2<150) {

    isDown2 = true
    r2.play()
}
else {
isDown2=false
}
    if(isDown2==false) {


        if (angle24_26_28 >= anglesabta && angle24_26_28 <= anglesabta2) {
            lineTwoText = "excellent"
            isUp2 = true

        }
    }


}
if(isUp2==true) {


    if ((angle24_26_28 >anglesabta2) ) {
        lineTwoText = "Downing"
        if (angle24_26_28 <= 180 && angle24_26_28 >= 160) {


            upCountbiceps++
            isUp2 = false

        }
    }
}


      val currentHeight =  (leftShoulder!!.position.y)
    val currentHeight2=  (leftWrist!!.position.y)
var wristHeight=0f;
      //minSize2 = (currentHeight2-currentHeight)

    drawText(canvas, lineOneText,1)
    drawText(canvas, lineTwoText,2)
    drawText(canvas, "count："+upCountbiceps.toString(), 3)
    /////////////////////
    drawLine(canvas, leftShoulder!!.position, rightShoulder!!.position, whitePaint)
    drawLine(canvas, leftHip!!.position, rightHip!!.position, whitePaint)
    // Left body
    drawLine(canvas, leftShoulder.position, leftElbow!!.position, leftPaint)
    drawLine(canvas, leftElbow.position, leftWrist!!.position, leftPaint)
    drawLine(canvas, leftShoulder.position, leftHip.position, leftPaint)
    drawLine(canvas, leftHip.position, leftKnee!!.position, leftPaint)
    drawLine(canvas, leftKnee.position, leftAnkle!!.position, leftPaint)
    drawLine(canvas, leftWrist.position, leftThumb!!.position, leftPaint)
    drawLine(canvas, leftWrist.position, leftPinky!!.position, leftPaint)
    drawLine(canvas, leftWrist.position, leftIndex!!.position, leftPaint)
    drawLine(canvas, leftAnkle.position, leftHeel!!.position, leftPaint)
    drawLine(canvas, leftHeel.position, leftFootIndex!!.position, leftPaint)
    // Right body
    drawLine(canvas, rightShoulder.position, rightElbow!!.position, rightPaint)
    drawLine(canvas, rightElbow.position, rightWrist!!.position, rightPaint)
    drawLine(canvas, rightShoulder.position, rightHip.position, rightPaint)
    drawLine(canvas, rightHip.position, rightKnee!!.position, rightPaint)
    drawLine(canvas, rightKnee.position, rightAnkle!!.position, rightPaint)
    drawLine(canvas, rightWrist.position, rightThumb!!.position, rightPaint)
    drawLine(canvas, rightWrist.position, rightPinky!!.position, rightPaint)
    drawLine(canvas, rightWrist.position, rightIndex!!.position, rightPaint)
    drawLine(canvas, rightAnkle.position, rightHeel!!.position, rightPaint)
    drawLine(canvas, rightHeel.position, rightFootIndex!!.position, rightPaint)
  }

  fun reInitParams(){
    lineOneText = ""
    lineTwoText = ""
    shoulderHeight = 0f
    minSize = 0f
    isCount = false
    isUp2 = false
    isDown2 = false
    upCountbiceps = 0
    downCount = 0
  }

  fun drawPoint(canvas: Canvas, point: PointF?, paint: Paint?) {
    if (point == null) {
      return
    }
    canvas.drawCircle(
      translateX(point.x),
      translateY(point.y),
      DOT_RADIUS,
      paint!!
    )
  }

  fun drawLine(
    canvas: Canvas,
    start: PointF?,
    end: PointF?,
    paint: Paint?
  ) {
    if (start == null || end == null) {
      return
    }
    canvas.drawLine(
      translateX(start.x), translateY(start.y), translateX(end.x), translateY(end.y), paint!!
    )
  }

  fun drawText(canvas: Canvas, text:String, line:Int) {
    if (TextUtils.isEmpty(text)) {
      return
    }
    canvas.drawText(text, InferenceInfoGraphic.TEXT_SIZE*0.5f, InferenceInfoGraphic.TEXT_SIZE*3 + InferenceInfoGraphic.TEXT_SIZE*line, tipPaint)
  }

  companion object {
    private const val DOT_RADIUS = 8.0f
    private const val IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f

    var isUp2 = false //是否起身
    var isDown2 = false //是否下蹲
    var flag=false
    var upCountbiceps = 0 //up times
    var downCount = 0 //down times
    var isCount = false //is counting
    var lineOneText = ""
    var lineTwoText = ""
    var shoulderHeight = 0f //
    var minSize = 0f //最小移动单位，避免测算抖动出现误差
    var lastHeight = 0f
      var minSize2=0f
      var type="bi-ceps"
  }

  init {
    whitePaint = Paint()
    whitePaint.color = Color.WHITE
    whitePaint.textSize = IN_FRAME_LIKELIHOOD_TEXT_SIZE
    leftPaint = Paint()
    leftPaint.color = Color.GREEN
    rightPaint = Paint()
    rightPaint.color = Color.YELLOW

    tipPaint = Paint()
    tipPaint.color = Color.WHITE
    tipPaint.textSize = 40f
  }

  fun getAngle(firstPoint: PoseLandmark?, midPoint: PoseLandmark?, lastPoint: PoseLandmark?): Double {
    var result = Math.toDegrees(atan2(1.0*lastPoint!!.getPosition().y - midPoint!!.getPosition().y,
            1.0*lastPoint.getPosition().x - midPoint.getPosition().x)
            - atan2(firstPoint!!.getPosition().y - midPoint.getPosition().y,
            firstPoint.getPosition().x - midPoint.getPosition().x))
    result = Math.abs(result) // Angle should never be negative
    if (result > 180) {
      result = 360.0 - result // Always get the acute representation of the angle
    }
    return result
  }
}
