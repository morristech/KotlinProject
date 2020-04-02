package com.webaddicted.kotlinproject.viewModel.fcmkit

import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.model.repository.fcmkit.FcmFoodRepository
import com.webaddicted.kotlinproject.viewModel.base.BaseViewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodViewModel(private val projectRepository: FcmFoodRepository) : BaseViewModel() {
    fun getFcmFoodUserInfo(): FcmSocialLoginRespoBean {
        return projectRepository.getFcmFoodUserInfo()
    }

    fun setFcmFoodUserInfo(userInfo: FcmSocialLoginRespoBean) {
        projectRepository.setFcmFoodUserInfo(userInfo)
    }

}