package com.acdev.pokedex.SubComposables

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable

@Composable
fun DisplayToast(context: Context,msg:String){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}