package com.darwindeveloper.MyPetsApp.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.BarcodeFormat
import java.util.ArrayList
import android.media.RingtoneManager
import android.media.Ringtone
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.DataUserActivity
import com.darwindeveloper.MyPetsApp.api.Constants
import com.darwindeveloper.MyPetsApp.api.WebApiClient
import com.darwindeveloper.MyPetsApp.api.WebService
import com.darwindeveloper.MyPetsApp.api.responses.QRResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by DARWIN MOROCHO on 30/9/2017.
 */
class QrFragment : Fragment(), MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener, Callback<QRResponse> {


    private var progressDialog: ProgressDialog? = null
    private var est_id: String? = null
    override fun onResponse(call: Call<QRResponse>?, response: Response<QRResponse>?) {
        val res = response!!.body()
        if (res != null) {
            if (res.status == 200) {
                progressDialog!!.dismiss()
                val user = res.user

                val editor = preferences!!.edit()
                editor.putString(Constants.USER_ID, user!!.user_id)
                editor.putString(Constants.USER_NAME, user.name)
                editor.putString(Constants.USER_LAST_NAME, user.last_name)
                editor.putString(Constants.USER_EMAIL, user.email)
                editor.putString(Constants.USER_NUMBRE_IDENTIFICATION, user.cedula)
                editor.putString(Constants.USER_PHOTO, user.foto)
                editor.putString(Constants.USER_API_TOKEN, user.api_token)
                editor.putString(Constants.ESTABLECIMIENTO_ID, est_id)
                editor.apply()

                val intent = Intent(context, DataUserActivity::class.java)
                intent.putExtra(DataUserActivity.USER_ID, user.user_id)
                intent.putExtra(DataUserActivity.API_TOKEN, user.api_token)
                intent.putExtra(DataUserActivity.EST_ID, est_id)
                intent.putExtra(DataUserActivity.EST_NOMBRE, res.establecimiento!!.nombre_establecimiento)
                intent.putExtra(DataUserActivity.EST_LOGO, res.establecimiento!!.icono)
                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                activity.finishAffinity()
            } else {
                progressDialog!!.dismiss()
                Toast.makeText(context, res.msg, Toast.LENGTH_SHORT).show()
                mScannerView!!.resumeCameraPreview(this);
            }
        } else {
            progressDialog!!.dismiss()
            Toast.makeText(context, "QR no valido", Toast.LENGTH_SHORT).show()
            mScannerView!!.resumeCameraPreview(this);
        }
    }

    override fun onFailure(call: Call<QRResponse>?, t: Throwable?) {
        progressDialog!!.dismiss()
        Toast.makeText(context, "QR no valido", Toast.LENGTH_SHORT).show()
        mScannerView!!.resumeCameraPreview(this);
    }

    override fun onDialogPositiveClick(dialog: DialogFragment?) {

        mScannerView!!.resumeCameraPreview(this);
    }

    var firebase_token: String? = null

    override fun handleResult(rawResult: Result?) {
        try {
            if (rawResult != null) {
                val jsonString = rawResult.getText()
                //val gson=Gson()
                val jo = JSONObject(jsonString)
                if (jo.getString("app") == "MyPetsApp") {
                    progressDialog = ProgressDialog.show(context, "Verificando QR", "Por favor espere...", false)
                    progressDialog!!.show()
                    // showMessageDialog("Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString())
                    val user_id = jo.getString("user_id")
                    est_id = jo.getString("est_id")

                    val ws = WebApiClient.client!!.create(WebService::class.java)
                    val mcall = ws.loginQR(user_id, est_id, firebase_token)
                    mcall.enqueue(this)

                } else {
                    mScannerView!!.resumeCameraPreview(this)
                }
            }
        } catch (e: Exception) {
            // Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            mScannerView!!.resumeCameraPreview(this)
        }


    }

    fun showMessageDialog(message: String) {
        val fragment = MessageDialogFragment.newInstance("Scan Results", message, this)
        fragment.show(activity.supportFragmentManager, "scan_results")
    }

    override fun onCameraSelected(cameraId: Int) {
        mCameraId = cameraId;
        mScannerView!!.startCamera(mCameraId);
        mScannerView!!.setFlash(mFlash);
        mScannerView!!.setAutoFocus(mAutoFocus);
    }

    override fun onFormatsSaved(selectedIndices: java.util.ArrayList<Int>?) {

    }

    private val FLASH_STATE = "FLASH_STATE"
    private val AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE"
    private val SELECTED_FORMATS = "SELECTED_FORMATS"
    private val CAMERA_ID = "CAMERA_ID"
    private var mScannerView: ZXingScannerView? = null
    private var mFlash: Boolean = false
    private var mAutoFocus: Boolean = false
    private var mSelectedIndices: ArrayList<Int>? = null
    private var mCameraId = -1

    private var preferences: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        firebase_token = preferences?.getString(Constants.FIREBASE_TOKEN, null)


        mScannerView = ZXingScannerView(getActivity())
        if (savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false)
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true)
            mSelectedIndices = savedInstanceState.getIntegerArrayList(SELECTED_FORMATS)
            mCameraId = savedInstanceState.getInt(CAMERA_ID, -1)
        } else {
            mFlash = false
            mAutoFocus = true
            mSelectedIndices = null
            mCameraId = -1
        }
        setupFormats()
        return mScannerView


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera(mCameraId)
        mScannerView!!.setFlash(mFlash)
        mScannerView!!.setAutoFocus(mAutoFocus)
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera();

    }

    fun closeMessageDialog() {
        closeDialog("scan_results")
    }

    fun closeFormatsDialog() {
        closeDialog("format_selector")
    }

    fun closeDialog(dialogName: String) {
        val fragmentManager = activity.supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(dialogName) as DialogFragment
        fragment.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            outState.putBoolean(FLASH_STATE, mFlash)
            outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus)
            outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices)
            outState.putInt(CAMERA_ID, mCameraId)
        }

    }


    fun setupFormats() {
        val formats = ArrayList<BarcodeFormat>()
        if (mSelectedIndices == null || mSelectedIndices!!.isEmpty()) {
            mSelectedIndices = ArrayList<Int>()
            for (i in ZXingScannerView.ALL_FORMATS.indices) {
                mSelectedIndices!!.add(i)
            }
        }

        for (index in mSelectedIndices!!.indices) {
            formats.add(ZXingScannerView.ALL_FORMATS[index])
        }



        if (mScannerView != null) {
            mScannerView!!.setFormats(formats)
        }
    }

    override fun onStop() {
        if (mScannerView != null) {
            mScannerView!!.stopCamera();

        }
        super.onStop()
    }

}