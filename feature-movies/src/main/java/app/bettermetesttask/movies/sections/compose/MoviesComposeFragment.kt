package app.bettermetesttask.movies.sections.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.movies.sections.MoviesState
import app.bettermetesttask.movies.sections.MoviesViewModel
import app.bettermetesttask.movies.sections.compose.movieitem.MovieItemDetails
import app.bettermetesttask.movies.sections.compose.movieitem.MovieItemRegular
import coil3.compose.AsyncImage
import javax.inject.Inject
import javax.inject.Provider

class MoviesComposeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MoviesViewModel>

    private val viewModel by viewModels<MoviesViewModel> {
        SimpleViewModelProviderFactory(
            viewModelProvider
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                val viewState by viewModel.moviesStateFlow.collectAsState()
                MoviesComposeScreen(viewState, likeMovie = { movie ->
                    viewModel.likeMovie(movie)
                }, viewLoaded = {
                    viewModel.loadMovies()
                })
            }
        }
    }
}

@Composable
private fun MoviesComposeScreen(
    moviesState: MoviesState,
    likeMovie: (Movie) -> Unit,
    viewLoaded: () -> Unit
) {
    viewLoaded()

    var isContentLoaded by remember { mutableStateOf(false) }

    AnimatedBox(
        isContentLoaded = isContentLoaded,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (moviesState) {
            MoviesState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
            is MoviesState.Loaded -> {
                LazyColumn {
                    items(moviesState.movies) { item ->
                        if(!isContentLoaded) isContentLoaded=true
                        MovieItem(item, onLikeClicked = {
                            likeMovie(item)
                        })
                    }
                }
            }

            MoviesState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onLikeClicked: (Int) -> Unit) {

    var isDetailScreenVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                )
        ) {
            when(isDetailScreenVisible){
                true->MovieItemDetails(
                    isActive = isDetailScreenVisible,
                    movie = movie,
                    onLikeClicked = onLikeClicked,
                    onOpenDetails = {
                        isDetailScreenVisible = !isDetailScreenVisible
                    }
                )
                false->MovieItemRegular(
                    isActive = !isDetailScreenVisible,
                    movie = movie,
                    onLikeClicked = onLikeClicked,
                    onOpenDetails = {
                        isDetailScreenVisible = !isDetailScreenVisible
                    }
                )
            }
        }

    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
private fun PreviewsMoviesComposeScreen() {
    MoviesComposeScreen(MoviesState.Loaded(
        List(20) { index ->
            Movie(
                index,
                "Title $index",
                "Overview $index",
                null,
                liked = index % 2 == 0,
            )
        }
    ), likeMovie = {}, viewLoaded = {})
}