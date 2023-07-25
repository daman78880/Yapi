package com.yapi

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yapi.common.Constants
import com.yapi.common.MyMessageEvent
import com.yapi.databinding.ActivityMainBinding
import com.yapi.pref.A
import com.yapi.pref.PreferenceFile
import com.yapi.views.chat.ChatMessagesFragment
import com.yapi.views.chat.chatGroupInfo.ChatGroupInfoFragment
import com.yapi.views.chat.chatUserInfo.ChatUserInfoFragment
import com.yapi.views.chat_empty.ChatEmptyFragment
import com.yapi.views.menu_screen.GroupData
import com.yapi.views.menu_screen.MenuFragment
import com.yapi.views.savedItems.SavedItemsFragment
import com.yapi.views.userList.UserListFragment
import com.yapi.views.workspaces.workspacesList.WorkSpacesListFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var dataBinding: ActivityMainBinding
    val viewModel: DataViewModel by viewModels()

    @Inject
    lateinit var preferenceFile: PreferenceFile
    @Inject
//    @Named("sahilSir")
    lateinit var testing: A
    @Inject
    lateinit var testiing: A
//    @Named("sahilDone")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("asdfjasdn","value a->${testing.a}b->${testing.d} e${testing===testiing}")
        dataBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(dataBinding.root)
        activity = WeakReference<Activity>(this)
        EventBus.getDefault().register(this)
        initUI()

        dataBinding.secondFrame.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                dataBinding.secondFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                preferenceFile.saveStringValue("second_frame_height",dataBinding.secondFrame.height.toString())
                preferenceFile.saveStringValue("second_frame_width",dataBinding.secondFrame.width.toString())
                //height is ready
            }
        })
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun initUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
         navController = navHostFragment.navController
        /*  val navHostFragment =
              supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
          val navController = navHostFragment.navController
          navController.navigate(R.id.signupTeam)*/
        //  findNavController(R.id.signupTeam)
        dataBinding.apply {
            vModel = viewModel
        }
    }

    fun changeTypeOfDevice() {
        if (resources.getBoolean(R.bool.isTab)) {
            System.out.println("phone========tablet")

            supportFragmentManager.beginTransaction().replace(R.id.firstFrame, MenuFragment())
                .commit()
            supportFragmentManager.beginTransaction().replace(R.id.secondFrame, ChatEmptyFragment())
                .commit()

            showTabsLayoutMethod()
            hideContainerMethod()

        } else {
            System.out.println("phone========mobile")
            hideTabsLayoutMethod()
            showContainerMethod()
        }
    }

    override fun onStart() {
        super.onStart()
        activity = WeakReference(this@MainActivity)
    }

    companion object {
        var activity: WeakReference<Activity>? = null
    }

    fun showTabsLayoutMethod() {
        dataBinding.llLayoutsForTabs.visibility = View.VISIBLE
    }

    fun hideTabsLayoutMethod() {
        dataBinding.llLayoutsForTabs.visibility = View.GONE
    }

    fun showContainerMethod() {
        dataBinding.linearNormalLayout.visibility = View.VISIBLE
    }

    fun hideContainerMethod() {
        dataBinding.linearNormalLayout.visibility = View.GONE
    }

    fun showThirdFrame() {
        dataBinding.thirdFrame.visibility = View.VISIBLE
    }

    fun hideThirdFrame() {
        dataBinding.thirdFrame.visibility = View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MyMessageEvent?) {
        // Do something
        Log.e("gsegegsgsgs===", System.currentTimeMillis().toString())
        if (event!!.screenName == Constants.MENU_KEY) {
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    changeTypeOfDevice()
                }
            }, 200)

        } else
            if (event.screenName == Constants.CHAT_MESSAGE_KEY) {

                var oldGroupData=event.allData as GroupData

                createChatMethod(oldGroupData,Constants.GROUPS_KEY)
            } else
                if (event.screenName == Constants.USER_PROFILE) {
                    showTabsMethod(3, event.screenName)
                } else
                    if (event.screenName == Constants.USER_PROFILE_BACK) {
                        showTabsMethod(2, event.screenName)
                    } else
                        if (event.screenName == Constants.GROUP_PROFILE) {
                            showTabsMethod(3, event.screenName)
                        } else
                            if (event.screenName == Constants.GROUP_PROFILE_BACK) {
                                showTabsMethod(2, event.screenName)
                            }else
                                if(event.screenName==Constants.USER_MANAGEMENT)
                                {
                                    var fragment = UserListFragment()
                                    openUserMamangementScreen(fragment)
                                }else
                                    if(event.screenName==Constants.WORKSPACE_MANAGEMENT)
                                    {
                                        var fragment = WorkSpacesListFragment()
                                        openUserMamangementScreen(fragment)
                                    }else
                                        if(event.screenName==Constants.SAVED_ITEMS_KEY)
                                        {
                                            var fragment = SavedItemsFragment()
                                            openUserMamangementScreen(fragment)
                                        }

    }

    fun openUserMamangementScreen(fragment:Fragment)
    {
        //var fragment = UserListFragment()
        supportFragmentManager.beginTransaction().replace(R.id.secondFrame, fragment).commit()
    }

    fun createChatMethod(groupData:GroupData,userType:String) {
        //   ChatMessagesFragment.newInstanceChatMethod("").show(supportFragmentManager," SimpleDialog.TAG")
        var bundle = Bundle()
        bundle.putString("userType", userType)
        bundle.putSerializable("group_data", groupData)
        var fragment = ChatMessagesFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.secondFrame, fragment).commit()
    }

    fun showTabsMethod(tabsValue: Int, screenType: String) {
        if (tabsValue == 3) {
            dataBinding.llLayoutsForTabs.weightSum = 4.2f
            // dataBinding.firstFrame.layoutParams
            var lParamsFirst =
                dataBinding.firstFrame.layoutParams as LinearLayout.LayoutParams
            var lParamsSecond =
                dataBinding.secondFrame.layoutParams as LinearLayout.LayoutParams
            var lParamsThird =
                dataBinding.thirdFrame.layoutParams as LinearLayout.LayoutParams
            lParamsFirst.weight = 1.2f
            lParamsSecond.weight = 1.8f
            lParamsThird.weight = 1.2f
            dataBinding.thirdFrame.visibility = View.VISIBLE
        } else {
            dataBinding.llLayoutsForTabs.weightSum = 3f

            var lParamsFirst =
                dataBinding.firstFrame.layoutParams as LinearLayout.LayoutParams
            var lParamsSecond =
                dataBinding.secondFrame.layoutParams as LinearLayout.LayoutParams
            var lParamsThird =
                dataBinding.thirdFrame.layoutParams as LinearLayout.LayoutParams
            lParamsFirst.weight = 1.2f
            lParamsSecond.weight = 1.8f
            lParamsThird.weight = 0f
            dataBinding.thirdFrame.visibility = View.GONE
        }
        if (screenType.equals(Constants.USER_PROFILE)) {
            var bundle = Bundle()
            bundle.putString("userType", "")
            var fragment = ChatUserInfoFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.thirdFrame, fragment).commit()
        } else
            if (screenType.equals(Constants.GROUP_PROFILE)) {
                var bundle = Bundle()
                bundle.putString("userType", "")
                var fragment = ChatGroupInfoFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.thirdFrame, fragment)
                    .commit()
            }
    }

        override fun onBackPressed() {
            if (navController.currentDestination?.id == R.id.signInFragment) {
                finishAffinity()
            }
            else
                if(navController.currentDestination!!.id==R.id.menuFragment)
                {
                    finish()
                }
            else {
                super.onBackPressed()
            }
    }
}