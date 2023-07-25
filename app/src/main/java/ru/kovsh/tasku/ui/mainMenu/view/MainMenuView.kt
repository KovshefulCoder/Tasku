package ru.kovsh.tasku.ui.mainMenu.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState
import ru.kovsh.tasku.ui.main.view.MainScreen
import ru.kovsh.tasku.ui.mainMenu.entities.AreaUI
import ru.kovsh.tasku.ui.mainMenu.entities.MenuCategory
import ru.kovsh.tasku.ui.mainMenu.entities.menuCategories
import ru.kovsh.tasku.ui.mainMenu.viewModel.MainMenuViewModel
import ru.kovsh.tasku.ui.shared.SharedViewModel
import ru.kovsh.tasku.ui.shared.entities.statesClasses.LoadingState
import ru.kovsh.tasku.ui.theme.Background
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base200
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.Base800
import ru.kovsh.tasku.ui.theme.DarkBlueBackground800
import ru.kovsh.tasku.ui.theme.DarkBlueBackground900
import ru.kovsh.tasku.ui.theme.PlaceholderText
import ru.kovsh.tasku.ui.theme.typography


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
internal fun MainMenuScreen(
    onUnauthorized: () -> Unit,
    onRepeatLoadingTasks: () -> Unit,
    onResetRemoteState: () -> Unit,
    onAreaClick: (Long, String) -> Unit,
    onSettingsClick: () -> Unit,
    onMenuButtonClicked: (Int) -> Unit,
    viewModel: MainMenuViewModel,
    sharedViewModel: SharedViewModel,
) {
    val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsState()
    val fabStates by viewModel.fabState.collectAsStateWithLifecycle()
    val areas = viewModel.areas

    if (sharedState.authorizationStatus == UserAuthenticationState.Unauthorized) {
        onUnauthorized()
    }
    when (screenState.isAuthorized) {
        UserAuthenticationState.None -> {
            MainScreen()
        }
        UserAuthenticationState.Unauthorized -> {
            onUnauthorized()
        }

        UserAuthenticationState.Authorized, UserAuthenticationState.NoServer -> {
            Log.i("MainMenuScreen", "Tasks loading status: ${sharedState.remoteState}")
            MainMenuScreen(
                onFabClick = viewModel::onFabClick,
                onUpdateVisibleAreas = viewModel::onUpdateVisibleAreas,
                onFabChange = viewModel::onFabChange,
                onFabStart = viewModel::onFabStart,
                onFabEnd = viewModel::onFabEnd,
                onAddNewArea = viewModel::onAddNewArea,
                onCalculateLazySizes = viewModel::onCalculateLazySizesForFAB,
                onAreaClick = onAreaClick,
                onSettingsClick = onSettingsClick,
                onRepeatLoadingTasks = onRepeatLoadingTasks,
                onResetRemoteState = onResetRemoteState,
                onMenuButtonClicked = onMenuButtonClicked,
                menuCategories = menuCategories,
                areas = areas,
                textFieldUnderFabIndex = fabStates.textFieldUnderFabIndex,
                textFieldUnderFabHeight = fabStates.textFieldUnderFabHeight,
                newElementIndex = screenState.newElementIndex,
                loadTasksStatus = sharedState.remoteState,
            )
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun MainMenuScreen(
    onFabClick: () -> Unit,
    onUpdateVisibleAreas: (MutableMap<Int, Pair<Float, Float>>) -> Unit,
    onFabChange: (Float, Float) -> Unit,
    onFabStart: () -> Unit,
    onFabEnd: () -> Unit,
    onCalculateLazySizes: (Int, Int, Dp) -> Unit,
    onAddNewArea: (String) -> Unit,
    onAreaClick: (Long, String) -> Unit,
    onSettingsClick: () -> Unit,
    onRepeatLoadingTasks: () -> Unit,
    onResetRemoteState: () -> Unit,
    onMenuButtonClicked: (Int) -> Unit = {},
    menuCategories: List<MenuCategory>,
    areas: List<AreaUI>,
    modifier: Modifier = Modifier,
    textFieldUnderFabIndex: Int = -2,
    textFieldUnderFabHeight: Dp = 0.dp,
    newElementIndex: Int = -2,
    loadTasksStatus: LoadingState = LoadingState.Idle,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        val lazyColumnYCoordinate = remember { mutableStateOf(0f) }
        val listState = rememberLazyListState()
        val visibleAreasCoordinates = remember { mutableMapOf<Int, Pair<Float, Float>>() }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(modifier = Modifier.weight(15f)) {
                SearchButton(textID = R.string.main_menu_search_placeholder)
                Spacer(modifier = Modifier.height(20.dp)) //TODO() FIX
                MenuCategories(
                    onCalculateLazySizes = onCalculateLazySizes,
                    onMenuButtonClicked = onMenuButtonClicked,
                    menuCategories = menuCategories
                )
                Divider(color = Base800)
                LazyColumn(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        lazyColumnYCoordinate.value = coordinates.positionInRoot().y
                    },
                    state = listState
                ) {
                    if (areas.isEmpty() && (textFieldUnderFabIndex == -1 || newElementIndex == -1)) {
                        item {
                            TextFieldPlaceholderUnderFab(
                                onAddNewArea,
                                textFieldUnderFabHeight,
                                focusRequester
                            )
                        }
                    }
                    itemsIndexed(areas) { index, area ->
                        RenderLazyColumn(
                            onCalculateLazySizes = onCalculateLazySizes,
                            onAddNewArea = onAddNewArea,
                            onAreaClick = onAreaClick,
                            onReturnCoordinates = { i, x, y ->
                                if (y >= lazyColumnYCoordinate.value) {
                                    visibleAreasCoordinates[i] = Pair(x, y)
                                } else if (visibleAreasCoordinates.containsKey(i)) {
                                    visibleAreasCoordinates.remove(i)
                                }
                            },
                            index = index,
                            area = area,
                            areasSize = areas.size,
                            textFieldUnderFabIndex = textFieldUnderFabIndex,
                            textFieldUnderFabHeight = textFieldUnderFabHeight,
                            newElementIndex = newElementIndex,
                            focusRequester = focusRequester
                        )
                    }

                }
                // transfer coordinates of visible areas to the viewModel
                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                        .debounce(300) // delay the collection of data to not overburden the system
                        .collect { visibleItemPositions ->
                            val indexes = visibleItemPositions.map { it.index }
                            visibleAreasCoordinates.keys.removeAll { it !in indexes }
                            onUpdateVisibleAreas(visibleAreasCoordinates)
                        }
                }
                val areaPx = with(LocalDensity.current) { textFieldUnderFabHeight.toPx() }
                LaunchedEffect(
                    textFieldUnderFabIndex == (visibleAreasCoordinates.keys.maxOrNull() ?: -3)
                ) {
                    // -3 - 100% not equal to textFieldUnderFabIndex
                    if (textFieldUnderFabIndex != -2) {
                        listState.scrollBy(areaPx)
                        delay(500)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f, false))
            {
                SettingsButton(
                    onSettingsClick = onSettingsClick,
                    textID = R.string.main_menu_settings
                )
            }
        }
        FAB(
            modifier = Modifier.align(Alignment.BottomEnd),
            onFabClick = onFabClick,
            onFabChange = onFabChange,
            onFabStart = onFabStart,
            onFabEnd = onFabEnd,
            focusRequester = focusRequester
        )
        if (loadTasksStatus != LoadingState.Idle) {
            LoadingStatus(
                onRepeatLoading = onRepeatLoadingTasks,
                onResetRemoteState = onResetRemoteState,
                modifier = Modifier.align(Alignment.TopEnd),
                remoteState = loadTasksStatus
            )
        }
    }
}

