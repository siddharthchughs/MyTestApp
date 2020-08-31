package com.medical.mycompanylocation.Utility

import android.content.Context

class MainHelper {

    companion object{
        fun getTextFromResource(context: Context, resourceSection:Int):String{
            return context.resources.openRawResource(resourceSection).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }
        fun getTextFromAssets(context: Context, filename:String):String{
            return context.assets.open(filename).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }
    }

}