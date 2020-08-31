package com.medical.mycompanylocation.adaptersection


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medical.mycompanylocation.R
import com.medical.mycompanylocation.datamodels.CompanyInfo
import java.util.*


class CompanyAdapter(val context: Context, val imageSearchdata: List<CompanyInfo>, val clickListener: SelectedClickListener) :
    RecyclerView.Adapter<CompanyAdapter.ViewHolder>(){


//    val searchFragment: SearchFragment?=null
    lateinit var checkedItemSelelcted : ArrayList<CompanyInfo>
    val checkedItem: Boolean ?=null
    var recentItemSelected: Boolean = true
    val VIEW_TYPE_TODAY: String ?=null
    val VIEW_TYPE_REGULAR: String ?=null
    val VIEW_COUNT: String ?=null


    override fun getItemCount() = imageSearchdata.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val displayView = layoutInflater.inflate(R.layout.layout_company_items, parent, false)
        return ViewHolder(displayView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val monsterDataCollection = imageSearchdata[position]

        val viewType = getItemViewType(position)
        with(holder) {
            nameText.let {
                it?.text = monsterDataCollection.company.name
            }
            id.let {
                it?.text = monsterDataCollection.email
            }
//            Glide.with(context)
//                .load(monsterDataCollection.link)
//                .into(this.imgLink!!)
          imgMap.let {

              val geoLat : String = monsterDataCollection.address.geo.lng
              val geoLng : String = monsterDataCollection.address.geo.lat

              if ( geoLat.isEmpty() || geoLng.isEmpty()) {
                  it?.setImageResource(R.drawable.ic_outline)
                  }
              else{
                  it?.setImageResource(R.drawable.ic_baseline)
              }
          }
            //           checkImage.tag = monsterDataCollection.title
//            checkImage?.setTag(monsterDataCollection)
  //          Log.i("the","tag"+checkImage)
//            if (!(checkedItem==true)) {
//                checkImage?.visibility = GONE
//
//
////                    checkedItemSelelcted.add(dataItem)
//                //        Toast.makeText(context, "item added into list: ${imageSearchdata.get(position).title}", Toast.LENGTH_SHORT).show()
//            }
//            else
//            {
//                //    checkedItemSelelcted.remove(dataItem)
//                checkImage?.visibility = VISIBLE
//                checkImage?.isChecked = true
//                //        Toast.makeText(context, "item removed from the list : ${imageSearchdata.get(position).title}", Toast.LENGTH_SHORT ).show()
//            }
//            if (isChecked!!) {
//                val dataItem = imageSearchdata.get(position).title as Data
//                checkedItemSelelcted.add(dataItem)
//                Toast.makeText(
//                    context,
//                    "item added into list: ${imageSearchdata.get(position).title}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                checkImage.visibility = GONE
//            } else {
//                checkImage.visibility = VISIBLE
//                val dataItem = imageSearchdata.get(position).title as Data
//                checkedItemSelelcted.remove(dataItem)
//                Toast.makeText(
//                    context,
//                    "item removed from the list : ${imageSearchdata.get(position).title}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }


//            checkImage.setOnClickListener {

//                if(imageSearchdata.get(position) is Data){
//                    val dataItem = imageSearchdata.get(position) as Data
//                    if () {
//                        context?.let { ContextCompat.getColor(it, R.color.colorOrange) }
//                            ?.let { holder.itemView.setBackgroundColor(it) }
//
//                    } else {
//                        context?.let { ContextCompat.getColor(it, R.color.white) }
//                            ?.let { holder.itemView.setBackgroundColor(it) }
//
//                    }
//                }
//                if (searchFragment.isChecked!!) {
//                    val dataItem = imageSearchdata.get(position).title as Data
//                    checkedItemSelelcted.add(dataItem)
//                    Toast.makeText(
//                        context,
//                        "item added into list: ${imageSearchdata.get(position).title}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    val dataItem = imageSearchdata.get(position).title as Data
//                    checkedItemSelelcted.remove(dataItem)
//                    Toast.makeText(
//                        context,
//                        "item removed from the list : ${imageSearchdata.get(position).title}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            //       }
            holder.itemView.setOnClickListener {
                clickListener.onSearchImageItemClickEvent(monsterDataCollection)
//                longClickListiner.onLonClickEvent(monsterDataCollection)

            }
        }
    }

    fun setCurrentItemLayout(recentItemSelected: Boolean ){
        this.recentItemSelected = recentItemSelected
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameText: TextView? = null
        var addressText: TextView? = null
        var imgMap: ImageView? =null
        var id: TextView? = null

        init {
            nameText = itemView.findViewById(R.id.tv_Name)
            addressText = itemView.findViewById(R.id.tv_Name)
            id = itemView.findViewById(R.id.tv_Username)
            imgMap = itemView.findViewById(R.id.img_geolocation)

        }

    }

    interface SelectedClickListener{
        fun onSearchImageItemClickEvent(data:CompanyInfo)
//        fun onLonClickEvent(data:Data)
    }
}
