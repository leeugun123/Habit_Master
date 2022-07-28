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
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.MainActivity
import org.techtown.habit_master.databinding.ActivityUploadBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityUploadBinding

    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청 코드
    lateinit var curPhotoPath: String //문자열 형태의 사진 경로 값

    private var filePath : Uri? = null
    //파일 Path

    var uid : String? = null
    //카카오 uid

    var habitTitle : String? = null
    var habitDate : String? = null
    //이전 intent에서 Title 가져오기

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        habitTitle = intent.getStringExtra("habitTitle") //습관 제목
        habitDate = intent.getStringExtra("habitDate") // 오늘 날짜 가져오기
        setPermission()//권한 허용

        mBinding.takePicture.setOnClickListener{

            takeCapture()
            // 기본 카메라 앱을 실행하여 사진 촬영
        }

        mBinding.uploadButton.setOnClickListener{

            uploadFile()
            //이미지 업로드
            Toast.makeText(this@UploadActivity,"이미지가 업로드 되었습니다",Toast.LENGTH_SHORT).show()

            finish()
            //액티비티 꺼짐

        }//이미지 업로드


    }

    private fun uploadFile() {

        if(filePath != null){

            val storage : FirebaseStorage = FirebaseStorage.getInstance()

            val formatter = SimpleDateFormat("yyyyMMHH_mmss")
            val now = Date()
            val filename : String = formatter.format(now) + ".png"
            //파일 이름 만들기

            var storageRef : StorageReference = storage.getReferenceFromUrl("gs://habitcertify.appspot.com")
                .child("images/" + filename)
            //storage에 업로드 파일 만들어 넣기

            storageRef.putFile(filePath!!)
            //파이어베이스에 업로드

            //파일 업로드까지 시간이 좀 걸림, 따라서 handler를 이용하여 늦게 처리해줌
            Handler().postDelayed({
                bringUri(filename)
            },3000)



        }

    }

    private fun bringUri(filename : String) {

        val storage : FirebaseStorage = FirebaseStorage.getInstance()

        var writeDatabase = FirebaseDatabase.getInstance()
        var databaseReference = writeDatabase.getReference()
        //데이터 쓰기


        UserApiClient.instance.me { user, error ->

            if (error != null) {
                //Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {

                uid = user.id.toString()
            }

            //storage -> Realtime Database로 옮기기
            val storageRef = storage.getReferenceFromUrl("gs://habitcertify.appspot.com")
            storageRef.child("images/" + filename).downloadUrl.addOnSuccessListener {

                var share = Share(it.toString(),mBinding.description.text.toString())

                //Share 클래스 자체로 서버로 업로드한다.

                //제목은 이전 액티비티에서 intent로 가져온다.
                databaseReference.child("Habits").child(habitTitle.toString()).child("date")
                    .child(habitDate.toString()).child(uid.toString())
                    .setValue(share)

                Log.e(TAG,"성공하였습니다.")

            }//성공시
                .addOnFailureListener{

                    Log.e(TAG,"실패하였습니다.")

                }

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


                    takePictrueIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictrueIntent,REQUEST_IMAGE_CAPTURE)

                }
            }
        }
    }

    //사진을 찍기 전에 미리 호출된다.
    private fun createImageFile(): File {

        val timestamp : String = SimpleDateFormat("yyyyMMddhhmmss").format(Date())
        //시간마다 파일을 다르게함



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

            //Log.e("TAG",curPhotoPath)

            val bitmap : Bitmap
            val file = File(curPhotoPath)

            filePath = Uri.fromFile(file)
            //filePath 경로 설정

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