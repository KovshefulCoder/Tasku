package ru.kovsh.tasku.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.mainMenu.view.FAB
import ru.kovsh.tasku.ui.theme.AccentBlue
import ru.kovsh.tasku.ui.theme.Background
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.typography

@Composable
fun GeneralScaffold(
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit,
    focusRequester: FocusRequester,
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        floatingActionButton = { FAB(onFabClicked, { _, _ -> }, {}, {}, focusRequester = focusRequester) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier,
        content = content
    )
}


@Preview
@Composable
fun TaskuTopBar(
    onBackClicked: () -> Unit = {},
    screenTitle: String = "Area",
    screenIcon: Int = R.drawable.ic_main_menu_area
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onBackClicked() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back button",
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = screenIcon),
                contentDescription = "Logo",
            )
            Text(
                text = screenTitle,
                style = typography.h4.copy(color = Base0),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_screen_menu),
                contentDescription = "Settings",
            )
        }
    }
}


@Preview
@Composable
fun SortGroupRow(
    onSortClicked: () -> Unit = {},
    onGroupClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
    curSortStatus: String = "Sort",
    curGroupStatus: String = "Group"
    ) {
    var (sortColor, groupColor) = Pair(Base500, Base500)
    if (curSortStatus != "Sort") {
        sortColor = AccentBlue
    }
    if (curGroupStatus != "Group") {
        groupColor = AccentBlue
    }
    Row(
        modifier = modifier.fillMaxWidth().background(Background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.weight(1f, fill = false))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(4f)
                .clip(RoundedCornerShape(10.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
            ),
            elevation = null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = curSortStatus,
                    style = typography.caption.copy(color = sortColor)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_pointer_expand),
                    contentDescription = "Sort",
                    tint = sortColor,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f, fill = false))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(4f)
                .clip(RoundedCornerShape(10.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
            ),
            elevation = null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = curGroupStatus,
                    style = typography.caption.copy(color = groupColor)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_pointer_expand),
                    contentDescription = "Group",
                    tint = groupColor,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f, fill = false))
    }
}