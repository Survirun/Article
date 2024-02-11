package com.devlog.article.presentation.work_face

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import com.devlog.article.R
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.databinding.FragmentWorkFaceBinding
import com.devlog.article.presentation.gacha.Item
import com.devlog.article.presentation.gacha.itemList

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [WorkFaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkFaceFragment : Fragment() {
    lateinit var binding:FragmentWorkFaceBinding
    lateinit var userPreference: UserPreference
    var userInventoryList = arrayListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentWorkFaceBinding.inflate(layoutInflater)
        userPreference.userInventory.map {
            userInventoryList.add(Integer.getInteger(it.toString())!!)

        }
        binding.userInventoryButton.setOnClickListener {
            binding.composeView.isVisible=true
        }


        return binding.root
    }

    @Preview(showBackground = true)
    @Composable
    fun preView(){

        LazyColumn(){
            items(itemList.size){ i->
                item(itemList[i])
            }

        }
        Column(modifier = Modifier.fillMaxSize(1f), horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Bottom) {
            Button(onClick = { binding.composeView.isVisible=false }) {
                Text(text = "닫기")
            }
        }


    }
   
    @Composable
    fun item(item: Item){
        Row(verticalAlignment = Alignment.CenterVertically , modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = 10.dp)
            .background(Color.White)) {
            Column {
                Image(painter = painterResource(id = item.image), contentDescription = null)

            }
            Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)) {
                Text(text =item.name )
                Text(modifier = Modifier.padding(vertical = 10.dp),text = item.subTitle)
            }

        }

    }



}