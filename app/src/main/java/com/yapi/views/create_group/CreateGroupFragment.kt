package com.yapi.views.create_group

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
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
import com.yapi.common.changeBackgroundForEditError
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard
import com.yapi.databinding.FragmentCreateGroupBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.add_people.AddPeopleFragment
import com.yapi.views.sign_in.SignInErrorData
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateGroupFragment : DialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentCreateGroupBinding
    val viewModel: ViewModelCreateGroup by viewModels()
    val GALARYCode = 321

    @Inject
    lateinit var preferenceFile: PreferenceFile

    companion object {
        fun newInstanceCreateGroup(title: String): CreateGroupFragment {
            val args = Bundle()
            args.putString("11", title)
            val fragment = CreateGroupFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkDeviceType()) {
            System.out.println("phone========tablet")
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialog)
            //setStyle(DialogFragment.STYLE_NO_FRAME,android.R.style.Theme_NoTitleBar);
            //}

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
        window.setLayout(second_frame_width.toInt(), second_frame_height.toInt())
        return dialog
    }

    /*  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
          // Implement Dialog-specific functionality here
          if(requireActivity().getResources().getBoolean(R.bool.isTab)) {
              return super.onCreateDialog(savedInstanceState)
          }else
          {
              return null!!
          }
      }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateGroupBinding.inflate(LayoutInflater.from(context))

        //inflater.context.setTheme(R.style.FullScreenDialog)
        binding.vModel = viewModel
        addObserverForOpenAddPeople()
        dialogDismissMethod()
        hideKeyboardObserver()
        clickMethod()

        return binding.root
    }

    //For call Click Listener
    private fun clickMethod() {

        binding.layoutUploadImageCreateGroup.setOnClickListener(this)

    }

    private fun hideKeyboardObserver() {

        viewModel.hideKeyboardData.observe(requireActivity(), Observer {
            var data = it as Boolean
            if (data) {
                requireActivity().hideKeyboard()
            }
        })
    }

    private fun addObserverForOpenAddPeople() {
        viewModel.addPeopleScreenOpenData.observe(requireActivity(), Observer {
            var data = it as String
            if (data != null) {
                AddPeopleFragment.newInstanceAddPeople(data)
                    .showNow(requireActivity().supportFragmentManager, " SimpleDialog.TAG")
                dismiss()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        showErrorUIObserver()
        binding.apply {
            var rightMarginTopLayout = 0
            if (checkDeviceType()) {
                rightMarginTopLayout =
                    requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
                ivOutsideCloseGroup.visibility = View.VISIBLE
                imgCancelCreateGroup.visibility = View.GONE
                layoutCreateGroup.setBackgroundResource(R.drawable.et_drawable)
            } else {
                layoutCreateGroup.setBackgroundResource(0)
                rightMarginTopLayout = 0
                ivOutsideCloseGroup.visibility = View.GONE
                imgCancelCreateGroup.visibility = View.VISIBLE
            }

            // val ll = LinearLayout(this)
            //ll.setOrientation(LinearLayout.VERTICAL)

            //var layoutParams=binding.layoutCreateGroup.layoutParams

            val layoutParams = binding.layoutCreateGroup.layoutParams as LinearLayout.LayoutParams
            //  val newLayoutParams = toolbar.getLayoutParams()
            layoutParams.topMargin = 0
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = rightMarginTopLayout
            binding.layoutCreateGroup.layoutParams = layoutParams
        }
    }

    fun dialogDismissMethod() {
        viewModel.dismissDialogData.observe(requireActivity(), Observer {
            var data = it as Boolean
            if (data) {
                dismiss()
            }
        })
    }

    fun showErrorUIObserver() {
        viewModel.errorData.observe(requireActivity(), Observer {
            var data = it as SignInErrorData

            var editText: AppCompatEditText? = null
            var errorText: AppCompatTextView? = null
            if (data.fieldId == 1) {
                editText = binding.etGroupNameCreateGroup
                errorText = binding.txtErrorEmailSignup
            } else
                if (data.fieldId == 2) {
                    editText = binding.etGroupDescriptionCreateGroup
                    errorText = binding.txtErrorDescription
                } else {
                    binding.txtErrorEmailSignup.text = ""
                    changeBackgroundForEditError(binding.etGroupNameCreateGroup,
                        requireActivity().resources.getColor(
                            R.color.white),
                        requireActivity().resources.getColor(R.color.liteGrey))

                    binding.txtErrorDescription.text = ""
                    changeBackgroundForEditError(binding.etGroupDescriptionCreateGroup,
                        requireActivity().resources.getColor(
                            R.color.white),
                        requireActivity().resources.getColor(R.color.liteGrey))
                }

            if (data != null && data.message.isNotEmpty()) {
                errorText!!.text = data.message

                changeBackgroundForEditError(editText!!,
                    ContextCompat.getColor(requireContext(), R.color.error_box_color),
                    ContextCompat.getColor(requireContext(), R.color.error_border_color))
            } else {
                if (data.fieldId != 0) {
                    errorText!!.text = ""


                    changeBackgroundForEditError(editText!!,
                        ContextCompat.getColor(requireContext(), R.color.liteGrey),
                        ContextCompat.getColor(requireContext(), R.color.white))
                }
            }
        })
    }

    fun uploadPhotoMethod() {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.layoutUploadImageCreateGroup -> {
                Log.e("kgkgvwsgwggwsg", "wggwgwggw")
                selectImage()
            }
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

    private fun requestPermissionsMedia(optionType: Int) {
        Dexter.withContext(requireActivity())
            .withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        if (optionType == 1) {
                            openFromGallery()
                        } else {
                            openCameraIntent()
                        }
                    } else {
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

    fun openFromGallery() {
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


    private val REQUEST_CAPTURE_IMAGE = 200
    var photoFile: File? = null
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
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode== Activity.RESULT_OK) {
            //binding.imgGalleryUploadEditProfile.set
            Glide.with(requireActivity())
                .load(photoFile)
                .into(binding.imgPicImageCreateGroup)
            binding.layoutPickGalleryImgEditProfile.visibility = View.GONE
            binding.imgPicImageCreateGroup.visibility = View.VISIBLE
            viewModel.photoFile = photoFile
            /*  binding.imgPicImageCreateGroup.layoutParams.height=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
              binding.imgPicImageCreateGroup.layoutParams.width=requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()*/
        } else
            if (requestCode == GALARYCode && resultCode== Activity.RESULT_OK) {
                val selectedImage = data!!.data
                val photoFile2 = File(getPath(selectedImage))
                Glide.with(requireActivity())
                    .load(photoFile2)
                    .into(binding.imgPicImageCreateGroup)
                binding.layoutPickGalleryImgEditProfile.visibility = View.GONE
                binding.imgPicImageCreateGroup.visibility = View.VISIBLE
                viewModel.photoFile = photoFile2
            }
    }
}