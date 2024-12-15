package site.disyfa.moneymanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.model.Category
import site.disyfa.moneymanagement.util.IconManager

class CategoryAdapter (private val context: Context, private val categoryList: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvName)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }
    override fun getItemCount(): Int {
        return categoryList.size
    }
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.tvName.text = category.name
        val iconManager = IconManager(context)
        holder.ivIcon.setImageResource(
            iconManager.getIconByID(iconManager.categoryIcons, category.icon).drawable
        )
        holder.ivIcon.setBackgroundResource(
            iconManager.getIconByID(iconManager.colourIcons, category.colour).drawable
        )
    }
}