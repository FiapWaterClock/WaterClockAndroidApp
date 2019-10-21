package br.com.waterclockapp.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.waterclockapp.R;
import br.com.waterclockapp.ui.home.HomeContract;
import br.com.waterclockapp.util.ConstantsKt;

public class CameraFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 756;
    private Size previewSize;
    private Size jpegSizes[] = null;

    private static final int REQUEST_CODE = 200;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String CAPTURE_IMAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private HomeContract.View viewHome;

    private TextureView textureView;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private Button getPicture;
    private Button getGallery;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    public CameraFragment(HomeContract.View viewHome) {
        this.viewHome = viewHome;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textureView = (TextureView) view.findViewById(R.id.surfaceViewCamera);
        textureView.setSurfaceTextureListener(surfaceTextureListener);

        getPicture = view.findViewById(R.id.buttonSavaCamera);
        getPicture.setOnClickListener(v -> {
            getPicture();
        });

        getGallery = view.findViewById(R.id.buttonOpenGallery);
        getGallery.setOnClickListener( v -> getGallery());
    }

    private void getGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mineTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mineTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
        //openCamera();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    //
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                    showDialogSend(bitmap);
                    Toast.makeText(getActivity(), "PEGOU FOTO " + bitmap.toString(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
        openCamera();

    }

    private void getPicture() {
        if(cameraDevice == null) return;
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            if(characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640, height = 480;
            if(jpegSizes != null && jpegSizes.length > 0){
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final  CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            ImageReader.OnImageAvailableListener imageAvailableListener=new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (Exception ee) {
                    }
                    finally {
                        if(image!=null)
                            image.close();
                    }
                }
                void save(byte[] bytes)
                {
                    File file12=getOutputMediaFile();
                    OutputStream outputStream=null;
                    try
                    {
                        outputStream= new FileOutputStream(file12);
                        outputStream.write(bytes);

                        Toast.makeText(getActivity(), "PEGOU FOTO", Toast.LENGTH_LONG).show();
                        Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        showDialogSend(image);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }finally {
                        try {
                            if (outputStream != null)
                                outputStream.close();
                        }catch (Exception e){}
                    }
                }
            };

            HandlerThread handlerThread=new HandlerThread("takepicture");
            handlerThread.start();
            final Handler handler=new Handler(handlerThread.getLooper());
            reader.setOnImageAvailableListener(imageAvailableListener,handler);
            final CameraCaptureSession.CaptureCallback  previewSSession=new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    startCamera();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try
                    {
                        session.capture(captureBuilder.build(),previewSSession,handler);
                    }catch (Exception e)
                    {

                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            },handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }



    //OPEN CAMERA
    public void openCamera(){

        if((ContextCompat.checkSelfPermission(getActivity(), CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(getActivity(), CAPTURE_IMAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED)) requestCameraPermission();
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewSize = (map != null ? map.getOutputSizes(SurfaceTexture.class) : new Size[0])[0];
            manager.openCamera(cameraId, stateCallback, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA_PERMISSION,
                CAPTURE_IMAGE_PERMISSION}, REQUEST_CODE);
    }

    //SURFACE TEXTURE LISTENER
    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startCamera();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (cameraDevice != null) cameraDevice.close();

    }

    void startCamera(){
        if(cameraDevice == null || !textureView.isAvailable() || previewSize == null) return;

        SurfaceTexture texture = textureView.getSurfaceTexture();
        if(texture == null) return;
        texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        Surface surface = new Surface(texture);
        try{
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    previewSession = session;
                    try{
                        getChangedPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void getChangedPreview() throws Exception{
        if(cameraDevice == null) return;
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("changed preview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "WaterClock");
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()) return null;
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "WC_"+timeStamp+".jpg");

        System.out.println("SAVE WITH SUCCESS");
        return mediaFile;
    }

    private void showDialogSend(Bitmap image){
        Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.send_image_dialog);
        dialog.setCancelable(false);
        ((ImageView) dialog.findViewById(R.id.imageViewSendPhoto)).setImageBitmap(image);
        ((TextView) dialog.findViewById(R.id.textViewDialogQuestion)).setText(ConstantsKt.DIALOG_SEND_IMAGE);
        dialog.show();

        dialog.findViewById(R.id.buttonNoChoose).setOnClickListener(v-> dialog.dismiss());
        dialog.findViewById(R.id.buttonYesChoose).setOnClickListener(v-> {
            Toast.makeText(getContext(), "ENVIADO", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        } );
    }


    public void enableNavigation(Boolean key){
        viewHome.enableNavigation(key);
    }

}
