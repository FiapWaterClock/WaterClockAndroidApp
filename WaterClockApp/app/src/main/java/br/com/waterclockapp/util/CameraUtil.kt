package br.com.waterclockapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CameraUtil(var activity: Activity, var surface: SurfaceView) {
    var cameraDevice:CameraDevice? = null


    companion object {
        private const val CAMERA_PERMISSION_CODE = 200
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

        fun hasCameraPermission(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED
        }

        fun requestCameraPermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, arrayOf(CAMERA_PERMISSION), CAMERA_PERMISSION_CODE)
        }

        fun shouldShowRequestPermission(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSION)
        }
    }

    private fun startCameraSession() {
        val cameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        if (cameraManager.cameraIdList.isEmpty()) return

        var firstCamera = cameraManager.cameraIdList[0]

        if (ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED) requestCameraPermission(activity)
        else return

        cameraManager.openCamera(firstCamera, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                var cameraCharacteristics = cameraManager.getCameraCharacteristics(camera.id)

                cameraCharacteristics[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]?.let { streamConfigurationMap ->
                    streamConfigurationMap.getOutputSizes(ImageFormat.YUV_420_888)?.let {
                        setRotationCamera(it.last(), cameraCharacteristics)
                        captureCamera(camera)
                    }
                }
            }

            override fun onDisconnected(camera: CameraDevice) {
            }

            override fun onError(camera: CameraDevice, error: Int) {
            }

        }, Handler { true })
    }

    private fun setRotationCamera(previewSize : Size, cameraCharacteristics: CameraCharacteristics) {
        val displayRotation = activity.windowManager.defaultDisplay.rotation
        val swappedDimensions = areDimensionsSwapped(displayRotation, cameraCharacteristics )
        val previewWidth = if(swappedDimensions) previewSize.height else previewSize.width
        val previewHeight = if(swappedDimensions) previewSize.width else previewSize.height
        surface.holder.setFixedSize(previewWidth, previewHeight)


    }

    private fun areDimensionsSwapped(displayRotation: Int, cameraCharacteristics: CameraCharacteristics): Boolean {
        var swapperDimension = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if ((cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) == 90) || (cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) == 270))
                    swapperDimension = true
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if ((cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) == 0) || (cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) == 180))
                    swapperDimension = true
            }
            else -> {
                // Invalid display rotation
            }
        }

        return swapperDimension
    }

    private fun captureCamera(cameraDevice: CameraDevice){
        val previewSurface = surface.holder.surface

        val captureCallback = object : CameraCaptureSession.StateCallback(){
            override fun onConfigureFailed(session: CameraCaptureSession) {

            }

            override fun onConfigured(session: CameraCaptureSession) {
                val previewRequestBuilder = cameraDevice.createCaptureRequest(
                        CameraDevice.TEMPLATE_PREVIEW).apply { addTarget(previewSurface) }
                session.setRepeatingRequest(previewRequestBuilder.build(), object : CameraCaptureSession.CaptureCallback(){
                }, Handler{true})
            }


        }
        cameraDevice.createCaptureSession(mutableListOf(previewSurface), captureCallback, Handler{true})
    }

    private fun imageCapture(){

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun teste(previewWidth: Int, previewHeight: Int, captureCallback: CameraCaptureSession.StateCallback){
        val imageReader = ImageReader.newInstance(previewWidth, previewHeight, ImageFormat.YUV_420_888, 2)
        imageReader.setOnImageAvailableListener({

        }, Handler{true})

        val previewSurface = surface.holder.surface
        val recordingSurface = imageReader.surface

        val previewRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)?.apply {
            addTarget(previewSurface)
            addTarget(recordingSurface)
        }
        cameraDevice?.createCaptureSession(mutableListOf(previewSurface, recordingSurface), captureCallback, Handler{true})

    }

    fun showCamera() = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder?) {}

        override fun surfaceCreated(holder: SurfaceHolder?) {
            startCameraSession()
        }

    }


}