package org.techtown.habit_master.Share


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import org.techtown.habit_master.databinding.ActivityUploadBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : AppCompatActivity() {

    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = firebaseStorage.getReference()
    //파이어베이스 storage 저장

    private lateinit var mBinding : ActivityUploadBinding

    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청 코드
    lateinit var curPhotoPath: String //문자열 형태의 사진 경로 값

    private lateinit var ImgUri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setPermission()//권한 허용

        mBinding.takePicture.setOnClickListener{

            takeCapture()// 기본 카메라 앱을 실행하여 사진 촬영
        }



    }

    //카메라 촬영 기능
    private fun takeCapture() {

        //기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictrueIntent ->
            takePictrueIntent.resolveActivity(packageManager)?.also{
                val photoFile : File? = try {
                    createImageFile()
                }catch (ex: IOException){
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "org.techtown.habit_master.fileprovider",
                        it
                    )

                    //ImgUri = photoURI

                    takePictrueIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictrueIntent,REQUEST_IMAGE_CAPTURE)

                }
            }
        }
    }

    private fun createImageFile(): File {

        val timestamp : String = SimpleDateFormat("yyyyMMddhhmmss").format(Date())
        //시간마다 파일을 다르게함

       // val riverRef = storageRef.child("habit_img/"+timestamp+".jpg")
      //  riverRef.putFile(ImgUri)
        //파이어 베이스에 이미지 업로드


        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //?은 nullable 이 변수를 null을 허락해줌
        return File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir)
            .apply { curPhotoPath  = absolutePath}
        //절대경로로 설정




    }//이미지 파일 생성

    //테드 퍼미션 설정 기능
    private fun setPermission() {

        val permission = object : PermissionListener{

            override fun onPermissionGranted() {//설정해놓은 위험권한들이 허용 되었을 경우 이 곳을 수행함

                Toast.makeText(this@UploadActivity,"권한이 허용되었습니다.",Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {//설정해놓은 위험권한 중 거부를 한 경우
                Toast.makeText(this@UploadActivity,"권한이 거부되었습니다.",Toast.LENGTH_SHORT).show()
            }

        }

        //Ted permission을 이용하여 카메라 권한 허용
        TedPermission.create()
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
            .check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //startActivityForResult을 통해서 기본 카메라 앱으로부터 받아온 사진 결과 값임
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){

            val bitmap : Bitmap
            val file = File(curPhotoPath)

            if(Build.VERSION.SDK_INT < 28){
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,Uri.fromFile(file))
                mBinding.uploadImg.setImageBitmap(bitmap)

            }//안드로이드 9.0 버전보다 낮을 경우
            else{

                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )

                bitmap = ImageDecoder.decodeBitmap(decode)
                mBinding.uploadImg.setImageBitmap(bitmap)

            }//더 높을 경우

            //안드로이드 버전에 따라 다르게 처리함
            savePhoto(bitmap)

        }//이미지를 성공적으로 가져왔다면


    }

    //갤러리에 저장
    private fun savePhoto(bitmap: Bitmap) {

        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/"
        //사진 폴더로 저장하기 위한 경로 설정

        val timestamp : String = SimpleDateFormat("yyyyMMddhhmmss").format(Date())

        val fileName = "${timestamp}.jpeg"

        val folder = File(folderPath)

        if(!folder.isDirectory){

            folder.mkdirs()//make directory 해당 경로 폴더 자동으로 만들기

        }//현재 해당 경로에 폴더가 존재하는지 않는다면

        //실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
        Toast.makeText(this,"사진이 앨범에 저장되었습니다",Toast.LENGTH_SHORT).show()

    }


}