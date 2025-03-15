package app.bettermetesttask.movies.sections.compose.movieitem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.movies.R
import coil3.compose.AsyncImage

@Composable
fun MovieItemRegular(isActive:Boolean, movie: Movie, onLikeClicked: (Int) -> Unit, onOpenDetails:()->Unit) {

    // Local flag for animation
    var localAnimateFlag by remember { mutableStateOf(false) }


    var likeButtonWidth by remember{ mutableStateOf(0)}
    val density = LocalDensity.current

    val animateAlpha by animateFloatAsState(
        targetValue = if(localAnimateFlag) 1f else 0f,
        animationSpec = tween(300)
    )

    LaunchedEffect(isActive) {
        if (isActive) localAnimateFlag=true
    }




    Row(
        modifier = Modifier
            .alpha(animateAlpha)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = "Movie Poster",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = movie.title, fontSize = 18.sp, color = Color.Black)
            Text(text = movie.description, fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onLikeClicked(movie.id) }) {
                Icon(
                    imageVector = if (movie.liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like Button",
                    tint = if (movie.liked) Color.Red else Color.Gray,
                    modifier = Modifier.onSizeChanged {
                        likeButtonWidth = it.width
                    }
                )
            }
            IconButton(onClick = onOpenDetails) {
                Icon(
                    painter = painterResource(R.drawable.ic_details_arrow) ,
                    contentDescription = "Details Button",
                    tint = Color.Gray,
                    modifier = Modifier.width(
                        width = with(density){likeButtonWidth.toDp()}
                    )
                )
            }
        }

    }
}