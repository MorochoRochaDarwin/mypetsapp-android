package com.darwindeveloper.MyPetsApp

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_dashboard.*
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import com.darwindeveloper.MyPetsApp.api.Constants
import com.squareup.picasso.Picasso
import android.content.Intent
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.util.Log
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.UploadResponse
import com.darwindeveloper.MyPetsApp.fragments.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class DashboardActivity : AppCompatActivity() {


    var user_id: String? = null
    var api_token: String? = null

    private val REQUEST_CODE_HOVER_PERMISSION = 1000
    private val REQUEST_WRITE_PERMISSION = 1001
    private val REQUEST_CAMERA_PERMISSION = 1002
    private val REQUEST_FINE_LOCATION_PERMISSION = 1003
    private val REQUEST_COARSE_LOCATION_PERMISSION = 1004
    private var mPermissionsRequested = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()

        val ars = Bundle()


        when (item.itemId) {

            R.id.navigation_home -> {
                ars.putString(MainFragment.USER_ID, user_id)
                ars.putString(MainFragment.USER_API_TOKEN, api_token)
                transaction.replace(R.id.dashboard_fragment_container, MainFragment.newInstance(ars))
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }


            R.id.navigation_inbox -> {
                transaction.replace(R.id.dashboard_fragment_container, MainNotificationsFragment())
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_petfriendly -> {
                transaction.replace(R.id.dashboard_fragment_container, PetFriendlyFragment())
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_profile -> {
                ars.putString(ProfileFragment.USER_ID, user_id)
                transaction.replace(R.id.dashboard_fragment_container, ProfileFragment.newInstance(ars))
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }

        }



        false
    }


    companion object {
        const val USER_ID = "DashboarAcivity.user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboard_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.mipmap.ic_launcher2)


        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString(Constants.USER_NAME, "null")
        val last_name = preferences.getString(Constants.USER_LAST_NAME, "null")
        val foto = preferences.getString(Constants.USER_PHOTO, null)
        user_id = intent.getStringExtra(USER_ID)
        api_token = preferences.getString(Constants.USER_API_TOKEN, null)
        dashboard_username.text = "$last_name $name"
        dashboard_user_id.text = "USUARIO\nID: $user_id"

        if (foto != null) {
            Picasso.with(this)
                    .load(Constants.WEB_URL + foto)
                    .placeholder(R.drawable.man)
                    .into(dashboard_profile_image);
        }

        dashboard_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val transaction = supportFragmentManager.beginTransaction()
        //pasamos los parametros
        val args = Bundle()
        args.putString(MainFragment.USER_ID, user_id)
        args.putString(MainFragment.USER_API_TOKEN, api_token)
        transaction.replace(R.id.dashboard_fragment_container, MainFragment.newInstance(args))
        transaction.commit()


        dashboard_profile_image.setOnClickListener {
            CropImage.activity().setAspectRatio(200, 200)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.dashboard, menu)

        // menu!!.findItem(android.R.id.home).setIcon(R.drawable.ic_menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_logout -> {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmación requerida")
                builder.setTitle("Cerrar sesión y salir de la app?")
                builder.setPositiveButton("SI", DialogInterface.OnClickListener { dialogInterface, i ->
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@DashboardActivity)
                    val edit = preferences.edit()
                    edit.putString(Constants.USER_ID, null)
                    edit.putString(Constants.USER_API_TOKEN, null)
                    edit.apply()
                    finishAffinity()
                })
                builder.setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                builder.create().show()

                return true
            }


            R.id.menu_settings -> {
                val i = Intent(this, PasswordActivity::class.java)
                i.putExtra(PasswordActivity.USER_ID, user_id)
                i.putExtra(PasswordActivity.API_TOKEN, api_token)
                startActivity(i)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkWritePermission(this)

        }


    }


    @TargetApi(Build.VERSION_CODES.M)
    fun checkWritePermission(context: Context) {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val result4 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (result != PackageManager.PERMISSION_GRANTED) {
            val per = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(per, REQUEST_WRITE_PERMISSION)
            return
        }

        if (result2 != PackageManager.PERMISSION_GRANTED) {
            val per = arrayOf(Manifest.permission.CAMERA)
            requestPermissions(per, REQUEST_CAMERA_PERMISSION)
            return
        }

        if (result3 != PackageManager.PERMISSION_GRANTED) {
            val per = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(per, REQUEST_FINE_LOCATION_PERMISSION)
            return
        }

        if (result4 != PackageManager.PERMISSION_GRANTED) {
            val per = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(per, REQUEST_COARSE_LOCATION_PERMISSION)
            return
        }


        // checkDrawOverlayPermission(this)
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun checkDrawOverlayPermission(context: Context) {
        // check if we already  have permission to draw over other apps
        if (Settings.canDrawOverlays(context)) {
            // code
        } else {
            // if not construct intent to request permission
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + packageName))
            // request permission via start activity for result
            startActivityForResult(intent, REQUEST_CODE_HOVER_PERMISSION)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //cuando se regresa de la actividad cropper
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {// si dodo es ok

                Toast.makeText(this, "subiendo imagen...", Toast.LENGTH_SHORT).show()

                val uri = result.uri//obtenemos la uri de la imagen recortada
                /*
                  //Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()
                  val storage = FirebaseStorage.getInstance()
                  val reference = storage.getReference();
                  val userImageRef = reference.child("users_images/user_${user_id}_photo.png")

                  var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                  bitmap = getResizedBitmap(bitmap, 500)

                  val baos = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                  val dataImage = baos.toByteArray()
                  val uploadTask = userImageRef.putBytes(dataImage)
                  uploadTask.addOnFailureListener(OnFailureListener {
                      Toast.makeText(this, "Error; intente mas tarde", Toast.LENGTH_SHORT).show()
                  }).addOnSuccessListener {
                      taskSnapshot ->
                      val downloadUrl = taskSnapshot.downloadUrl

                      Picasso.with(this)
                              .load(downloadUrl.toString())
                              .placeholder(R.drawable.man)
                              .into(dashboard_profile_image);


                  }
                 */


                val file = File(uri.path)


                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                // MultipartBody.Part is used to send also the actual file name
                val body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                // add another part within the multipart request
                val mapi_token = RequestBody.create(
                        MediaType.parse("ext/plain"), api_token)


                val muser_id = RequestBody.create(
                        MediaType.parse("ext/plain"), user_id)


                val ws = WebApiClient.client!!.create(WebService::class.java)
                val mcall = ws.subir_foto(muser_id, mapi_token, body)
                mcall.enqueue(object : Callback<UploadResponse> {
                    override fun onResponse(call: Call<UploadResponse>?, response: Response<UploadResponse>?) {


                        val res = response?.body()
                        if (res != null) {
                            if (res.status == 200) {

                                Picasso.with(this@DashboardActivity)
                                        .load(Constants.WEB_URL + res.url)
                                        .into(dashboard_profile_image)

                                val editor = PreferenceManager.getDefaultSharedPreferences(this@DashboardActivity).edit()
                                editor.putString(Constants.USER_PHOTO, res.url)
                                editor.apply()
                            }
                            Toast.makeText(this@DashboardActivity, res.msg, Toast.LENGTH_SHORT).show()

                        }

                    }

                    override fun onFailure(call: Call<UploadResponse>?, t: Throwable?) {
                        Log.i("errorimg",t?.message)
                        Toast.makeText(this@DashboardActivity, "Error; intente mas tarde", Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }


}
