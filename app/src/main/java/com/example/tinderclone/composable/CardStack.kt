package com.example.tinderclone.composable


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.tinderclone.User
import com.example.tinderclone.ui.theme.Green
import com.example.tinderclone.ui.theme.Pink
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun CardStack(modifier : Modifier = Modifier,
              users: MutableList<User>,
              thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
              enableButtons: Boolean = false,
              onSwipeLeft : ( user : User) -> Unit = {},
              onSwipeRight : ( item : User) ->  Unit = {},
              onEmptyStack : ( lastUser : User) -> Unit = {}
){

    var i by remember { mutableStateOf(users.size-1)}

    if( i == -1 ){
        onEmptyStack( users.last() )
    }

    val cardStackController = rememberCardStackController()
    cardStackController.onSwipeLeft = {
        onSwipeLeft(users[i])
        i--
    }
    cardStackController.onSwipeRight = {
        onSwipeRight(users[i])
        i--
    }

    ConstraintLayout(modifier = modifier.fillMaxSize().padding(20.dp)) {
        val (buttons, stack) = createRefs()

        if(enableButtons){
            Row( modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttons){
                    bottom.linkTo(parent.bottom)
                    top.linkTo(stack.bottom)
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                FloatingActionButton(
                    onClick = { if (i >= 0) cardStackController.swipeLeft() },
                    backgroundColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(5.dp)
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "", tint = Pink, modifier = Modifier.size(30.dp))
                }
                Spacer( modifier = Modifier.width(70.dp))
                FloatingActionButton(
                    onClick = { if (i >= 0) cardStackController.swipeRight() },
                    backgroundColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(5.dp)
                ) {
                    Icon(Icons.Rounded.Favorite,contentDescription = "", tint = Green, modifier = Modifier.size(30.dp))
                }
            }
        }

        Box(modifier = Modifier
            .constrainAs(stack){
                top.linkTo(parent.top)
            }
            .draggableStack(
                controller = cardStackController,
                thresholdConfig = thresholdConfig,
            )
            .fillMaxHeight(0.8f)
        ){
            users.asReversed().forEachIndexed{ index, user ->
                Card(modifier = Modifier
                    .moveTo(
                        x = if (index == i) cardStackController.offsetX.value else 0f,
                        y = if (index == i) cardStackController.offsetY.value else 0f
                    )
                    .visible( visible = index == i || index == i - 1)
                    .graphicsLayer(
                        rotationZ = if (index == i) cardStackController.rotation.value else 0f,
                        scaleX = if (index < i) cardStackController.scale.value else 1f,
                        scaleY = if (index < i) cardStackController.scale.value else 1f
                    )
                    .shadow(4.dp, RoundedCornerShape(10.dp)),
                    user
                )
            }
        }
    }
}

@Composable
fun Card(modifier: Modifier = Modifier, user: User){
    Box(
        modifier
    ){
        AsyncImage(
            model = user.imageURL,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp)),
        )
        Column(modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(10.dp)
        ){
            Text(text = user.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.clickable(onClick = {}) // disable the highlight of the text when dragging
            )
            Text(text = user.city,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.clickable(onClick = {}) // disable the highlight of the text when dragging
            )
        }
    }
}


fun Modifier.moveTo(
    x: Float,
    y: Float
) = this.then(Modifier.layout{measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height){
        placeable.placeRelative(x.roundToInt(),y.roundToInt())
    }
})

fun Modifier.visible(
    visible: Boolean = true
) = this.then(Modifier.layout{measurable, constraints ->
    val placeable = measurable.measure(constraints)
    if(visible){
        layout(placeable.width, placeable.height){
            placeable.placeRelative(0,0)
        }
    }else{
        layout(0, 0) {}
    }
})
