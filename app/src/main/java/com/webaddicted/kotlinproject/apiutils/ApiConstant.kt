package com.webaddicted.kotlinproject.apiutils

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ApiConstant {

    companion object {



        const val PHONE_AUTH_TIMEOUT: Long = 119

        /*********API BASE URL************/
        const val BASE_URL = "https://api.github.com/"
        const val API_TIME_OUT: Long = 6000

        //        START FIREBASE CHILD
        const val FCM_DB_USERS = "Users"
        const val FCM_DB_CATEGORY = "Category"

        //        END FIREBASE CHILD

        //        START FIREBASE STORAGE FOLDER
        const val FCM_STORAGE_PROFILE = "UserProfileImages/"
        //        END FIREBASE STORAGE FOLDER

        //        START FIREBASE KEY
        const val FCM_USERS_EMAIL_ID = "userEmailId"
        const val FCM_USERS_MOBILE_NO = "userMobileno"
        const val FCM_USERS_PASSWORD = "password"
        const val FCM_USERS_FCM_TOKEN= "fcmToken"
        //        END FIREBASE KEY

    }
}