package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.view.base.BaseLocation
import com.webaddicted.kotlinproject.view.fragment.GoogleMapFrm
import com.webaddicted.kotlinproject.viewModel.map.MapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class MapActivity : BaseLocation() {
    private lateinit var mBinding: ActivityCommonBinding
    public val mapViewModel: MapViewModel by viewModel()

    companion object{
        val TAG: String = MapActivity::class.java.simpleName
        fun newIntent(activity: Activity){
            activity.startActivity(Intent(activity, MapActivity::class.java))
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_common
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        navigateScreen(GoogleMapFrm.TAG)
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            GoogleMapFrm.TAG -> frm = GoogleMapFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }

    override fun getCurrentLocation(location: Location, address: String?) {
        Lg.d(TAG, "lat -> " + location.latitude.toString() + "\n long -> " + location.longitude.toString())
        mapViewModel.locationUpdated.postValue(location)
    }


}