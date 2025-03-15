package app.bettermetesttask.movies.sections.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedBox( isContentLoaded:Boolean, modifier: Modifier = Modifier, content: @Composable ()->Unit) {

    val animateColor by animateColorAsState(
        targetValue = if(isContentLoaded) Color.White else Color(0xFF121212),
        tween(1300)
    )

    Box(modifier=Modifier.background(color = animateColor)){
        content()
    }
}