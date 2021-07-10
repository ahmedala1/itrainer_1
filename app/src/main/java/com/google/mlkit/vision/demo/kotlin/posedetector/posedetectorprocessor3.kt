
package com.google.mlkit.vision.demo.kotlin.posedetector

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.VisionImageProcessor
import com.google.mlkit.vision.demo.kotlin.VisionProcessorBase
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptions

/** A processor to run pose detector.  */
class posedetectorprocessor3(
        context: Context,
        options: PoseDetectorOptions,
        private val showInFrameLikelihood: Boolean
) :
        VisionProcessorBase<Pose>(context), VisionImageProcessor {
    private val detector: PoseDetector
    override fun stop() {
        super.stop()
        detector.close()
    }

    override fun detectInImage(image: InputImage): Task<Pose> {
        return detector.process(image)
    }

    override fun onSuccess(
            pose: Pose,
            graphicOverlay: GraphicOverlay
    ) {
        graphicOverlay.add(triceps(graphicOverlay, pose, showInFrameLikelihood))
    }

    override fun onFailure(e: Exception) {
        Log.e(
                TAG,
                "Pose detection failed!",
                e
        )
    }

    companion object {
        private const val TAG = "PoseDetectorProcessor"
    }

    init {
        detector = PoseDetection.getClient(options)
    }
}