@Composable
fun SearchButton(textID: Int, modifier: Modifier = Modifier) {
    val text = stringResource(id = textID)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { /*TODO*/ },
            modifier = Modifier.weight(9f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = DarkBlueBackground900, contentColor = PlaceholderText
            ),
            shape = RoundedCornerShape(10.dp),
            content = {
                Image(
                    painter = painterResource(id = R.drawable.ic_main_menu_search),
                    contentDescription = text,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = text,
                    style = typography.caption,
                )
            })
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MenuCategories(
    onCalculateLazySizes: (Int, Int, Dp) -> Unit,
    onMenuButtonClicked: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    menuCategories: List<MenuCategory>,
) {
    Column(
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
    ) {
        for (i in menuCategories.indices) when (i) {
            1, 4, menuCategories.lastIndex -> {
                MainMenuCategory(
                    onCalculateLazySizes = onCalculateLazySizes,
                    category = menuCategories[i],
                    onMenuButtonClicked = { onMenuButtonClicked(menuCategories[i].id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            else -> MainMenuCategory(
                onCalculateLazySizes = onCalculateLazySizes,
                category = menuCategories[i],
                onMenuButtonClicked = { onMenuButtonClicked(menuCategories[i].id) }
            )
        }
    }
}


@Composable
fun MainMenuCategory(
    onCalculateLazySizes: (Int, Int, Dp) -> Unit,
    onMenuButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
    category: MenuCategory,
) {
    val density = LocalDensity.current.density
    TextButton(
        onClick = onMenuButtonClicked,
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                onCalculateLazySizes(it.width, it.height, (it.height / density).dp)
            },
        content = {
            val name = stringResource(id = category.nameID)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Image(
                    painter = painterResource(id = category.iconID),
                    contentDescription = name,
                    modifier = Modifier
                        .weight(1f, false)
                        .fillMaxWidth()
                )
                Text(
                    text = name, style = typography.subtitle2, modifier = Modifier.weight(8f)
                )
                //TODO() This row does not center vertically
                Row(
                    modifier = Modifier
                        .weight(1f, false)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (category.tasksCounter > 0) {
                        Text(
                            modifier = Modifier,
                            text = category.tasksCounter.toString(),
                            style = typography.body1.copy(
                                fontWeight = FontWeight.Bold, color = Base500
                            )
                        )
                    }
                }
            }
        })
}

@Composable
fun SettingsButton(
    onSettingsClick: () -> Unit,
    textID: Int, modifier: Modifier = Modifier
) {
    val text = stringResource(id = textID)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onSettingsClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Background),
            shape = RoundedCornerShape(10.dp),
            content = {
                Image(
                    painter = painterResource(id = R.drawable.ic_main_menu_settings),
                    contentDescription = text,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = text,
                    style = typography.button.copy(
                        color = Base500
                    ),
                )
            })
    }
}

