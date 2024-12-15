package site.disyfa.moneymanagement.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import site.disyfa.moneymanagement.databinding.SpinnerColourBinding
import site.disyfa.moneymanagement.databinding.SpinnerIconBinding
import site.disyfa.moneymanagement.model.Category

@Suppress("NAME_SHADOWING")
class IconSpinner (
    context: Context,
    private var icons: Array<Icon>?,
    private var backgrounds: Array<Icon>?,
    private var spinnerType: String,
    private var categoryList: List<Category>?=null
) : BaseAdapter() {

    private var inflter: LayoutInflater = LayoutInflater.from(context)
    private var manager: IconManager = IconManager(context = context)

    override fun getCount(): Int {
        if (categoryList != null) {
            return categoryList!!.size
        }
        if (icons != null) {
            return icons!!.size
        }
        return backgrounds!!.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    @SuppressLint("InflateParams")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val view: View?

        when (spinnerType) {
            "category" -> {
                val spinnerView = SpinnerIconBinding.inflate(inflter)
                view = spinnerView.root
                spinnerView.ivAccountIcon.setImageResource(manager.getIconByID(icons!!,categoryList!![position].icon).drawable)
                spinnerView.ivAccountIcon.setBackgroundResource(manager.getIconByID(backgrounds!!,categoryList!![position].colour).drawable)
                spinnerView.tvIconName.text = categoryList!![position].name
            }
            "icon" -> {
                val spinnerView = SpinnerIconBinding.inflate(inflter)
                view = spinnerView.root

                spinnerView.ivAccountIcon.setImageResource(icons!![position].drawable)
                if (backgrounds != null) {
                    spinnerView.ivAccountIcon.setBackgroundResource(backgrounds!![position].drawable)
                }
                spinnerView.tvIconName.text = icons!![position].text
            }
            "colour" -> {
                val spinnerView = SpinnerColourBinding.inflate(inflter)
                view = spinnerView.root
                spinnerView.ivAccountColour.setBackgroundResource(backgrounds!![position].drawable)
                spinnerView.tvColourName.text = backgrounds!![position].text
            }

            else -> {
                view = SpinnerColourBinding.inflate(inflter).root
            }
        }
        return view
    }

}


