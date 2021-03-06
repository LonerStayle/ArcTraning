package kr.loner.arctraining.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kr.loner.arctraining.BuildConfig
import kr.loner.arctraining.R
import kr.loner.arctraining.model.Result
import kr.loner.arctraining.ui.MainActivityViewModel
import kr.loner.arctraining.ui.NavigationViewModel
import kr.loner.arctraining.ui.currentViewModel
import kr.loner.arctraining.ui.screen.Mode.NORMAL
import kr.loner.arctraining.ui.screen.Mode.SEARCH
import kr.loner.base.ui.compose.*

@Composable
fun UpcomingScreen(
    modifier: Modifier,
    viewModel: MainActivityViewModel = currentViewModel()
) {
    val currentType by viewModel.currentMode.collectAsState()

    if (currentType == SEARCH) {
        BackHandler {
            viewModel.changeMode(NORMAL)
        }
    }

    Scaffold(
        topBar = {
            SearchAppBar(
                title = stringResource(R.string.toolbar_title_search),
                currentType = currentType,
                onBackPressed = { /* if you need define... */ },
                onChangeType = viewModel::changeMode
            ) { query ->
                viewModel.setQuery(query)
            }
        }) { innerPadding ->
        when (currentType) {
            NORMAL -> {
                val movies = viewModel.upcomingMovie.collectAsLazyPagingItems()
                val isRefreshing by viewModel.isRefresh.collectAsState()

                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { viewModel.refreshMovie() }
                ) {
                    UpcomingMovieList(
                        modifier = modifier.padding(innerPadding),
                        movies = movies,
                        block = viewModel::navigate
                    )
                }
            }
            SEARCH -> {
                val searchMovie = viewModel.searchMovie.collectAsLazyPagingItems()
                val keyboardTrigger by viewModel.keyboardTrigger.collectAsState(0L)
                SearchMovieList(
                    modifier = modifier.padding(innerPadding),
                    movies = searchMovie,
                    isNotEmptyQuery = { viewModel.searchQuery.isNotEmpty() },
                    keyboardTrigger = keyboardTrigger,
                    block = viewModel::navigate
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UpcomingMovieList(
    modifier: Modifier,
    movies: LazyPagingItems<Result>,
    block: (NavigationViewModel.Screen) -> Unit
) {
    LazyVerticalGrid(cells = GridCells.Fixed(2), modifier = modifier) {
        itemsIndexed(movies) { movie, position ->
            MovieThumbnail(movie, position, block)
        }
        setPagingStateListener(
            movies,
            refresh = {
                item { LoadingView(Modifier.fillParentMaxSize()) }
                item { LoadingView(Modifier.fillParentMaxSize()) }
            },
            append = {
                item { LoadingItem() }
                item { LoadingItem() }
            }
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchMovieList(
    modifier: Modifier,
    movies: LazyPagingItems<Result>,
    isNotEmptyQuery: () -> Boolean,
    keyboardTrigger: Long,
    block: (NavigationViewModel.Screen) -> Unit
) {
    val scaffoldState = LocalScaffoldState.current

    LazyColumn(modifier = modifier) {
        // Result' id is not stable
        items(movies /*, key = ResultModel::id */) { movie ->
            if (movie == null) return@items
            MovieCard(movie, block)
        }
        setPagingStateListener(
            movies,
            isNotEmptyQuery = isNotEmptyQuery,
            refresh = { item { LoadingView(Modifier.fillParentMaxSize()) } },
            append = { item { LoadingItem() } },
            empty = {
                item {
                    EmptySnackBar(scaffoldState)
                    EmptyPlaceHolder(Modifier.fillParentMaxSize())
                }
            }
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(keyboardTrigger) {
        if (keyboardTrigger == 0L) return@LaunchedEffect
        keyboardController?.hide()
        focusManager.clearFocus()
    }
}

@Composable
private fun MovieCard(movie: Result, block: (NavigationViewModel.Screen) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val imageRatio = screenWidth / screenHeight
    Image(
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .aspectRatio(imageRatio)
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = { block(NavigationViewModel.Screen.DetailMovie(movie)) }
            ),
        painter = rememberImagePainter("${BuildConfig.TMDB_IMAGE_URL}${movie.posterPath}") {
            // if you want..
            // placeholder()
            // error()
        },
        contentDescription = "MovieThumbnail",
        alignment = Alignment.Center
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchAppBar(
    title: String,
    currentType: Mode,
    onBackPressed: () -> Unit,
    onChangeType: (Mode) -> Unit,
    block: (searchQuery: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Move to another screen and when you return restore it
    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // when restore searchText, move cursor to text.length
    searchText = if (currentType == Mode.NORMAL) {
        TextFieldValue()
    } else {
        block(searchText.text)
        TextFieldValue(searchText.text, TextRange(searchText.text.length))
    }

    SimpleTopBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(painter = painterResource(id = R.drawable.ic_sketch_symbol), null)
            }
        },
        actions = {
            if (currentType == Mode.SEARCH) {
                IconButton(
                    onClick = { onChangeType(NORMAL) },
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "CloseButton",
                        tint = Color.White
                    )
                }
            } else {
                keyboardController?.hide()
                IconButton(
                    onClick = { onChangeType(SEARCH) },
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "SearchButton",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        if (currentType == Mode.SEARCH) {
            Surface {
                TextField(
                    textStyle = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        block(searchText.text)
                    },
                    placeholder = { Text(text = stringResource(R.string.search)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            block(searchText.text)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.White,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
            SideEffect {
                if (searchText.text.isEmpty()) {
                    focusRequester.requestFocus()
                }
            }
        } else {
            Text(text = title)
        }
    }
}

@Composable
private fun MovieThumbnail(
    movie: Result,
    index: Int,
    block: (NavigationViewModel.Screen) -> Unit
) {
    val bottomPadding = if (index % 2 == 1) 8.dp else 0.dp

    val moviePosterRatio = 1f / 1.3f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = bottomPadding),
        contentAlignment = Alignment.Center
    ) {
        Image(
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .aspectRatio(moviePosterRatio)
                .fillMaxWidth()
                .align(Alignment.Center)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = { block(NavigationViewModel.Screen.DetailMovie(movie)) }
                ),
            painter = rememberImagePainter("${BuildConfig.TMDB_IMAGE_URL}${movie.posterPath}") {
                // if you want..
                // placeholder()
                // error()
            },
            contentDescription = "MovieThumbnail",
            alignment = Alignment.Center
        )
    }
}

enum class Mode {
    NORMAL, SEARCH
}


