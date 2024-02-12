package com.devlog.article.presentation.work_face

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.bumptech.glide.Glide
import com.devlog.article.R
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.databinding.FragmentWorkFaceBinding
import com.devlog.article.presentation.gacha.GachaActivity
import com.devlog.article.presentation.gacha.Item
import com.devlog.article.presentation.gacha.chair
import com.devlog.article.presentation.gacha.computer
import com.devlog.article.presentation.gacha.desk
import com.devlog.article.presentation.gacha.itemCategory
import com.devlog.article.presentation.gacha.itemList
import com.devlog.article.presentation.gacha.sub_item

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
        userPreference=UserPreference.getInstance(requireContext())
        userPreference.userInventory.map {
            userInventoryList.add(Integer.getInteger(it.toString())!!)

        }
        initUserProfile()

        binding.userInventoryButton.setOnClickListener {
            binding.composeView.isVisible=true
            binding.composeView.setContent {
                LazyColumn(modifier = Modifier.fillMaxSize(1f).background(Color.White)){
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
        }
        binding.gachaButton.setOnClickListener {
            startActivity(Intent(requireContext(),GachaActivity::class.java))
        }


        return binding.root
    }

    fun initUserProfile(){
        userPreference.run {
            Log.e("asfasf",userChair.toString())
            if (userChair!=-1){
                Glide.with(this@WorkFaceFragment)
                    .load(itemList[userChair].image)
                    .into(binding.chairImageView)

            }
            if (userComputer!=-1){
                Glide.with(this@WorkFaceFragment)
                    .load(itemList[userComputer].image)
                    .into(binding.computerImageView)

            }
            if (userDesk!=-1){
                Glide.with(this@WorkFaceFragment)
                    .load(itemList[userDesk].image)
                    .into(binding.deskImageView)

            }
            if (userSubItem!=-1){
                Glide.with(this@WorkFaceFragment)
                    .load(itemList[userSubItem].image)
                    .into(binding.subItemImageView)
            }

        }

    }

    @Preview(showBackground = true)
    @Composable
    fun preView(){
        LazyColumn(modifier = Modifier.fillMaxSize(1f).background(Color.White)){
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
            .background(Color.White)
            .clickable {
                when(itemCategory(item.id)){
                    chair->{
                        userPreference.userChair=item.id
                        Glide.with(this@WorkFaceFragment)
                            .load(item.image)
                            .into(binding.chairImageView)


                    }
                    desk ->{
                        userPreference.userDesk=item.id
                        Glide.with(this@WorkFaceFragment)
                            .load(item.image)
                            .into(binding.deskImageView)

                    }
                    computer->{
                        userPreference.userComputer=item.id
                        Glide.with(this@WorkFaceFragment)
                            .load(item.image)
                            .into(binding.computerImageView)
                    }
                    sub_item->{
                        userPreference.userSubItem=item.id
                        Glide.with(this@WorkFaceFragment)
                            .load(item.image)
                            .into(binding.subItemImageView)
                    }

                }
                binding.composeView.isVisible=false
            }
        ) {
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