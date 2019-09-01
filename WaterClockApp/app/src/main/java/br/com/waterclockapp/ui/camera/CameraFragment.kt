package br.com.waterclockapp.ui.camera

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.waterclockapp.R
import br.com.waterclockapp.ui.HomeActivity
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.util.*
import java.util.jar.Manifest

class CameraFragment : Fragment() {

    private val REQUESTCODECAMERA: Int = 101
    val ORIENTATIONS : SparseIntArray  = SparseIntArray().apply{
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)

    }


    private lateinit var cameraId: String
    var cameraDevice:CameraDevice? = null
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var captureRequest: CaptureRequest
    lateinit var captureRequestBuilder:CaptureRequest.Builder
    private lateinit var size: Size
    private lateinit var imageReader: ImageReader
    private lateinit var file: File
    lateinit var backgroundHandler: Handler
    lateinit var backgroundThread: HandlerThread





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textureViewCamera.surfaceTextureListener = textureListener
        buttonSavaCamera.setOnClickListener {
            takePicture()
        }
    }

    private val textureListener : TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
           try {
               openCamera()
           }catch (e:CameraAccessException){

           }
        }
    }

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback(){
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }

    }

    private fun createCameraPreview() {
        val texture : SurfaceTexture = textureViewCamera.surfaceTexture
        texture.setDefaultBufferSize(size.width, size.height)
        val surface: Surface = Surface(texture)

        captureRequestBuilder = (cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW) ?: captureRequestBuilder.addTarget(surface)) as CaptureRequest.Builder

        cameraDevice?.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback(){
            override fun onConfigureFailed(session: CameraCaptureSession) {
                if(cameraDevice == null)
                    return

                cameraCaptureSession = session
                updatePreview()
            }

            override fun onConfigured(session: CameraCaptureSession) {
                Toast.makeText(activity, "Configuration Changed", Toast.LENGTH_LONG).show()
            }



        }, null)

    }

    private fun updatePreview() {
        if(cameraDevice == null) return

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)

    }

    private fun openCamera() {

        val cameraActivity = activity
        if(cameraActivity == null || cameraActivity.isFinishing) return
        val cameraManager: CameraManager = cameraActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]
        val characteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
        val map : StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        size = map.getOutputSizes(SurfaceTexture::class.java)[0]
        if(ActivityCompat.checkSelfPermission(cameraActivity, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(cameraActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(HomeActivity(), arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUESTCODECAMERA)
            return
        }
        cameraManager.openCamera(cameraId, stateCallback, null)
    }

    private fun takePicture() {


    }

    override fun onResume() {
        super.onResume()
    }

}
