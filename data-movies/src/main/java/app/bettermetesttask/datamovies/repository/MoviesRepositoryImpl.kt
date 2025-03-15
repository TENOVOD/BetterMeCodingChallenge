package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val localStore: MoviesLocalStore,
    private val mapper: MoviesMapper
) : MoviesRepository {

    private val restStore = MoviesRestStore()

    override suspend fun getMovies(): Result<List<Movie>> {
        val localMovies = localStore.getMovies()
        val initialMovies = if (localMovies.isNotEmpty()) {
            localMovies.map(mapper.mapFromLocal)
        } else {
            emptyList()
        }

        return try {
            // Try try to catch data from remote source
            withTimeout(3000L) {
                val remoteMovies = restStore.getMovies()

                // Remote data to local
                val movieEntities = remoteMovies.map(mapper.mapToLocal)
                localStore.insertMovies(movieEntities)
                val localMovies = localStore.getMovies()
                val mappedMovies = localMovies.map(mapper.mapFromLocal)
                Result.Success(mappedMovies)
            }
        } catch (e: Exception) {
            // If error -> return local data
            Timber.e(e, "Failed to fetch remote movies, using local data")
            if (initialMovies.isNotEmpty()) {
                Result.Success(initialMovies)
            } else {
                Result.Error(e)
            }
        }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return Result.of { mapper.mapFromLocal(localStore.getMovie(id)) }
    }

    override fun observeLikedMovieIds(): Flow<List<Int>> {
        return localStore.observeLikedMoviesIds()
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        localStore.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localStore.dislikeMovie(movieId)
    }
}