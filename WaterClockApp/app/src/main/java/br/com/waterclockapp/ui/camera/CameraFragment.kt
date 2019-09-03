package br.com.waterclockapp.ui.camera

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.system.Os.close
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import br.com.waterclockapp.R
import br.com.waterclockapp.ui.HomeActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer

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
    var backgroundHandler: Handler? = null
    var backgroundThread: HandlerThread? = null

    private lateinit var textureViewCamera : TextureView
    private lateinit var buttonSavaCamera : Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        textureViewCamera = view.findViewById(R.id.textureViewCamera)
        buttonSavaCamera = view.findViewById(R.id.buttonSavaCamera)
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
        if(cameraDevice == null)
            return
        val cameraManager: CameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val characteristics = cameraManager.getCameraCharacteristics(cameraDevice?.id)
        val jpegSizes : Array<Size>? =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG)

        var width = 640
        var height = 480
        if(jpegSizes != null && jpegSizes.size > 0){
            width = jpegSizes[0].width
            height = jpegSizes[0].height
        }

        val imageReader: ImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
        val outputSurface = ArrayList<Surface>(2)
        outputSurface.add(imageReader.surface)
        outputSurface.add(Surface(textureViewCamera.surfaceTexture))
        val captureBuilder : CaptureRequest.Builder? = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureBuilder?.addTarget(imageReader.surface)
        captureBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        captureBuilder?.set(CaptureRequest.JPEG_ORIENTATION, rotation?.let { ORIENTATIONS.get(it) })
        val tsLong : Long = System.currentTimeMillis()/ 1000
        val ts : String = tsLong.toString()

        file = File("${Environment.getExternalStorageDirectory()}/$ts.jpg")

        val readerListener : ImageReader.OnImageAvailableListener = ImageReader.OnImageAvailableListener {
            var image: Image = imageReader.acquireLatestImage()
            val byteBuffer: ByteBuffer = image.planes[0].buffer
            val bytes  = byteArrayOfInts(byteBuffer.capacity())
            byteBuffer.get(bytes)
            try {
                save(bytes)
            }catch (e: IOException){
                e.printStackTrace()
            }finally {
                image?.let { it.close() }
            }


        }
        imageReader.setOnImageAvailableListener(readerListener, backgroundHandler)

        val captureListener : CameraCaptureSession.CaptureCallback = object : CameraCaptureSession.CaptureCallback() {
            override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                super.onCaptureCompleted(session, request, result)
                Toast.makeText(activity, "Saved", Toast.LENGTH_LONG).show()
                try{
                    createCameraPreview()
                }catch (e:CameraAccessException){
                    e.printStackTrace()
                }
            }
        }
        cameraDevice?.createCaptureSession(outputSurface, object: CameraCaptureSession.StateCallback(){
            override fun onConfigureFailed(session: CameraCaptureSession) {
                try {
                    session.capture(captureRequestBuilder.build(), captureListener, backgroundHandler)

                }catch (e: CameraAccessException){
                    e.printStackTrace()
                }
            }

            override fun onConfigured(session: CameraCaptureSession) {

            }

        }, backgroundHandler)

    }

    private fun save(bytes: ByteArray) {
        val outputStream: OutputStream = FileOutputStream(file)
        outputStream.write(bytes)
        outputStream.close()
    }

    fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if(textureViewCamera.isAvailable){
            openCamera()
        }else {
            textureViewCamera.surfaceTextureListener = textureListener
        }

    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("Camera Background")
        backgroundThread?.start()
        backgroundHandler = Handler(backgroundThread?.looper)
    }

    override fun onPause() {
        stopBackgroundThread()
        super.onPause()
    }

    protected fun stopBackgroundThread() {
        if(backgroundThread != null){
            backgroundThread?.quitSafely()
            backgroundThread?.join()
        }
        backgroundThread = null
        backgroundHandler = null

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUESTCODECAMERA){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(activity, "Sorry, camera permission is necessary", Toast.LENGTH_LONG).show()
            }
        }
    }

}
