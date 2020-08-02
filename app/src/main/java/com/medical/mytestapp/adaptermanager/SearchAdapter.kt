package com.medical.mytestapp.adaptermanager

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medical.mytestapp.R
import com.medical.mytestapp.model.Data
import java.util.ArrayList


class SelectItemAdapter(val context: Context, val imageSearchdata: List<Data>, val clickListener: SelectedClickListener) :
    RecyclerView.Adapter<SelectItemAdapter.ViewHolder>(), Filterable {

    private  lateinit var searchFilteredlist: List<Data>
   init {
       this.searchFilteredlist= imageSearchdata
   }
    override fun getItemCount() = searchFilteredlist.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val displayView = layoutInflater.inflate(R.layout.layout_search_items, parent, false)

        return ViewHolder(displayView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val monsterDataCollection = imageSearchdata[position]
        with(holder){
            nameText?.let {
                it.text = monsterDataCollection.title
            }
            monsterRate?.let {
                it.text = monsterDataCollection.id
            }
            Glide.with(context)
                .load(monsterDataCollection.link)
                .into(monsterImage)

            holder.itemView.setOnClickListener {
                clickListener.onSearchImageItemClickEvent(monsterDataCollection)
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText = itemView.findViewById<TextView>(R.id.tvHeading)
        val monsterImage = itemView.findViewById<ImageView>(R.id.imGallery)
        val monsterRate = itemView.findViewById<TextView>(R.id.tvSubHeading)
    }

    interface SelectedClickListener{
        fun onSearchImageItemClickEvent(data:Data)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchFilteredlist = imageSearchdata
                } else {
                    val resultList = ArrayList<Data>()
                    for (row in imageSearchdata) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.cover.contentEquals(charString)) {
                            resultList.add(row)
                        }
                        searchFilteredlist = resultList
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = searchFilteredlist
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

                searchFilteredlist = filterResults!!.values as List<Data>
                notifyDataSetChanged()

            }
        }
    }
}
