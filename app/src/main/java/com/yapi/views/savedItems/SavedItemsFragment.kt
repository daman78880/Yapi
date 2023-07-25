package com.yapi.views.savedItems

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yapi.MainActivity
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.databinding.SavedItemsLayoutBinding
import com.yapi.views.chat.ClickMessage
import com.yapi.views.chat.EditMessageData
import com.yapi.views.chat.MessageClickListener
import com.yapi.views.chat.RVEditMessageAdapter
import java.util.ArrayList

class SavedItemsFragment : Fragment(), MessageClickListener {

    private var rvSavedItemsAdapter: RVSavedItemsAdapter? = null
    private lateinit var dataBinding: SavedItemsLayoutBinding

    private val mViewModel: SavedViewModels by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dataBinding = SavedItemsLayoutBinding.inflate(inflater)
        dataBinding.mViewmodel = mViewModel

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        mViewModel.screenWidth = width
        mViewModel.screenHeight = height

        initUI()
        return dataBinding.root
    }

    //For UI Intialization
    private fun initUI() {
        if (checkDeviceType()) {
            mViewModel.backArrowVisible.set(false)
        } else {
            mViewModel.backArrowVisible.set(true)
        }

        setAdapterDataMethod()

    }

    //For set adapter
    private fun setAdapterDataMethod() {
        val arraylist = ArrayList<String>()
        arraylist.clear()
        arraylist.add("AA")
        arraylist.add("BB")
        arraylist.add("CC")
        arraylist.add("DD")
        arraylist.add("EE")
        arraylist.add("FF")
        arraylist.add("GG")
        arraylist.add("GG")
        arraylist.add("AA")
        arraylist.add("AA")
        arraylist.add("AA")
        arraylist.add("AA")
        arraylist.add("AA")
        arraylist.add("AA")

        dataBinding.rvSavedItems.layoutManager = LinearLayoutManager(requireActivity())
        rvSavedItemsAdapter = RVSavedItemsAdapter(requireActivity(),arraylist,this)
        dataBinding.rvSavedItems.adapter = rvSavedItemsAdapter
    }

    override fun onMesssageListener(position: Int, ivMoreImageView: ImageView, userType: Int) {
        showEditMessageMethod(ivMoreImageView,userType)
    }

    //When click on the three dots
    fun showEditMessageMethod(ivMoreImageView: ImageView,userType:Int) {
        val mView: View = LayoutInflater.from(MainActivity.activity!!.get())
            .inflate(R.layout.edit_chat_message_layout, null, false)
        val newWidth = mViewModel.screenWidth!! / 1.5

        //   val popUp = PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false)
        val popUp =
            PopupWindow(mView, newWidth.toInt(), LinearLayout.LayoutParams.WRAP_CONTENT, false)

        //  if(rvChatAdapter)

        //  popUp.showAtLocation(mView, Gravity.RIGHT,0,0);
        popUp.isTouchable = true
        popUp.isFocusable = true
        popUp.isOutsideTouchable = true

        //popUp.setIsLaidOutInScreen(true)
        //   val btnViewProfile =

        popUp.showAsDropDown(ivMoreImageView)

        popUp.isOutsideTouchable = true

        popUp.setTouchInterceptor(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.action === MotionEvent.ACTION_OUTSIDE) {
                    popUp.dismiss()
                    return true
                }
                return false
            }
        })

        val editOtherMessageList = ArrayList<EditMessageData>()
       /* editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.reply_message_text),
            R.drawable.reply_message_icon))*/
        /* editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.edit_message_text),
             R.drawable.edit_message_icon))*/
        editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.unsave_message_text),
            R.drawable.save_message))
        editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.mark_unread_text),
            R.drawable.mark_as_unread))
        editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.copy_message_text),
            R.drawable.copy_message_icon))
        editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.pin_conversation_text),
            R.drawable.push_pin))
        editOtherMessageList.add(EditMessageData(requireActivity().resources.getString(R.string.delete_message_text),
            R.drawable.delete_chat_icon))


        val newMessageList = ArrayList<EditMessageData>()
            for(idx in 0 until editOtherMessageList.size)
            {
                newMessageList.add(editOtherMessageList[idx])
            }

        val rvEditMessageAdapter =
            RVEditMessageAdapter(requireActivity(), newMessageList, object : ClickMessage {
                override fun onClickListener(position: Int) {
                    //For click on Edit Message Options
                    popUp.dismiss()
                }
            })
        val rvEditMessages = mView.findViewById<RecyclerView>(R.id.rvEditMessages)
        rvEditMessages.layoutManager = LinearLayoutManager(requireActivity())
        rvEditMessages.adapter = rvEditMessageAdapter
    }
}