@Composable
fun AreaButton(
    onAreaClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
    area: AreaUI,
) {
    TextButton(
        onClick = { onAreaClick(area.id, area.name) },
        colors = ButtonDefaults.textButtonColors(backgroundColor = area.color),
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AreaImage(
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth(),
                areaName = area.name,
                areaIconID = area.iconID
            )
            Text(
                text = area.name, style = typography.subtitle2, modifier = Modifier.weight(9f)
            )
        }
    }
}

@Composable
fun AreaImage(
    modifier: Modifier = Modifier,
    areaName: String,
    areaIconID: Int = R.drawable.ic_main_menu_area
) {
    Image(
        painter = painterResource(id = areaIconID),
        contentDescription = areaName,
        modifier = modifier
    )
}


@Composable
fun RenderLazyColumn(
    onCalculateLazySizes: (Int, Int, Dp) -> Unit,
    onAddNewArea: (String) -> Unit,
    onReturnCoordinates: (Int, Float, Float) -> Unit,
    onAreaClick: (Long, String) -> Unit,
    index: Int,
    area: AreaUI,
    areasSize: Int,
    textFieldUnderFabIndex: Int,
    textFieldUnderFabHeight: Dp,
    newElementIndex: Int,
    focusRequester: FocusRequester,
) {
    if (index == 0 && (textFieldUnderFabIndex == -1 || newElementIndex == -1)) {
        TextFieldPlaceholderUnderFab(onAddNewArea, textFieldUnderFabHeight, focusRequester)
    }
    val density = LocalDensity.current.density
    AreaButton(
        onAreaClick = onAreaClick,
        modifier = Modifier
            .onSizeChanged {
                onCalculateLazySizes(it.width, it.height, (it.height / density).dp)
            }
            .onGloballyPositioned { coordinates ->
                onReturnCoordinates(
                    index,
                    coordinates.positionInRoot().x,
                    coordinates.positionInRoot().y
                )
            },
        area = area,
    )
    if (index != -1) {
        if ((index == textFieldUnderFabIndex || index == newElementIndex) ||
            (index == areasSize - 1 && (textFieldUnderFabIndex == areasSize - 1 || newElementIndex == areasSize - 1))
        ) {
            TextFieldPlaceholderUnderFab(onAddNewArea, textFieldUnderFabHeight, focusRequester)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingStatus(
    onRepeatLoading: () -> Unit,
    onResetRemoteState: () -> Unit,
    modifier: Modifier = Modifier,
    remoteState: LoadingState
) {
    when (remoteState) {
        LoadingState.Loading -> {
            CircularProgressIndicator(
                color = Base500,
                modifier = modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        }

        LoadingState.Error -> {
            ModalBottomSheet(
                onDismissRequest = onResetRemoteState,
                containerColor = DarkBlueBackground900,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkBlueBackground900)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error during loading tasks",
                        style = typography.body1.copy(
                            color = Base0
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onResetRemoteState,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = DarkBlueBackground800
                            ),
                        ) {
                            Text(
                                text = "Continue offline",
                                style = typography.button.copy(
                                    color = Base200
                                )
                            )
                        }
                        Button(
                            onClick = onResetRemoteState,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = DarkBlueBackground800
                            ),
                        ) {
                            Text(
                                text = "Repeat",
                                style = typography.button.copy(
                                    color = Base200
                                )
                            )
                        }
                    }
                }
            }
//            Box(
//                modifier = Modifier.clip(RoundedCornerShape(10.dp))
//            ) {
//                Row(
//                    modifier = modifier.background(DarkBlueBackground900),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_failed_loading),
//                        contentDescription = "Error",
//                        modifier = modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "Error during loading tasks",
//                        style = typography.body1.copy(
//                            color = Base0
//                        )
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Button(onClick = onRepeatLoading) {
//                        Text(
//                            text = "Repeat",
//                            style = typography.caption.copy(
//                                color = Base0
//                            ),
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(4.dp))
//                }
//            }
        }

        LoadingState.Success -> {
            Image(
                painter = painterResource(id = R.drawable.ic_success_loading),
                contentDescription = "Error",
                modifier = modifier
            )
        }

        else -> {}
    }
}