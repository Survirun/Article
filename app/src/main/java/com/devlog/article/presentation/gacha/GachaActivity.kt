package com.devlog.article.presentation.gacha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devlog.article.R
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.ui.theme.BaseColumn
import java.util.Random


class GachaActivity : AppCompatActivity() {
    var random = Random()

    lateinit var userPreference: UserPreference
    lateinit var  item :Item
    lateinit var  popUpOpen : MutableState<Boolean>
    var coin: Int =0
    var userInventory :String = ""
    var  userInventoryList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gacha)
        userPreference = UserPreference.getInstance(this)
        userInventory = userPreference.userInventory
        userInventory.map {
            userInventoryList.add(it.toString())
        }
        if ( userPreference.userDesk!=-1){

        }
        coin = userPreference.coin
        setContent {
            GachaView()
        }

    }

    @Composable
    fun GachaView(){
        popUpOpen = remember {
            mutableStateOf(false)
        }
        Box() {
            Image(
                modifier = Modifier.fillMaxSize(1f),
                painter = painterResource(id = R.drawable.gacha2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    if (coin>5){
                        val  r= random()
                        coin -=5
                        popUpOpen.value=true
                        item = itemList[r]
                        if (!userInventoryList.contains(r.toString())){
                           userInventoryList.add(r.toString())
                        }
                        Log.e("아이템",userInventoryList.toString())

                    }else{
                        Toast.makeText(this@GachaActivity,"코인이 부족해요",Toast.LENGTH_SHORT).show()
                    }

                }, modifier = Modifier.fillMaxWidth(1f)) {
                    Text(text = "뽑기")
                }

            }
            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                Text(modifier = Modifier.background(Color.White), text = coin.toString())
            }
            if (popUpOpen.value){
                popUp(item)
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun preView() {
        coin =100
        userInventory.map {
            userInventoryList.add(it.toString())
        }
        GachaView()



    }
    @Preview(showBackground = false)
    @Composable
    fun preViewPopUp(){
        var  item=Item(
        "사과 노트북 듀얼 모니터 세트",
        "이 애플 노트북 듀얼 모니터 세트는 스타일과 성능을 결합한 완벽한 작업 환경을 제공하는 특별한 아이템입니다. 애플 노트북과 더블 모니터를 조합하여 사용자가 생산성을 극대화하고 창의성을 높일 수 있도록 돕습니다.",
        R.drawable.computer_2
        ,0)
        popUp(item)
    }


    @Composable
    fun popUp(item: Item) {

        Column(
            Modifier
                .fillMaxSize(1f)
                .padding(horizontal = 30.dp),verticalArrangement = Arrangement.Center) {
            Surface(shape = RoundedCornerShape(10), ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier.padding(vertical = 10.dp), text = item.name)
                    Image(
                        modifier = Modifier.padding(vertical = 10.dp),
                        painter = painterResource(id = item.image),
                        contentDescription = null
                    )
                    Text(modifier = Modifier.padding(vertical = 10.dp),text = item.subTitle)
                    Button(modifier = Modifier.padding(vertical = 10.dp),onClick = { popUpOpen.value =false }) {
                        Text(text = "닫기")
                    }
                }
            }

        }



    }

    fun random(): Int = random.nextInt(18)
    fun item(int: Int) {
        when (int) {
            1 -> {}
            2 -> {}
            3 -> {}
            4 -> {}
            5 -> {}
            6 -> {}
            7 -> {}
            8 -> {}
            9 -> {}
            10 -> {}
        }
    }

}