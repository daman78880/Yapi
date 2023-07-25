package com.yapi.views.search_result

import android.graphics.drawable.Drawable

data class PojoSearchScreenData(val status:Int=0, val statusTitle:String?=null, val statusSubTitle:String?=null,
                                val nameOrFileName:String?=null, val daginationOrFileSize:String?=null,
                                val imageOrDrawable: Drawable?=null, val groupName:String?=null, val groupCreatedTime:String?=null,
                                val msgTime:String?=null, val msg:String?=null,val endViewLine:Boolean=false)
