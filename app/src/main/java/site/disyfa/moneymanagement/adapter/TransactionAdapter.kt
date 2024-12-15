package site.disyfa.moneymanagement.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import site.disyfa.moneymanagement.MainActivity
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.CategoryDao
import site.disyfa.moneymanagement.localdb.TransactionDao
import site.disyfa.moneymanagement.model.Transaction
import site.disyfa.moneymanagement.ui.EditActivity
import site.disyfa.moneymanagement.util.DBInitializer
import site.disyfa.moneymanagement.util.IconManager
import site.disyfa.moneymanagement.util.StringGenerator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TransactionAdapter (
    private val context: Context,
    private val transactionList: List<Transaction>,
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.transaction_title)
        val textDescription: TextView = itemView.findViewById(R.id.transaction_description)
        val textNominal: TextView = itemView.findViewById(R.id.transaction_nominal)
        val textDate: TextView = itemView.findViewById(R.id.transaction_date)
        val transactionContainer:  ConstraintLayout= itemView.findViewById(R.id.transaction_container)
        val categoryBox:  ImageView= itemView.findViewById(R.id.categoryBox)
        val typeColor:  LinearLayout= itemView.findViewById(R.id.type_color)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }
    override fun getItemCount(): Int {
        return transactionList.size
    }
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.textTitle.text = transaction.category
        holder.textDescription.text = transaction.description
        holder.textDate.text = transaction.date.substring(0,10)
        holder.textNominal.text = StringGenerator.formatToRupiah(transaction.amount)
        holder.transactionContainer.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("TRANSACTION_ID",transaction.id)
            context.startActivity(intent)
        }
        if(transaction.type=="income"){
            holder.typeColor.setBackgroundColor(Color.GREEN)
        }else{
            holder.typeColor.setBackgroundColor(Color.RED)

        }
        holder.categoryBox.setBackgroundResource(IconManager(context).colourIcons.first { it.id==transaction.categoryColor }.drawable)
        holder.categoryBox.setImageResource(IconManager(context).categoryIcons.first { it.id==transaction.categoryIcon }.drawable)

    }
}