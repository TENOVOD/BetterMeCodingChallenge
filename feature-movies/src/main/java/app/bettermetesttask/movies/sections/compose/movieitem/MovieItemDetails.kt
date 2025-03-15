package app.bettermetesttask.movies.sections.compose.movieitem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun MovieItemDetails(isActive: Boolean, movie: Movie, onLikeClicked: (Int) -> Unit, onOpenDetails:()->Unit) {

    var localAnimateFlag by remember { mutableStateOf(false) }
    var likeButtonWidth by remember{ mutableStateOf(0) }
    val density = LocalDensity.current
    val animateAlpha by animateFloatAsState(
        targetValue = if(localAnimateFlag) 1f else 0f
    )
    val animateScale by animateFloatAsState(
        targetValue = if(localAnimateFlag) 1f else 0f,
        animationSpec = tween(300)
    )
    val animateRowIconAlpha by animateFloatAsState(
        targetValue = if(localAnimateFlag) 1f else 0f,
        animationSpec = tween(1000)
    )
    LaunchedEffect(isActive) {
        if (isActive) localAnimateFlag=true
    }

    Column(
        modifier = Modifier
            .alpha(animateAlpha)
            .scale(animateScale)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                modifier = Modifier.weight(1f).alpha(animateRowIconAlpha),
                onClick = { onLikeClicked(movie.id) }
            ) {
                Icon(
                    imageVector = if (movie.liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like Button",
                    tint = if (movie.liked) Color.Red else Color.Gray,
                    modifier = Modifier.onSizeChanged {
                        likeButtonWidth = it.width
                    }
                )
            }
            AsyncImage(
                model = movie.posterPath,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .weight(3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )
            IconButton(
                onClick = onOpenDetails,
                modifier = Modifier.weight(1f).alpha(animateRowIconAlpha)
            ) {
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

        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = movie.title, fontSize = 24.sp, color = Color.Black)
            Text(text = movie.description, fontSize = 18.sp, color = Color.Gray)
        }


    }

}