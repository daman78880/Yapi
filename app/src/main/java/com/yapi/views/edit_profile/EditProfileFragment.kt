package com.yapi.views.edit_profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yapi.BuildConfig
import com.yapi.R
import com.yapi.common.*
import com.yapi.databinding.FragmentEditProfileBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.profile.ProfileData
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : DialogFragment(), View.OnClickListener {

    private var mediaDialog: AlertDialog?=null

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: ViewModelEditProfile by viewModels()

    @Inject
    lateinit var preferenceFile: PreferenceFile

    private val GALARYCode=123
    private val REQUEST_CAPTURE_IMAGE = 400
    var photoFile: File? = null

    companion object {
        fun newInstanceEditProfileScreen(
            title: String,
            profileBundle: ProfileData,
        ): EditProfileFragment {
            val args = Bundle()
            args.putString("11", title)
            args.putSerializable("profile_data", profileBundle)
            val fragment = EditProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkDeviceType()) {
            System.out.println("phone========tablet")
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialog)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new dialog
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        // Get the window of the dialog
        val window: Window = dialog.window!!

        // Set the dialog to be shown at the bottom of the screen
        window.setGravity(Gravity.RIGHT)

        var second_frame_height = preferenceFile.fetchStringValue("second_frame_height")
        var second_frame_width = preferenceFile.fetchStringValue("second_frame_width")
        Log.e("nefjkwnddfkewfwefe===", second_frame_height + "===" + second_frame_width)
     //   window.setLayout(second_frame_width.toInt(), second_frame_height.toInt())
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(LayoutInflater.from(requireContext()))
        binding.model = viewModel

        if (Constants.API_CALL_DEMO) {
            var profileData = requireArguments().getSerializable("profile_data") as ProfileData
            if (profileData != null) {
                viewModel.nameValue.set(profileData.name)
                viewModel.setNameValue.set(profileData.name)
                viewModel.userNameValue.set(profileData.user_name)
                viewModel.emailAddressValue.set(profileData.email)

                viewModel.aboutValue.set(profileData.about.toString().trim())

                binding.txtUserNameEditProfile.text = profileData.user_name
                if (!(profileData.mobile_number.toString().equals(""))
                    && profileData.mobile_number.toString() != null
                    && !(profileData.mobile_number.toString().equals("null"))
                ) {
                    var phoneNumber =
                        addSpaceBetweenPhoneMethod(profileData.mobile_number.toString())
                    viewModel.phoneNumberValue.set(phoneNumber)
                    viewModel.countryCodeValue.set(profileData.country_code.toString())
                }

                if (profileData.profile_pic_url != "" && profileData.profile_pic_url != null) {
                    viewModel.noImageOnlyNameVisible.set(false)
                    viewModel.photoUrl.set(profileData.profile_pic_url)
                   /* Glide.with(requireActivity())
                        .load(profileData.profile_pic_url)
                        .into(binding.imgProfilePicEditProfile)*/
                } else {
                    viewModel.noImageOnlyNameVisible.set(true)
                    // viewModel.showTopNameTag.set(profileData.name!!.substring(0, 1).uppercase(Locale.getDefault()))
                    viewModel.showTopNameTag.set(convertFromFullNameToTwoString(profileData.name!!))
                }
            }
        } else {
            viewModel.noImageOnlyNameVisible.set(false)
            viewModel.nameValue.set("Testing")
            viewModel.userNameValue.set("Demo")
            viewModel.emailAddressValue.set("Testing1@gmail.com")
            viewModel.aboutValue.set("About Data")
            var phoneNumber = addSpaceBetweenPhoneMethod("0123456789")
            viewModel.phoneNumberValue.set(phoneNumber)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun setTopLayoutMethod() {
        var rightMarginTopLayout = 0
        if (checkDeviceType()) {
            viewModel.checkTabValid = true
            rightMarginTopLayout =
                requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
            binding.layoutEditProfile.setBackgroundResource(R.drawable.et_drawable)
        } else {
            viewModel.checkTabValid = false
            rightMarginTopLayout = 0
            binding.layoutEditProfile.setBackgroundResource(0)
        }

        val layoutParams = binding.layoutEditProfile.layoutParams as LinearLayout.LayoutParams
        //  val newLayoutParams = toolbar.getLayoutParams()
        layoutParams.topMargin = 0
        layoutParams.leftMargin = 0
        layoutParams.rightMargin = rightMarginTopLayout
        binding.layoutEditProfile.layoutParams = layoutParams
    }

    private fun init() {
        showErrorUIObserver()
        phoneErrorObserver()
        dismissDialogMethod()
        setTopLayoutMethod()
        scrollEditTextMethod()

        binding.ivDrpArrow.setOnClickListener(this)
        binding.layoutUploadImageEditProfile.setOnClickListener(this)

        setPhoneMethod(binding.countryCodePickerEditProfile.selectedCountryCodeWithPlus)
        binding.apply {
            countryCodePickerEditProfile.setOnCountryChangeListener {
                Log.e("fefefewefwddf===", countryCodePickerEditProfile.selectedCountryCodeWithPlus)
                var selected_country_code =
                    countryCodePickerEditProfile.selectedCountryCodeWithPlus
                setPhoneMethod(selected_country_code!!)
            }
        }

        binding.etNumberEditProfile.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //binding.etNumberEditProfile.inputType=InputType.TYPE_CLASS_TEXT
                    //this is for backspace
                    if (binding.etNumberEditProfile.text.toString().length == 10 || binding.etNumberEditProfile.text.toString().length == 5) {
                        binding.etNumberEditProfile.setText(binding.etNumberEditProfile.text.toString()
                            .trim())
                    }
                } else {
                    //binding.etNumberEditProfile.inputType=InputType.TYPE_CLASS_NUMBER
                }
                return false
            }
        })

        binding.etNumberEditProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (binding.etNumberEditProfile.text.toString().length > 0) {
                    binding.txtErrorPhone.text = ""
                    changeBackgroundForError(binding.layoutNumberProfileEdit,
                        requireActivity().resources.getColor(
                            R.color.white),
                        requireActivity().resources.getColor(R.color.liteGrey))
                }

                if (binding.etNumberEditProfile.text.toString().length == 4) {
                    var first = binding.etNumberEditProfile.text.toString().substring(0, 3)
                    var last = binding.etNumberEditProfile.text.toString().substring(3, 4)

                    binding.etNumberEditProfile.setText(first + " " + last)
                    binding.etNumberEditProfile.setSelection(binding.etNumberEditProfile.text.toString().length)
                } else
                    if (binding.etNumberEditProfile.text.toString().length == 9) {
                        var first = binding.etNumberEditProfile.text.toString().substring(0, 8)
                        var last = binding.etNumberEditProfile.text.toString().substring(8, 9)

                        binding.etNumberEditProfile.setText(first + " " + last)

                    }
                binding.etNumberEditProfile.setSelection(binding.etNumberEditProfile.text.toString().length)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    fun setPhoneMethod(selectedCountryCode: String) {
        viewModel.countryCodeValue.set(selectedCountryCode.toString())
        binding.etNumberEditProfile.setSelection(binding.etNumberEditProfile.text.toString()
            .trim().length)

        //var profilePic = binding.countryCodePickerEditProfile.mImvFlag
        viewModel.countryCodeValue.set(selectedCountryCode.toString())
        binding.etNumberEditProfile.setSelection(binding.etNumberEditProfile.text.toString()
            .trim().length)
        var profilePic = binding.countryCodePickerEditProfile.mImvFlag
        //profilePic.invalidate()
        /* val imageBitmap = BitmapFactory.decodeResource(resources,
             binding.countryCodePickerEditProfile.selectedCountry.name)*/


        // profilePic.setDrawingCacheEnabled(true);
        //val imageBitmap: Bitmap = profilePic.getDrawingCache()


        /*var  resourceId= CountryUtils.getFlagDrawableResId(binding.countryCodePickerEditProfile.selectedCountry)
           val imageBitmap = BitmapFactory.decodeResource(resources, resourceId)

           val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
           var radiusValue = requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._3sdp)
           roundedBitmapDrawable.cornerRadius = radiusValue
           roundedBitmapDrawable.setAntiAlias(true)
           profilePic.setImageDrawable(roundedBitmapDrawable)*/

        /* binding.imgProfilePicEditProfile.setImageBitmap(imageBitmap)
         binding.imgProfilePicEditProfile.invalidate()*/
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivDrpArrow -> {
                binding.countryCodePickerEditProfile.showCountryCodePickerDialog()
                //  if(!(binding.countryCodePickerEditProfile.isShown)) {
                //showToastMessage("Hello")
                // binding.countryCodePickerEditProfile.
                //  }
            }
            R.id.layoutUploadImageEditProfile -> {
                // openCameraIntent()
                selectImage()
            }
        }
    }

    fun dismissDialogMethod() {
        viewModel.dismissDialogData.observe(requireActivity(), Observer {
            var data = it as Boolean
            if (data) {
                dismiss()
            }
        })
    }

    //For Scroll Yourself text
    fun scrollEditTextMethod() {
        binding.etAboutYourSelfEditProfile.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
               // if (binding.etAboutYourSelfEditProfile.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
            //    }
                return false
            }
        })
    }

    fun phoneErrorObserver() {
        viewModel.phoneErrorData.observe(requireActivity(), Observer {
            var data = it as SignInErrorData
            var editText: ConstraintLayout? = null
            var errorText: AppCompatTextView? = null
            if (data != null && data.message != "") {
                editText = binding.layoutNumberProfileEdit
                errorText = binding.txtErrorPhone
                errorText.text = data.message
                changeBackgroundForError(editText, requireActivity().resources.getColor(
                    R.color.error_box_color),
                    requireActivity().resources.getColor(R.color.error_border_color))
            } else {
                editText = binding.layoutNumberProfileEdit
                errorText = binding.txtErrorPhone
                errorText.text = ""
                changeBackgroundForError(editText, requireActivity().resources.getColor(
                    R.color.white),
                    requireActivity().resources.getColor(R.color.liteGrey))
            }
        })
    }

    fun showErrorUIObserver() {
        viewModel.errorData.observe(requireActivity(), Observer {
            var data = it as SignInErrorData

            if (data.fieldId == 1) {
                if (data.message != "") {
                    setSelectedDataMethod(binding.txtErrorName,
                        binding.etNameEditProfile,
                        data.message)
                } else {
                    setDefaultDataMethod(binding.txtErrorName, binding.etNameEditProfile)
                }
            } else
                if (data.fieldId == 2) {
                    if (data.message != "") {
                        setSelectedDataMethod(binding.txtErrorUserName,
                            binding.etUserNameEditProfile,
                            data.message)
                    } else {
                        setDefaultDataMethod(binding.txtErrorUserName,
                            binding.etUserNameEditProfile)
                    }
                } else
                    if (data.fieldId == 3) {
                        if (data.message != "") {
                            setSelectedDataMethod(binding.txtErrorEmail,
                                binding.etEmailEditProfile,
                                data.message)
                        } else {
                            setDefaultDataMethod(binding.txtErrorEmail,
                                binding.etEmailEditProfile)
                        }
                    } else
                        if (data.fieldId == 4) {
                            /*  editText=binding.layoutNumberProfileEdit
                              errorText=binding.txtErrorPhone*/

                        } else
                            if (data.fieldId == 5) {
                                if (data.message != "") {
                                    setSelectedDataMethod(binding.txtErrorAbout,
                                        binding.etAboutYourSelfEditProfile,
                                        data.message)
                                } else {
                                    setDefaultDataMethod(binding.txtErrorAbout,
                                        binding.etAboutYourSelfEditProfile)
                                }

                            }

            /*      if(data!=null && data.message.isNotEmpty())
                  {
                      errorText!!.setText(data.message)
                      changeBackgroundForEditError(editText!!,requireActivity().resources.getColor(
                          R.color.error_box_color),
                          requireActivity().resources.getColor(R.color.error_border_color))
                  }else {
                      if (data.fieldId != 0) {
                          errorText!!.setText("")
                          changeBackgroundForEditError(editText!!, requireActivity().resources.getColor(
                              R.color.white),
                              requireActivity().resources.getColor(R.color.liteGrey))
                      }


                  }*/
        })
    }

    fun setSelectedDataMethod(
        txtErrorName: AppCompatTextView,
        etNameEditProfile: AppCompatEditText,
        message: String,
    ) {
        txtErrorName.text = message
        changeBackgroundForEditError(etNameEditProfile, requireActivity().resources.getColor(
            R.color.error_box_color),
            requireActivity().resources.getColor(R.color.error_border_color))
    }

    fun setDefaultDataMethod(
        txtErrorName: AppCompatTextView,
        etNameEditProfile: AppCompatEditText,
    ) {
        txtErrorName.text = ""
        changeBackgroundForEditError(etNameEditProfile, requireActivity().resources.getColor(
            R.color.white),
            requireActivity().resources.getColor(R.color.liteGrey))
    }


    private fun openCameraIntent() {
        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            //Create a file to store the image

            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                val photoURI: Uri =
                    FileProvider.getUriForFile(requireActivity(),
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        photoFile!!)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI)
                startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode==RESULT_OK) {
            //binding.imgGalleryUploadEditProfile.set
            Glide.with(requireActivity())
                .load(photoFile)
                .into(binding.imgPicImageCreateGroup)
            binding.layoutPickGalleryImgEditProfile.visibility=View.GONE
            binding.imgPicImageCreateGroup.visibility=View.VISIBLE
            viewModel.photoFile=photoFile
            /*  binding.imgPicImageCreateGroup.layoutParams.height=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
              binding.imgPicImageCreateGroup.layoutParams.width=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()*/
        }else
            if(requestCode==GALARYCode && resultCode==RESULT_OK)
            {
                val selectedImage = data!!.data
                val photoFile2 = File(getPath(selectedImage))
                Glide.with(requireActivity())
                    .load(photoFile2)
                    .into(binding.imgPicImageCreateGroup)
                binding.layoutPickGalleryImgEditProfile.visibility=View.GONE
                binding.imgPicImageCreateGroup.visibility=View.VISIBLE
                viewModel.photoFile=photoFile2
            }
    }

    var imagePath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        imagePath = image.absolutePath
        return image
    }

    private fun requestPermissionsMedia(optionType:Int) {
            Dexter.withContext(requireActivity())
                .withPermissions(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        if (p0!!.areAllPermissionsGranted()) {
                            if(optionType==1){
                                openFromGallery()
                            }else
                            {
                                openCameraIntent()
                            }
                        }else
                        {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?,
                    ) {
                        p1!!.continuePermissionRequest()
                    }
                }).check()
    }

    // below is the shoe setting dialog method which is use to display a dialogue message.
    fun showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        var builder = AlertDialog.Builder(requireActivity())

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions")

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                // below is the intent from which we are redirecting our user.
                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                var uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivityForResult(intent, 101)
            })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        // below line is used to display our dialog
        builder.show()
    }

    fun openFromGallery()
    {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, GALARYCode)
    }


    fun getPath(uri: Uri?): String {
        val projection = arrayOf<String>(MediaStore.MediaColumns.DATA)
        val cursor: Cursor = requireActivity().managedQuery(uri, projection, null, null, null)
       var column_index = cursor
            .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        imagePath = cursor.getString(column_index)
        return cursor.getString(column_index)
    }

    private fun selectImage() {
        try {
            //  val pm: PackageManager = requireActivity().getPackageManager()
            // val hasPerm: Int = pm.checkPermission(Manifest.permission.CAMERA, requireActivity().getPackageName())
            //if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(requireContext())
            builder.setTitle("Select Option")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item] == "Take Photo") {
                    dialog.dismiss()
                    requestPermissionsMedia(2)
                } else if (options[item] == "Choose From Gallery") {
                    dialog.dismiss()
                    requestPermissionsMedia(1)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            })
            builder.show()
            //  }
        } catch (e: Exception) {
            Toast.makeText(requireActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun startDialog() {
        var myAlertDialog = AlertDialog.Builder(
            activity)
        myAlertDialog.setTitle("Upload Pictures Option")
        myAlertDialog.setPositiveButton("Gallery"
        ) { arg0, arg1 ->
            requestPermissionsMedia(1)
            mediaDialog!!.dismiss()
        }
        myAlertDialog.setNegativeButton("Camera"
        ) { arg0, arg1 ->
            mediaDialog!!.dismiss()
            requestPermissionsMedia(2)
        }
        myAlertDialog.setNeutralButton("Cancel"
        ) { arg0, arg1 ->
            mediaDialog!!.dismiss()
        }
        mediaDialog=myAlertDialog.create()
        mediaDialog!!.show()

    }



}
