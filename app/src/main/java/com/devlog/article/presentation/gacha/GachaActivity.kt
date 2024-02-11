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
    var itemList = arrayListOf(
        Item(
            "신비로운 나무 의자",
            "이 나무 의자는 숲의 신비로운 힘을 담고 있는 특별한 아이템입니다. 그냥 앉아만 있지 않고, 이 의자를 통해 자연과 소통하며 다양한 경험을 즐길 수 있습니다.",
            R.drawable.chair_1
        ),
        Item(
            "컴퓨터 의자",
            "이 의자는 평범함의 뒷면에 숨겨진 놀라움을 안고 있습니다. 무언가 특별한 능력이나 마법은 없어 보이지만, 한 번 앉아보면 당신은 이 평범한 의자가 얼마나 특별한 경험을 제공할 수 있는지 깨닫게 될 것입니다.",
            R.drawable.chair_2
        ),
        Item("고급 게이밍 의자", "이것만 있음 나도 페이커...?", R.drawable.chair_3),
        Item(
            "할아버지의 품격 있는 의자",
            "이 의자는 할아버지의 품격과 옛날의 정신을 계승한 특별한 아이템입니다. 간결한 디자인에는 역사와 추억이 깃들여져 있으며, 할아버지의 지혜와 따뜻함이 의자 전체에 스며 있습니다.",
            R.drawable.chair_4
        ),
        Item(
            "디벨로퍼의 편안한 의자",
            "이 의자는 개발자들을 위해 특별히 디자인된, 편안하고 실용적인 아이템입니다. 긴 개발 세션과 코딩 시간을 고려하여 개발자의 편의를 위해 다양한 기능이 탑재되어 있습니다.",
            R.drawable.chair_5
        ),
        Item(
            "더블 비전 모니터 세트",
            "이 더블 모니터 세트는 현대적이고 생산적인 작업환경을 위해 디자인된 효율적인 아이템입니다. 두 개의 모니터가 효율적인 작업을 위한 다양한 기능과 함께 사용자에게 편의를 제공합니다.",
            R.drawable.computer_1
        ),
        Item(
            "사과 노트북 듀얼 모니터 세트",
            "이 애플 노트북 듀얼 모니터 세트는 스타일과 성능을 결합한 완벽한 작업 환경을 제공하는 특별한 아이템입니다. 애플 노트북과 더블 모니터를 조합하여 사용자가 생산성을 극대화하고 창의성을 높일 수 있도록 돕습니다.",
            R.drawable.computer_2
        ),
        Item(
            "트리플 모니터 업그레이드 키트",
            "트리플 모니터 업그레이드 키트는 사용자에게 최대한의 작업 효율성을 제공하기 위해 설계된 특별한 아이템입니다. 이 업그레이드 키트는 애플리케이션 다중 실행, 데이터 분석, 게임 플레이 등 다양한 작업에 이상적인 작업 환경을 제공합니다.",
            R.drawable.computer_3
        ),
        Item(
            "사운드 마스터 음향 작업용 컴퓨터",
            "사운드 마스터 음향 작업용 컴퓨터는 전문적인 음향 작업과 녹음에 최적화된 기능과 편의성을 제공하는 고급 아이템입니다. 음향 엔지니어, 음악 프로듀서, 혹은 오디오 업계에서 작업하는 사용자들을 위해 특별히 개발되었습니다.",
            R.drawable.computer_4
        ),
        Item(
            "매킨토시 컴퓨터",
            "맥스토리 음향작업 마스터 컴퓨터는 음향 엔지니어, 음악 프로듀서, 사운드 디자이너 등 전문적인 음향 작업을 수행하는 사용자들을 위해 디자인된 특별한 매킨토시 컴퓨터입니다. 최신 기술과 맥 운영체제를 결합하여 뛰어난 성능과 안정성을 제공합니다",
            R.drawable.computer_5
        ),
        Item(
            "트렌디한 모던 책상",
            "이 트렌디한 모던 책상은 기능성과 스타일을 결합하여 현대적이고 실용적인 작업 환경을 제공합니다. 간결한 디자인과 다양한 기능으로 일상적인 사용자들을 위한 이상적인 책상입니다.",
            R.drawable.desk_1
        ),
        Item(
            "프리미엄 럭셔리 책상",
            "프리미엄 럭셔리 책상은 고품질의 자재와 정교한 디자인으로 제작된 고급 책상입니다. 이 책상은 세련된 스타일과 기능성을 결합하여 탁월한 작업 환경을 제공합니다.",
            R.drawable.desk_2
        ),
        Item(
            "메이플 우드 그레인 책상",
            "메이플 우드 그레인 책상은 자연스럽고 아름다운 메이플 나무를 사용하여 제작된 책상으로, 자연의 따뜻한 감성과 함께 고급스러운 작업 공간을 제공합니다. 이 책상은 목재의 고유한 우드 그레인과 아름다운 색감이 돋보이는 디자인으로 사용자에게 특별한 작업 환경을 제공합니다.",
            R.drawable.desk_3
        ),
        Item(
            "멀티펑션 수납 책상",
            "멀티펑션 수납 책상은 작업 환경을 깔끔하게 정리하고 효율적으로 사용할 수 있는 현대적이고 다용도적인 아이템입니다. 특히 수납 공간이 풍부하여 문서, 도서, 문구류 등을 체계적으로 정리할 수 있습니다.",
            R.drawable.desk_4
        ),
        Item(
            "인더스트리얼 스타일 철제 책상",
            "인더스트리얼 스타일 철제 책상은 튼튼하면서도 세련된 디자인으로 제작된 아이템으로, 강화된 철재와 고급스러운 마감으로 현대적이면서도 특별한 작업 공간을 제공합니다.",
            R.drawable.desk_5
        ),
        Item(
            "실내 정원",
            "실내 정원 셋은 아름다운 꽃과 함께 화분이 어우러진 아이템으로, 실내 공간에 자연의 아름다움과 신선한 느낌을 불어넣습니다. 이 셋은 공간을 화사하게 꾸미며, 식물의 치유적인 효과와 함께 따뜻한 분위기를 제공합니다.",
            R.drawable.sub_item_1
        ),
        Item(
            "에덴의 플로랄 파리지옥 ",
            "에덴의 플로랄 파리지옥은 특별하고 신비로운 분위기를 조성하는 독특한 향기와 디자인의 캔들입니다. 플로랄한 향과 아름다운 디자인은 공간에 환상적인 분위기를 불어넣어주며, 휴식과 안정을 제공합니다.",
            R.drawable.sub_item_2
        ),
        Item(
            "트리플 헤이피 풍선 세트",
            "트리플 헤이피 풍선 세트는 즐거움과 흥분을 불러일으키는 세 가지 색상의 풍선으로, 특별한 순간을 기념하거나 행사를 화려하게 장식하는 데 이상적인 아이템입니다.",
            R.drawable.sub_item_3
        ),
    )
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
                        Toast.makeText(this@GachaActivity,"코인이 무족해요",Toast.LENGTH_SHORT).show()
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
        R.drawable.computer_2)
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