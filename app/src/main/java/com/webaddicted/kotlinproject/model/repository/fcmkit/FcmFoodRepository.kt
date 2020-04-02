package com.webaddicted.kotlinproject.model.repository.fcmkit

import com.webaddicted.kotlinproject.apiutils.ApiServices
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.model.repository.base.BaseRepository

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodRepository constructor(private val apiServices: ApiServices) : BaseRepository() {
    fun setFcmFoodUserInfo(fcmUser: FcmSocialLoginRespoBean) {
        preferenceMgr.setFcmUserInfo(fcmUser)
    }

    fun getFcmFoodUserInfo(): FcmSocialLoginRespoBean {
        return preferenceMgr.getFcmUserInfo()
    }
}