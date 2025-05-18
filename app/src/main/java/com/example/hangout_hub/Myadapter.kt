package com.example.hangout_hub


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide



class CustomAdapter(var context: Context, var data:ArrayList<Hangout>):BaseAdapter() {
    private class ViewHolder(row:View?){
        var mTximgview:ImageView
        var mTxtName:TextView
        var mTxlocation:TextView
        var mTxcontacts:TextView
        var mTxdescription:TextView

        init {
            this.mTximgview = row?.findViewById(R.id.mTvimgview)as ImageView
            this.mTxtName = row?.findViewById(R.id.mTvName) as TextView
            this.mTxlocation = row?.findViewById(R.id.mTvlocation) as TextView
            this.mTxcontacts = row?.findViewById(R.id.mTvcontacts) as TextView
            this.mTxdescription = row?.findViewById(R.id.mTvdescription) as TextView

        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if (convertView == null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.myitem,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

//            val imageReference = storageReference.child(dataSource[position])
//            imageReference.downloadUrl.addOnSuccessListener { uri ->
//                Glide.with(context)
//                    .load(uri)
//                    .into(imageView)
//            }.addOnFailureListener { exception ->
//                // Handle failure
//            }
//




        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var item:Hangout = getItem(position) as Hangout
        viewHolder.mTxtName.text = item.name
        viewHolder.mTxlocation.text = item.location
        viewHolder.mTxcontacts.text = item.contacts
        viewHolder.mTxdescription.text = item.description

        return view as View
    }
    override fun getItem(position: Int): Any {
        return  data.get(position)
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return data.count()
    }
}
