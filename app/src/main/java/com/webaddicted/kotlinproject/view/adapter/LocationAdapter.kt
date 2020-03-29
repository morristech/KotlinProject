package com.webaddicted.kotlinproject.view.adapter

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowLocationBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.LocationBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class LocationAdapter(private var mTaskList: ArrayList<LocationBean>?) : BaseAdapter() {

    override fun getListSize(): Int {
        if (mTaskList == null) return 0
        return mTaskList?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_location
    }

    override fun onBindTo(mRowBinding: ViewDataBinding, position: Int) {
        if (mRowBinding is RowLocationBinding) {
            val source = mTaskList?.get(position)
            if (source?.addre?.length!! > 0) {
                mRowBinding.txtAddress.text = source.addre
                mRowBinding.txtAddress.visible()
            } else mRowBinding.txtAddress.gone()
            mRowBinding.txtLat.text = "Latitude : ${source.lat}"
            mRowBinding.txtLong.text = "Longitude : ${source.lon}"
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            mRowBinding.card.setBackgroundColor(color)
        }
    }

    fun notifyAdapter(list: ArrayList<LocationBean>) {
        this.mTaskList = list
        notifyDataSetChanged()
    }
}
